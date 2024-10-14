package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class LoginScreenController {

    public TextField loginUsernameField;
    public TextField loginPasswordField;
    public Button loginButton;
    public Button registerButton;

    public void onLoginButtonClick(ActionEvent actionEvent) throws IOException, SQLException {
        if (Objects.equals(loginUsernameField.getText(), "admin") && Objects.equals(loginPasswordField.getText(), "admin")) {
            FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("DocumentManagement.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 500);
            AppMain.window.setTitle("Hello!");
            AppMain.window.setScene(scene);
        } else {
            String usn = loginUsernameField.getText();
            String pw = loginPasswordField.getText();
            if (Objects.equals(loginUsernameField.getText(), "")) {
                System.out.println("Please enter username");
            } else if (Objects.equals(loginPasswordField.getText(), "")) {
                System.out.println("Please enter password");
            } else if (UserManagement.isValidLoginCredentials(usn, pw) == null) {
                System.out.println("Wrong username or password");
            } else {
                System.out.println("Welcome, " + usn);
            }
        }
    }

    public void onRegisterButtonClick(ActionEvent actionEvent) throws SQLException {
        if (Objects.equals(loginUsernameField.getText(), "")) {
            System.out.println("Please enter username");
        } else if (Objects.equals(loginPasswordField.getText(), "")) {
            System.out.println("Please enter password");
        } else {
            UserManagement.addNormalUser(new User(loginUsernameField.getText(), loginPasswordField.getText(), 1));
        }
    }
}
