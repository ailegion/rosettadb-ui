package com.adaptivescale.rosettadb.ui;

import com.adaptivescale.rosetta.common.JDBCDriverProvider;
import com.adaptivescale.rosetta.common.types.DriverClassName;
import com.vaadin.flow.component.dialog.Dialog;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Manager {
  private static Manager instance = null;

  private Set<Driver> driversSet = new HashSet<>();

  private Object activePage = null;

  private ActiveObjectContainer activeObjectContainer = null;

  private Dialog addSourceDialog = null;

  private LeftMenu sourcesMenu = null;
  private RightMenu targetsMenu = null;

  private List<Source> sources = new ArrayList<>();

  private HashMap<String, Source> sourceHashMap = new HashMap<>();

  private List<Source> targets = new ArrayList<>();

  private Manager() {
  }

  public static Manager getInstance() {
    if (instance == null) {
      instance = new Manager();
      // TODO remove - dev help only
      Source tempSource = new Source();
      tempSource.setUrl("jdbc:postgresql://localhost:5432/career_zen");
//      tempSource.setUrl("jdbc:postgresql:postgres");
      tempSource.setUserName("postgres");
      tempSource.setPassword("123456");
      tempSource.setName("career_zen");
      tempSource.setDbType("POSTGRES");
      tempSource.setSchemaName("career_zen");
      instance.addSource(tempSource);
    }
    return instance;
  }

  public Dialog getAddSourceDialog() {
    return addSourceDialog;
  }

  public void setAddSourceDialog(Dialog addSourceDialog) {
    this.addSourceDialog = addSourceDialog;
  }

  public LeftMenu getSourcesMenu() {
    return sourcesMenu;
  }

  public void setSourcesMenu(LeftMenu sourcesMenu) {
    this.sourcesMenu = sourcesMenu;
  }

  public List<Source> getSources() {
    return sources;
  }

  public void setSources(List<Source> sources) {
    this.sources = sources;
  }

  public List<Source> getTargets() {
    return targets;
  }

  public void setTargets(List<Source> targets) {
    this.targets = targets;
  }

  public RightMenu getTargetsMenu() {
    return targetsMenu;
  }

  public void setTargetsMenu(RightMenu targetsMenu) {
    this.targetsMenu = targetsMenu;
  }

  public void addSource(Source source) {
    sources.add(source);
    sourceHashMap.put(String.format("%s.%s", source.getName(), source.getUrl()), source);
  }

  public Source getSource(String sourceUniqueId) {
    return sourceHashMap.get(sourceUniqueId);
  }

  public void setSelectedSource(String sourceUniqueId) {
    sourceHashMap.values().forEach(source -> {
      source.setSelected(false);
    });
    getSource(sourceUniqueId).setSelected(true);
  }

  public static Connection getConnection(File jarFile, DriverClassName driverClassName, String url) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, SQLException {
    URL jarUrl = jarFile.toURI().toURL();
    URLClassLoader loader = new URLClassLoader(new URL[]{jarUrl}, ClassLoader.getSystemClassLoader());
    Class clazz = Class.forName(driverClassName.getValue(), true, loader);
    Properties prop = new Properties();
    prop.put("user", "admin");
    prop.put("password", "1234");
    Connection conn = (Connection) DriverManager.getConnection(url, prop);
    return conn;
  }

  public void addDriver(Driver driver) {
    Manager.getInstance().driversSet.add(driver);
  }

  public Driver getDriver(DriverClassName driverClassName) {
    for (Driver driver : driversSet) {
      if(driver.getDriverClassName().equals(driverClassName)){
        return driver;
      }
    }
    return null;
  }

  public void loadDriver(DriverClassName driverClassName) {
    Driver driverToLoad = Manager.getInstance().getDriver(driverClassName);
    Path path = Path.of(driverToLoad.getDriverPath());
    Logger.getAnonymousLogger().log(Level.INFO, String.format("Loading driver:%s", path));
    try {
      URLClassLoader loader = new URLClassLoader(new URL[]{path.toUri().toURL()}, ClassLoader.getSystemClassLoader());
      Class<?> driverClass = null;
      driverClass = Class.forName(DriverClassName.POSTGRES.getValue(), true, loader);
      java.sql.Driver driver = (java.sql.Driver) driverClass.getDeclaredConstructor().newInstance();
      Logger.getAnonymousLogger().log(Level.INFO, String.format("Registering driver:%s with driver manager", path));
      DriverManager.registerDriver(driver);
    } catch (ClassNotFoundException | SQLException | InvocationTargetException | InstantiationException |
             IllegalAccessException | NoSuchMethodException | MalformedURLException e) {
      throw new RuntimeException(e);
    }

  }

  private JDBCDriverProvider jdbcDriverProvider = new JDBCDriverProvider() {
    @Override
    public java.sql.Driver getDriver(com.adaptivescale.rosetta.common.models.input.Connection connection) throws SQLException {
      try {
        Driver driverToLoad = Manager.getInstance().getDriver(DriverClassName.valueOf(connection.getDbType()));
        Path path = Path.of(driverToLoad.getDriverPath());
        Logger.getAnonymousLogger().log(Level.INFO, String.format("Loading driver:%s", path));
        URLClassLoader loader = new URLClassLoader(new URL[]{path.toUri().toURL()}, ClassLoader.getSystemClassLoader());
        Class<?> driverClass = null;
        driverClass = Class.forName(DriverClassName.POSTGRES.getValue(), true, loader);
        java.sql.Driver driver = (java.sql.Driver) driverClass.getDeclaredConstructor().newInstance();
        return driver;
      } catch (MalformedURLException | ClassNotFoundException | InvocationTargetException | InstantiationException |
               IllegalAccessException | NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
    }
  };

  public JDBCDriverProvider getJdbcDriverProvider() {
    return jdbcDriverProvider;
  }

  public Object getActivePage() {
    return activePage;
  }

  public void setActivePage(Object activePage) {
    this.activePage = activePage;
  }

  public ActiveObjectContainer getActiveObjectContainer() {
    return activeObjectContainer;
  }

  public void setActiveObjectContainer(ActiveObjectContainer activeObjectContainer) {
    this.activeObjectContainer = activeObjectContainer;
  }

}
