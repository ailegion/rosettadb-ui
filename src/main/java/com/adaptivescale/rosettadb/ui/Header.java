package com.adaptivescale.rosettadb.ui;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;


public class Header extends HorizontalLayout {
  Header() {
    Text brand = new Text("RosettaDB UI");
    new AddSourceDialog();

    Button addDataSource = new Button("Add source", e->Manager.getInstance().getAddSourceDialog().open());
    addDataSource.addThemeVariants(ButtonVariant.LUMO_SMALL);

    add(brand, addDataSource);
  }
}
