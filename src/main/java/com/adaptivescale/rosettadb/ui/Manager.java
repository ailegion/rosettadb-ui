package com.adaptivescale.rosettadb.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

public class Manager {
  private static Manager instance = null;

  private Dialog addSourceDialog = null;

  private LeftMenu sourcesMenu = null;
  private RightMenu targetsMenu = null;

  private List<Source> sources = new ArrayList<>();
  private List<Source> targets = new ArrayList<>();

  private Manager() {
  }

  public static Manager getInstance() {
    if(instance==null) {
      instance = new Manager();
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
}
