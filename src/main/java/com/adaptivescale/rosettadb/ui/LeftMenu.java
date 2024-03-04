package com.adaptivescale.rosettadb.ui;

import com.adaptivescale.rosetta.common.models.Database;
import com.adataptivescale.rosetta.source.core.SourceGeneratorFactory;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LeftMenu extends VirtualList<Source> {
  LeftMenu() {
    Manager.getInstance().setSourcesMenu(this);
    setWidth(300, Unit.PIXELS);
    setHeight("100%"); // workaround for setHeightFull not working with split
    setItems(Manager.getInstance().getSources());
    setRenderer(new ComponentRenderer<>(source -> {
      H3 name = new H3(source.getName());
      H6 url = new H6(String.format("Url:%s", source.getUrl()));
      VerticalLayout summary = new VerticalLayout();
      summary.setPadding(false);
      summary.setSpacing(false);
      summary.add(name);
      summary.add(url);
      Button expand = new Button(new Icon(VaadinIcon.ANGLE_RIGHT), buttonClickEvent -> {
        String sourceId = String.format("%s.%s", source.getName(), source.getUrl());
        Source temp = Manager.getInstance().getSource(sourceId);
        temp.setExpanded(!temp.getExpanded());
        Logger.getAnonymousLogger().log(Level.ALL, String.format("%s expand status %s", source.getName(), source.getExpanded()));
        buttonClickEvent.getSource().setIcon(temp.getExpanded()?new Icon(VaadinIcon.ANGLE_DOWN):new Icon(VaadinIcon.ANGLE_RIGHT));
      });
      expand.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

      HorizontalLayout sourceInformation = new HorizontalLayout(summary);
      sourceInformation.setSpacing(false);
      sourceInformation.setWidthFull();
      sourceInformation.getStyle().set("padding-left", "6px");
      sourceInformation.getStyle().set("padding-right", "6px");
      sourceInformation.getStyle().set("cursor", "pointer");
      sourceInformation.add(expand, summary);
      sourceInformation.addSingleClickListener(e->{
        String sourceId = String.format("%s.%s", source.getName(), source.getUrl());
        Manager.getInstance().setSelectedSource(sourceId);
        Source temp = Manager.getInstance().getSource(sourceId);
        e.getSource().getStyle().set("background-color",temp.getSelected()?"gray":"white");
        if(Optional.ofNullable(source.getDatabase()).isEmpty()){
          try {
            // TODO - refactor - temporary workaround
            Manager.getInstance().loadJdbcDrivers();

            Database database = SourceGeneratorFactory.sourceGenerator(temp).generate(temp);
            temp.setDatabase(database);
          } catch (Exception ex) {
            Notification notification = new Notification();
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.add(new HorizontalLayout(new Text(String.format("Failed to connect: %s", ex.getMessage()))));
            notification.open();
          }
        }
      });
      return sourceInformation;
    }));
  }

}
