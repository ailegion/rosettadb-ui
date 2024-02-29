package com.adaptivescale.rosettadb.ui;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

public class AddSourceDialog extends Dialog {

  private TextField name = new TextField("Name");
  private TextField url =new TextField("Url");
  private TextField username = new TextField("Username");
  private PasswordField password = new PasswordField("Password");
  AddSourceDialog() {
    Manager.getInstance().setAddSourceDialog(this);
    Manager.getInstance().getAddSourceDialog().setWidth(300, Unit.PIXELS);
    Manager.getInstance().getAddSourceDialog().setHeaderTitle("Add source");
    Manager.getInstance().getAddSourceDialog().getHeader().add(
      new Button(new Icon("lumo", "cross"),
        (e) -> Manager.getInstance().getAddSourceDialog().close()
      )
    );
    FormLayout dialogFormLayout = new FormLayout();
    dialogFormLayout.add(name);
    dialogFormLayout.add(url);
    dialogFormLayout.add(username);
    dialogFormLayout.add(password);
    Manager.getInstance().getAddSourceDialog().add(dialogFormLayout);
    Manager.getInstance().getAddSourceDialog().getFooter().add(new Button("Save", e->{
      Source newSource = new Source();
      newSource.setName(name.getValue());
      newSource.setUrl(url.getValue());
      newSource.setUsername(username.getValue());
      newSource.setPassword(password.getValue());
      Manager.getInstance().getSources().add(newSource);
      Manager.getInstance().getSourcesMenu().setItems(Manager.getInstance().getSources());
      Manager.getInstance().getAddSourceDialog().close();
    }));
  }

  public TextField getUrl() {
    return url;
  }

  public void setUrl(TextField url) {
    this.url = url;
  }

  public TextField getUsername() {
    return username;
  }

  public void setUsername(TextField username) {
    this.username = username;
  }

  public PasswordField getPassword() {
    return password;
  }

  public void setPassword(PasswordField password) {
    this.password = password;
  }
}
