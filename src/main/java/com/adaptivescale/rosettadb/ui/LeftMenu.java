package com.adaptivescale.rosettadb.ui;

import com.adaptivescale.rosetta.common.models.Column;
import com.adaptivescale.rosetta.common.models.Database;
import com.adaptivescale.rosetta.common.models.Table;
import com.adaptivescale.rosetta.common.types.DriverClassName;
import com.adataptivescale.rosetta.source.core.SourceGeneratorFactory;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
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
    setRenderer(treeDetailsRenderer());
  }

  private static ComponentRenderer<Details, Source> treeDetailsRenderer() {
    return new ComponentRenderer<>(source -> {
      VerticalLayout sourceDetails = new VerticalLayout();
      sourceDetails.add(new H6(source.getName()));
      sourceDetails.add(new H6(source.getUrl()));
      VirtualList<Database> databaseList = new VirtualList<Database>();
      databaseList.setRenderer(new ComponentRenderer<com.vaadin.flow.component.Component, Database>(database -> {
        VirtualList<Table> tableList = new VirtualList<Table>();
        tableList.setRenderer(new ComponentRenderer<com.vaadin.flow.component.Component, Table>(table -> {
          VirtualList<Column> columnVirtualList = new VirtualList<Column>();
          columnVirtualList.setRenderer(new ComponentRenderer<com.vaadin.flow.component.Component, Column>(column -> {
            Span tableName = new Span(String.format("%s (%s)", column.getName(), column.getTypeName()));
            return tableName;
          }));
          columnVirtualList.setItems(table.getColumns());
          columnVirtualList.getStyle().setPaddingLeft("30px");
          Details tableDetails =  new Details(table.getName(), columnVirtualList);
          tableDetails.getStyle().setBorderBottom("1px solid black");
          return tableDetails;
        }));
        tableList.setItems(database.getTables());
        tableList.getStyle().setPaddingLeft("20px");
        Button viewDb = new Button(new Icon(VaadinIcon.BOOK), buttonClickEvent -> {
          Manager.getInstance().setActivePage(database);
          Manager.getInstance().getActiveObjectContainer().render();
        });
        HorizontalLayout dbInfo = new HorizontalLayout(new Span(database.getDatabaseProductName()), viewDb);
        Details dbDetails = new Details();
        dbDetails.setSummary(dbInfo);
        dbDetails.add(tableList);
        dbDetails.getStyle().setBorderBottom("1px solid black");
        return dbDetails;
      }));
      databaseList.setItems(source.getDatabase());
      databaseList.getStyle().setPaddingLeft("10px");
      Details details = new Details(sourceDetails, databaseList);
      details.getStyle().setBorderBottom("1px solid black");
      details.addOpenedChangeListener(sourceDetailsOpened(source, details));
      return details;
    });
  }

  private static ComponentEventListener<Details.OpenedChangeEvent> sourceDetailsOpened(Source source, Details details) {
    return openedChangeEvent -> {
      if (openedChangeEvent.isOpened() && Optional.ofNullable(source.getDatabase()).isEmpty()) {
        String sourceId = String.format("%s.%s", source.getName(), source.getUrl());
        Manager.getInstance().setSelectedSource(sourceId);
        Source temp = Manager.getInstance().getSource(sourceId);
        if (Optional.ofNullable(source.getDatabase()).isEmpty()) {
          try {
            Manager.getInstance().loadDriver(DriverClassName.valueOf(temp.getDbType()));
            Database database = SourceGeneratorFactory.sourceGenerator(temp, Manager.getInstance().getJdbcDriverProvider()).generate(temp);
            temp.setDatabase(database);
            Logger.getAnonymousLogger().log(Level.INFO, String.format("Introspecting %s", source.getName()));
            Manager.getInstance().getSourcesMenu().getDataProvider().refreshAll();
          } catch (Exception ex) {
            Notification notification = new Notification();
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.add(new HorizontalLayout(new Text(String.format("Failed to connect: %s", ex.getMessage()))));
            notification.open();
          }
        }
      }
    };
  }

  ;

//  private static ComponentRenderer<HorizontalLayout, Source> treeDetailsRenderer() {
//    return new ComponentRenderer<>(source -> {
//      H3 name = new H3(source.getName());
//      H6 url = new H6(String.format("Url:%s", source.getUrl()));
//      VerticalLayout summary = new VerticalLayout();
//      summary.setPadding(false);
//      summary.setSpacing(false);
//      summary.add(name);
//      summary.add(url);
//      Button expand = new Button(new Icon(VaadinIcon.ANGLE_RIGHT), buttonClickEvent -> {
//        String sourceId = String.format("%s.%s", source.getName(), source.getUrl());
//        Source temp = Manager.getInstance().getSource(sourceId);
//        temp.setExpanded(!temp.getExpanded());
//        Logger.getAnonymousLogger().log(Level.ALL, String.format("%s expand status %s", source.getName(), source.getExpanded()));
//        buttonClickEvent.getSource().setIcon(temp.getExpanded() ? new Icon(VaadinIcon.ANGLE_DOWN) : new Icon(VaadinIcon.ANGLE_RIGHT));
//      });
//      expand.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
//
//      HorizontalLayout sourceInformation = new HorizontalLayout(summary);
//      sourceInformation.setSpacing(false);
//      sourceInformation.setWidthFull();
//      sourceInformation.getStyle().set("padding-left", "6px");
//      sourceInformation.getStyle().set("padding-right", "6px");
//      sourceInformation.getStyle().set("cursor", "pointer");
//      sourceInformation.add(expand, summary);
//      sourceInformation.addSingleClickListener(clickEventListener(source, sourceInformation));
//      return sourceInformation;
//    });
//  }

//  private static ComponentEventListener<ClickEvent<HorizontalLayout>> clickEventListener(Source source, HorizontalLayout sourceInformation) {
  private static ComponentEventListener<ClickEvent<Details>> clickEventListener(Source source, Details sourceInformation) {
    return e -> {
      String sourceId = String.format("%s.%s", source.getName(), source.getUrl());
      Manager.getInstance().setSelectedSource(sourceId);
      Source temp = Manager.getInstance().getSource(sourceId);
      e.getSource().getStyle().set("background-color", temp.getSelected() ? "gray" : "white");
      if (Optional.ofNullable(source.getDatabase()).isEmpty()) {
        try {
          Manager.getInstance().loadDriver(DriverClassName.valueOf(temp.getDbType()));
          Database database = SourceGeneratorFactory.sourceGenerator(temp, Manager.getInstance().getJdbcDriverProvider()).generate(temp);
          temp.setDatabase(database);
          Logger.getAnonymousLogger().log(Level.INFO, String.format("Introspecting %s", source.getName()));
          Manager.getInstance().getSourcesMenu().setItems(Manager.getInstance().getSources());
        } catch (Exception ex) {
          Notification notification = new Notification();
          notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
          notification.setDuration(5000);
          notification.add(new HorizontalLayout(new Text(String.format("Failed to connect: %s", ex.getMessage()))));
          notification.open();
        }
      }
    };
  }

  public void rerenderItems() {
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
        buttonClickEvent.getSource().setIcon(temp.getExpanded() ? new Icon(VaadinIcon.ANGLE_DOWN) : new Icon(VaadinIcon.ANGLE_RIGHT));
      });
      expand.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
      VerticalLayout content = new VerticalLayout();

      HorizontalLayout sourceInformation = new HorizontalLayout(summary);
      sourceInformation.setSpacing(false);
      sourceInformation.setWidthFull();
      sourceInformation.getStyle().set("padding-left", "6px");
      sourceInformation.getStyle().set("padding-right", "6px");
      sourceInformation.getStyle().set("cursor", "pointer");
      sourceInformation.add(expand, summary);

      content.add(sourceInformation);

      VerticalLayout details = new VerticalLayout();
      details.setSpacing(false);
      details.setPadding(false);
      details.getStyle().set("padding-left", "20px");
      if (Optional.ofNullable(source.getDatabase()).isPresent()) {
        HorizontalLayout subItem = new HorizontalLayout();
        subItem.setSpacing(false);
        subItem.setPadding(false);
        H6 dbName = new H6(source.getDatabaseName());
        subItem.add(dbName);
        VerticalLayout tables = new VerticalLayout();
        source.getDatabase().getTables().forEach(table -> {
          HorizontalLayout tableInfo = new HorizontalLayout();
          tableInfo.add(new H6(table.getName()));
          tableInfo.add(new Span(table.getDescription()));
          tables.add(tableInfo);
        });
        details.add(subItem);
        details.add(tables);
        content.add(details);

      }
      return content;
    }));

  }

}
