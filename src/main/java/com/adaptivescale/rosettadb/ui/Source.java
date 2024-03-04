package com.adaptivescale.rosettadb.ui;

import com.adaptivescale.rosetta.common.models.Database;
import com.adaptivescale.rosetta.common.models.input.Connection;

public class Source extends Connection {

  private Boolean isExpanded = false;

  private Boolean isSelected = false;

  private Database database = null;

  public Boolean getExpanded() {
    return isExpanded;
  }

  public void setExpanded(Boolean expanded) {
    isExpanded = expanded;
  }

  public Boolean getSelected() {
    return isSelected;
  }

  public void setSelected(Boolean selected) {
    isSelected = selected;
  }

  public Database getDatabase() {
    return database;
  }

  public void setDatabase(Database database) {
    this.database = database;
  }
}
