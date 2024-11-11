package org.proview.test.Scene;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.proview.modal.User.UserManagement;

import java.io.*;
import java.sql.SQLException;

public class ProfileView {
    public Label usernameField;
    public Label emailField;
    public ImageView avatarImageView;

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
    }
}
