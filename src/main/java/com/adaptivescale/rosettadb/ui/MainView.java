package com.adaptivescale.rosettadb.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Main")
@Route(value = "")
public class MainView extends AppLayout {
  public MainView() {
    addToNavbar(new Header());

    setContent(new Container());
  }
}
