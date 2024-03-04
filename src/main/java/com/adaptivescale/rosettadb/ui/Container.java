package com.adaptivescale.rosettadb.ui;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;


public class Container extends HorizontalLayout {
  Container(){
    setHeightFull();
    // Init menus for Manager
    new LeftMenu();
    new RightMenu();
    HorizontalLayout mainContainer = new HorizontalLayout();
    HorizontalLayout detailContainer = new HorizontalLayout();
    SplitLayout leftSplit = new SplitLayout(Manager.getInstance().getSourcesMenu(), detailContainer);

    mainContainer.add(leftSplit);
    add(mainContainer);
  }
}
