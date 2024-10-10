package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Objects;

public class LoginScreenController {

    public TextField loginUsernameField;
    public TextField loginPasswordField;
    public Button loginButton;

    public void onLoginButtonClick(ActionEvent actionEvent) throws IOException {
        if (Objects.equals(loginUsernameField.getText(), "admin") && Objects.equals(loginPasswordField.getText(), "admin")) {
            FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("DocumentManagement.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 500);
            AppMain.window.setTitle("Hello!");
            AppMain.window.setScene(scene);
        }
    }
}
