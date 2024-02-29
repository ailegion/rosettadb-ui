package com.adaptivescale.rosettadb.ui;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;

public class LeftMenu extends VirtualList<Source> {
  LeftMenu() {
    Manager.getInstance().setSourcesMenu(this);
    setWidth(200, Unit.PIXELS);
    setHeightFull();
    getStyle().set("border-right","1px solid black");
    setItems(Manager.getInstance().getSources());
    setRenderer(new ComponentRenderer<>(source -> {
      Span name = new Span(source.getName());
      Span url = new Span(String.format("Url:%s", source.getUrl()));
      Div summary = new Div();
      summary.add(name);
      summary.add(url);
      AccordionPanel sourceInformation = new AccordionPanel(summary, url);
      sourceInformation.setWidthFull();
      sourceInformation.getStyle().set("padding-left", "6px");
      return sourceInformation;
    }));
  }

}
