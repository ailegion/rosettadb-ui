package com.adaptivescale.rosettadb.ui;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

//@PageTitle("Main")
//@Route(value = "/dashboard")
public class Container extends HorizontalLayout {
  Container(){
    setHeightFull();
    HorizontalLayout mainContainer = new HorizontalLayout();
    mainContainer.add(new LeftMenu());
    HorizontalLayout mainContentHolder = new HorizontalLayout();
    mainContainer.setWidthFull();
    mainContainer.setHeightFull();
    Scroller scroller = new Scroller();
    scroller.setContent(mainContentHolder);
    mainContainer.add(scroller); // TODo store as variable (main container)
    scroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
    scroller.setWidthFull();
    mainContainer.add(new RightMenu());
    add(mainContainer);
//    addToNavbar(mainContainer);
  }
}
