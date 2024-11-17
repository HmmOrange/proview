package org.proview.test.Scene;

import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.proview.modal.User.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.proview.modal.User.UserManagement;
import org.proview.utils.SQLUtils;
import org.proview.test.AppMain;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class LoginView {

    public TextField loginUsernameField;
    public TextField loginPasswordField;
    public Button loginButton;
    public Button registerButton;
    public Label loginResultLabel;

    public void onLoginButtonClick(ActionEvent actionEvent) throws IOException, SQLException {
        /*
        String usn = loginUsernameField.getText();
        String pw = loginPasswordField.getText();
        if (Objects.equals(loginUsernameField.getText(), "")) {
            loginResultLabel.setText("Please enter username");
        } else if (Objects.equals(loginPasswordField.getText(), "")) {
            loginResultLabel.setText("Please enter password");
        } else if (UserManagement.isValidLoginCredentials(usn, pw) == null) {
            loginResultLabel.setText("Wrong username or password");
        } else {
            loginResultLabel.setText("Welcome, " + usn);
        }
        */

        String username = loginUsernameField.getText();
        String password = loginPasswordField.getText();

        if (Objects.equals(loginUsernameField.getText(), "")) {
            loginResultLabel.setText("Please enter username");
            return;
        }

        if (Objects.equals(loginPasswordField.getText(), "")) {
            loginResultLabel.setText("Please enter password");
            return;
        }

        User checkingUser = SQLUtils.getUser(username, password);
        if (checkingUser == null) {
            loginResultLabel.setText("Username or password is incorrect");
            return;
        }

        // Save details of logged-in user
        UserManagement.setCurrentUser(checkingUser);

        // Check if user is admin
        /*if (checkingUser.getType() == 0) {
            FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("HomeView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
            AppMain.window.setTitle("Hello!");
            AppMain.window.setScene(scene);
            AppMain.window.centerOnScreen();
        }

        // Check if user is normal user
        if (checkingUser.getType() == 1) {
            FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("CreateIssueView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 500);
            AppMain.window.setTitle("Hello!");
            AppMain.window.setScene(scene);
            AppMain.window.centerOnScreen();
        }*/
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("HomeView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }

    public void onRegisterButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("RegisterView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }

    public void onKeyReleased(KeyEvent keyEvent) throws IOException, SQLException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            this.onLoginButtonClick(new ActionEvent());
        }
    }
}
