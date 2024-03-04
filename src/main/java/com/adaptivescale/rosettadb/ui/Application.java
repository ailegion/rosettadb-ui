package com.adaptivescale.rosettadb.ui;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements AppShellConfigurator {

  public static void main(String[] args) {
    System.setProperty("java.library.path", "drivers");
    SpringApplication.run(Application.class, args);
  }

}
