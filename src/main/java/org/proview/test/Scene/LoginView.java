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
import java.util.Objects;

public class LoginView {

    public TextField loginUsernameField;
    public TextField loginPasswordField;
    public Button loginButton;
    public Button registerButton;
    public Label loginResultLabel;

    public void onLoginButtonClick(ActionEvent actionEvent) throws IOException, SQLException {
        String username = loginUsernameField.getText();
        String password = loginPasswordField.getText();

        if (Objects.equals(loginUsernameField.getText(), "")) {
            loginResultLabel.setText("Please enter username!");
            return;
        }

        if (Objects.equals(loginPasswordField.getText(), "")) {
            loginResultLabel.setText("Please enter password!");
            return;
        }

        User checkingUser = SQLUtils.getUser(username, password);
        if (checkingUser == null) {
            loginResultLabel.setText("Username/Password is incorrect");
            return;
        }

        // Save details of logged-in user
        UserManagement.setCurrentUser(checkingUser);

        Utils.switchScene("HomeView.fxml");
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