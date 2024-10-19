package org.proview.test;

import org.proview.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.proview.model.UserManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class LoginView {

    public TextField loginUsernameField;
    public TextField loginPasswordField;
    public Button loginButton;
    public Button registerButton;

    public void onLoginButtonClick(ActionEvent actionEvent) throws IOException, SQLException {
        if (Objects.equals(loginUsernameField.getText(), "admin") && Objects.equals(loginPasswordField.getText(), "admin")) {
            FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("EditDocumentView.fxml"));
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

        String username = loginUsernameField.getText();
        String password = loginPasswordField.getText();

        if (Objects.equals(loginUsernameField.getText(), "")) {
            System.out.println("Please enter username");
            return;
        }

        if (Objects.equals(loginPasswordField.getText(), "")) {
            System.out.println("Please enter password");
            return;
        }

        User checkingUser = UserManagement.isValidLoginCredentials(username, password);
        if (checkingUser == null) {
            System.out.println("Username or password is incorrect");
            return;
        }

        // Save details of logged in user
        UserManagement.setCurrentUser(checkingUser);

        // Check if user is admin
        if (checkingUser.getType() == 0) {
            FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("EditDocumentView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 500);
            AppMain.window.setTitle("Hello!");
            AppMain.window.setScene(scene);
        }

        // Check if user is normal user
        if (checkingUser.getType() == 1) {
            FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("CreateIssueView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 500);
            AppMain.window.setTitle("Hello!");
            AppMain.window.setScene(scene);
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
