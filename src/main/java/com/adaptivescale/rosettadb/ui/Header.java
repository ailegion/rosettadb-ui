package com.adaptivescale.rosettadb.ui;

import com.adaptivescale.rosetta.common.types.DriverClassName;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.server.StreamResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Header extends HorizontalLayout {
  Header() {
    Image logo = new Image(new StreamResource("logo.png",
      () -> getClass().getResourceAsStream("/logo.png")),"RosettaDB");
    logo.setHeight(42, Unit.PIXELS);

    new AddSourceDialog();

    Button addDataSource = new Button("Add source", e->Manager.getInstance().getAddSourceDialog().open());
    addDataSource.addThemeVariants(ButtonVariant.LUMO_SMALL);

    MemoryBuffer buffer = new MemoryBuffer();

    Upload addDriver = new Upload(buffer);
    addDriver.setWidth("300px");
    addDriver.setDropAllowed(false);
    Button addDriverButton = new Button("Add driver");
    addDriver.setUploadButton(addDriverButton);
    int maxFileSizeInBytes = 100 * 1024 * 1024; // 100MB
    addDriver.setMaxFiles(maxFileSizeInBytes);
    addDriver.setAcceptedFileTypes("driver", ".jar");

    addDriver.addFileRejectedListener(event -> {
      String errorMessage = event.getErrorMessage();

      Notification notification = Notification.show(errorMessage, 5000,
        Notification.Position.MIDDLE);
      notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    });
    addDriver.addSucceededListener(succeededEvent -> {

      Path path = Paths.get("drivers"+ File.separator + succeededEvent.getFileName());
      if(Files.exists(path)) {
        ConfirmDialog fileExistsDialog = new ConfirmDialog();
        fileExistsDialog.setHeader("Driver exists");
        fileExistsDialog.setText("Driver exists. Would you like to replace?");
        fileExistsDialog.setRejectable(true);
        fileExistsDialog.setRejectText("No");
        fileExistsDialog.addRejectListener(event -> {
          Logger.getAnonymousLogger().log(Level.INFO,"Replace canceled");
          fileExistsDialog.close();
          addDriver.clearFileList();
        });
        fileExistsDialog.setConfirmText("Yes");
        fileExistsDialog.addConfirmListener(event -> {
          Logger.getAnonymousLogger().log(Level.INFO,String.format("Replacing driver %s",path));
          saveDriver(path, buffer);
          addDriver.clearFileList();
        });
        fileExistsDialog.open();
      } else{
        saveDriver(path, buffer);
      }
    });

    add(logo, addDataSource, addDriver);
  }

  void saveDriver(Path path, MemoryBuffer buffer) {
    // TODO refactor this part
    try {
      if(!Files.isDirectory(Path.of("drivers"))){
        Logger.getAnonymousLogger().log(Level.INFO, String.format("Creating directory:%s", Path.of("drivers")));
        Files.createDirectory(Path.of("drivers"));
      }

      Logger.getAnonymousLogger().log(Level.INFO, String.format("Storing driver:%s", path));
      Files.write(path, buffer.getInputStream().readAllBytes());
      Driver driver = new Driver();
      driver.setDriverPath(path.toString());
      driver.setDriverClassName(DriverClassName.POSTGRES); // TODO fix
      Manager.getInstance().addDriver(driver);

      Notification notification = Notification.show("Driver uploaded", 5000, Notification.Position.BOTTOM_END);
      notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    } catch (IOException e) {
      Notification notification = Notification.show(e.getMessage(), 5000, Notification.Position.BOTTOM_END);
      notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
  }
}
