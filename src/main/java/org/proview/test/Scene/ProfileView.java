package org.proview.test.Scene;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.proview.modal.User.Admin;
import org.proview.modal.User.NormalUser;
import org.proview.modal.User.UserManagement;
import org.proview.test.AppMain;

import java.io.*;
import java.sql.SQLException;

public class ProfileView {
    public Label usernameField;
    public Label emailField;
    public ImageView avatarImageView;
    public Button editProfileButton;

    public void initialize() throws SQLException, IOException {
        usernameField.setText(UserManagement.getCurrentUser().getUsername());
        emailField.setText(UserManagement.getCurrentUser().getEmail());

        InputStream stream = new FileInputStream("./assets/avatars/user" + UserManagement.getCurrentUser().getId() + ".png");
        Image image = new Image(stream);
        avatarImageView.setImage(image);
        avatarImageView.setFitWidth(100);
        avatarImageView.setPreserveRatio(true);
        avatarImageView.setSmooth(true);
        avatarImageView.setCache(true);

        if (UserManagement.getCurrentUser() instanceof Admin) {
            editProfileButton.setDisable(true);
            editProfileButton.setVisible(false);
        }
    }

    public void onEditProfileButtonClick(ActionEvent actionEvent) throws IOException {
        if (UserManagement.getCurrentUser() instanceof NormalUser) {
            FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("EditProfileView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
            AppMain.window.setTitle("Hello!");
            AppMain.window.setScene(scene);
            AppMain.window.centerOnScreen();
        }
    }
}
