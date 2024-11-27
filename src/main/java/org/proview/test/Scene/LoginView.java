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
import org.proview.utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Objects;

public class LoginView {
    public TextField loginUsernameField;
    public TextField loginPasswordField;
    public Button loginButton;
    public Button registerButton;
    public Label loginResultLabel;
    public Label errorLabel;
    public void onLoginButtonClick(ActionEvent actionEvent) {
        try {
            String username = loginUsernameField.getText();
            String password = loginPasswordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                throw new IllegalArgumentException("Username and password must not be empty.");
            }
            if (!username.matches("^[a-zA-Z0-9_]+$")) { // Only alphanumeric and underscore
                throw new IllegalArgumentException("Username can only contain letters, numbers, and underscores.");
            }
            if (!password.equalsIgnoreCase("admin") && password.length() < 8) {
                throw new IllegalArgumentException("Password must be at least 8 characters long.");
            }

            User checkingUser = SQLUtils.getUser(username, password);
            if (checkingUser == null) {
                throw new IllegalArgumentException("Username or password is incorrect.");
            }

            // Save details of logged-in user
            UserManagement.setCurrentUser(checkingUser);
            Utils.switchScene("HomeView.fxml");
        }
        catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
        }
        catch (Exception e) {
            errorLabel.setText("An unexpected error occurred.");
        }

    }

    public void initialize() {
        errorLabel.setText("");
    }
    public void onRegisterButtonClick(ActionEvent actionEvent) throws IOException {
        Utils.switchScene("RegisterView.fxml");
    }

    public void onKeyReleased(KeyEvent keyEvent) throws IOException, SQLException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            this.onLoginButtonClick(new ActionEvent());
        }
    }
}