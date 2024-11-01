package org.proview.test;

import javafx.scene.control.Label;
import org.proview.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.proview.model.UserManagement;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
public class RegisterView {
    public TextField registerUsernameField;
    public TextField registerPasswordField;
    public TextField registerConfirmPasswordField;
    public Button confirmButton;
    public Button backButton;
    public Label registerResultLabel;

    public void onConfirmButtonClick(ActionEvent actionEvent) throws IOException, SQLException {
        if (Objects.equals(registerUsernameField.getText(), "") ||
        Objects.equals(registerPasswordField.getText(), "") || Objects.equals(registerConfirmPasswordField.getText(), "")) {
            registerResultLabel.setText("Please fill in all blanks");
        }
        else {
            String usn = registerUsernameField.getText();
            String pass = registerPasswordField.getText();
            String cfpass = registerConfirmPasswordField.getText();

            /// check if username exists
            String sql = "SELECT username FROM user WHERE username = ?";
            Connection connection = AppMain.connection;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, usn);
            ResultSet resultSet = preparedStatement.executeQuery();
            ///

            if (!Objects.equals(pass, cfpass)) registerResultLabel.setText("Password wrong");
            else if (resultSet.next()) registerResultLabel.setText("Username already exists!");
            else {
                User us = new User(usn, pass, 1);
                UserManagement.addNormalUser(us);
                registerResultLabel.setText("Register Success");

                FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("LoginView.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 500, 500);
                LoginView tempLoginView = fxmlLoader.getController();
                tempLoginView.loginResultLabel.setText("Register Success. Please log in!");
                AppMain.window.setTitle("Hello!");
                AppMain.window.setScene(scene);
                AppMain.window.centerOnScreen();
            }
        }
    }

    public void onBackButtonClick(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }
}
