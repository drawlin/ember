package me.drawlin.ember;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    TextField fieldEmail;

    @FXML
    TextField fieldDesiredName;

    @FXML
    PasswordField fieldPassword;

    @FXML
    Label fieldStatus;

    public void beginTaskButton(ActionEvent event) {
        NameChangeTask nameChangeTask = new NameChangeTask(fieldEmail.getText(), fieldPassword.getText(), fieldDesiredName.getText());

        fieldStatus.setText("Status: Starting task...");

        try {
            nameChangeTask.startTask();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
