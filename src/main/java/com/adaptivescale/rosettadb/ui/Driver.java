package com.adaptivescale.rosettadb.ui;

import com.adaptivescale.rosetta.common.types.DriverClassName;

public class Driver {
  private DriverClassName driverClassName;
  private String driverPath;

  public DriverClassName getDriverClassName() {
    return driverClassName;
  }

  public void setDriverClassName(DriverClassName driverClassName) {
    this.driverClassName = driverClassName;
  }

  public String getDriverPath() {
    return driverPath;
  }

  public void setDriverPath(String driverPath) {
    this.driverPath = driverPath;
  }
}
