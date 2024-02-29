package com.adaptivescale.rosettadb.ui;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.virtuallist.VirtualList;

public class RightMenu extends VirtualList<Source> {
  RightMenu() {
    Manager.getInstance().setTargetsMenu(this);
    setHeightFull();
    setWidth(200, Unit.PIXELS);
    getStyle().set("border-left","1px solid black");
    setItems(Manager.getInstance().getTargets());
  }
}
