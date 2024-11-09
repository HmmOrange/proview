package org.proview.test;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.proview.model.UserManagement;

public class ProfileView {
    public Label usernameField;
    public Label emailField;
    public ImageView avatarImageView;

    public void initialize() {
        usernameField.setText(UserManagement.getCurrentUser().getUsername());
        emailField.setText(UserManagement.getCurrentUser().getEmail());
    }
}
