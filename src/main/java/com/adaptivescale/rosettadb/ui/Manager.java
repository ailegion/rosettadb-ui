package com.adaptivescale.rosettadb.ui;

import com.adaptivescale.rosetta.common.types.DriverClassName;
import com.vaadin.flow.component.dialog.Dialog;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manager {
  private static Manager instance = null;

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
      tempSource.setUrl("jdbc:postgresql://localhost:5432/postgres");
      tempSource.setUserName("postgres");
      tempSource.setPassword("123456");
      tempSource.setName("postgres");
      tempSource.setDbType("POSTGRES");
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

//  public void reloadDrivers() {
//    loadDriverFor(DriverClassName.MYSQL);
//    loadDriverFor(DriverClassName.POSTGRES);
//    loadDriverFor(DriverClassName.KINETICA);
//  }

  void  loadJdbcDrivers() {
    try {
      // Create a File object pointing to your drivers directory
      File driversDir = new File("drivers");
      // Filter to only .jar files
      File[] driverFiles = driversDir.listFiles(new FilenameFilter() {
        public boolean accept(File dir, String name) {
          return name.toLowerCase().endsWith(".jar");
        }
      });

      if (driverFiles != null && driverFiles.length > 0) {
        URL[] urls = new URL[driverFiles.length];
        for (int i = 0; i < driverFiles.length; i++) {
          // Convert the file path to a URL
          urls[i] = driverFiles[i].toURI().toURL();
        }
        // Create a new URLClassLoader with the directory
        URLClassLoader loader = new URLClassLoader(urls);
        // Load and register each driver class with the DriverManager
        for (URL url : urls) {
          for (DriverClassName driverClassName : DriverClassName.values()) {
            // Load the driver class
            try {
              Class<?> driverClass = Class.forName(driverClassName.getValue(), true, loader);
              // Register the driver with the DriverManager
              DriverManager.registerDriver((Driver) driverClass.newInstance());
            } catch (Exception e) {

            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
