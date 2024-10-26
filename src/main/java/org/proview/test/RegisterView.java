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
public class RegisterView {
    public TextField registerUsernameField;
    public TextField registerPasswordField;
    public TextField registerConfirmPasswordField;
    public Button confirmButton;
    public Button backButton;

    public void onConfirmButtonClick(ActionEvent actionEvent) throws IOException, SQLException {
        if (Objects.equals(registerUsernameField.getText(), "") ||
        Objects.equals(registerPasswordField.getText(), "") || Objects.equals(registerConfirmPasswordField.getText(), "")) {
            System.out.println("Please fill in all blanks");
        }
        else {
            String usn = registerUsernameField.getText();
            String pass = registerPasswordField.getText();
            String cfpass = registerConfirmPasswordField.getText();
            if (!Objects.equals(pass, cfpass)) System.out.println("Password wrong");
            else {
                User us = new User(usn, pass, 1);
                UserManagement.addNormalUser(us);
                System.out.println("Register Success");
            }
        }
    }

    public void onBackButtonClick(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
    }
}
