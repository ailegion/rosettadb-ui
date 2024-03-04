package com.adaptivescale.rosettadb.ui;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.hilerio.ace.AceEditor;
import com.hilerio.ace.AceMode;
import com.hilerio.ace.AceTheme;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.Optional;

public class ActiveObjectContainer extends HorizontalLayout {
  private final AceEditor aceEditor = new AceEditor();
  ActiveObjectContainer() {
    Manager.getInstance().setActiveObjectContainer(this);
    setWidthFull();
    setHeightFull();
    aceEditor.setTheme(AceTheme.terminal);
    aceEditor.setMode(AceMode.yaml);
    aceEditor.setValue("-- no content");
    aceEditor.setHeightFull();
    add(aceEditor);
  }
  public void render() {
    if(Optional.ofNullable(Manager.getInstance().getActivePage()).isPresent()) {
      try {
        String output = new ObjectMapper(new YAMLFactory()).setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsString(Manager.getInstance().getActivePage());
        aceEditor.setValue(output);
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
