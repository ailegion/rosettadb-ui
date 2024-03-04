package com.adaptivescale.rosettadb.ui;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;


public class Container extends HorizontalLayout {
  Container(){
    setHeightFull();
    // Init menus for Manager
    new LeftMenu();
    new RightMenu();
    new ActiveObjectContainer();
    HorizontalLayout mainContainer = new HorizontalLayout();
    mainContainer.setWidthFull();
    mainContainer.setHeightFull();
    SplitLayout leftSplit = new SplitLayout(Manager.getInstance().getSourcesMenu(), Manager.getInstance().getActiveObjectContainer());
    leftSplit.setWidthFull();
    leftSplit.setHeightFull();
    mainContainer.add(leftSplit);
    add(mainContainer);
  }
}
