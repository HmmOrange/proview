package org.proview.test.Scene;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import org.proview.model.User.UserManagement;
import org.proview.test.AppMain;
import org.proview.utils.PopUpWindowUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class EditProfileView {
    public Button changeAvatarButton;
    public Label editProfileResultLabel;
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField emailField;
    public TextField registerUsernameField;
    public TextField currentPasswordField;
    public TextField newPasswordField;
    public TextField confirmPasswordField;
    public Button confirmButton;
    public Button backButton;
    public ImageView avatarImageView;

    private File avatarFile;

    public void initialize() throws FileNotFoundException {
        avatarFile = new File("./assets/avatars/user" + UserManagement.getCurrentUser().getId() + ".png");
        try (InputStream inputStream = new FileInputStream(avatarFile)) {
            Image image = new Image(inputStream);
            if (image.isError()) {
                System.out.println("Error loading image: " + image.getException().getMessage());
            }
            avatarImageView.setImage(image);
            avatarImageView.setFitWidth(300);
            avatarImageView.setPreserveRatio(true);
            avatarImageView.setSmooth(true);
            avatarImageView.setCache(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        firstNameField.setText(UserManagement.getCurrentUser().getFirstName());
        lastNameField.setText(UserManagement.getCurrentUser().getLastName());
        emailField.setText(UserManagement.getCurrentUser().getEmail());
        registerUsernameField.setText(UserManagement.getCurrentUser().getUsername());
    }

    public void onConfirmButtonClick(ActionEvent actionEvent) throws SQLException, IOException {
        if (Objects.equals(currentPasswordField.getText(), "")) {
            editProfileResultLabel.setText("Please enter current password to confirm changes");
        } else {
            Connection connection = AppMain.connection;
            String sql = "SELECT password FROM user WHERE id = ?";
            PreparedStatement passwordPS = connection.prepareStatement(sql);
            passwordPS.setInt(1, UserManagement.getCurrentUser().getId());
            ResultSet passwordRS = passwordPS.executeQuery();
            if (passwordRS.next()) {
                if (!Objects.equals(currentPasswordField.getText(), passwordRS.getString("password"))) {
                    editProfileResultLabel.setText("Current password incorrect");
                } else {
                    if (PopUpWindowUtils.showConfirmation("Warning", "Are you sure to confirm those changes?")) {
                        String editProfileSql = "UPDATE user SET firstname = ?, lastname = ?, email = ?, username = ? WHERE id = ?";
                        PreparedStatement editProfilePS = connection.prepareStatement(editProfileSql);
                        editProfilePS.setString(1, firstNameField.getText());
                        editProfilePS.setString(2, lastNameField.getText());
                        editProfilePS.setString(3, emailField.getText());
                        editProfilePS.setString(4, registerUsernameField.getText());
                        editProfilePS.setInt(5, UserManagement.getCurrentUser().getId());
                        editProfilePS.executeUpdate();
                        UserManagement.getCurrentUser().setFirstName(firstNameField.getText());
                        UserManagement.getCurrentUser().setLastName(lastNameField.getText());
                        UserManagement.getCurrentUser().setEmail(emailField.getText());
                        UserManagement.getCurrentUser().setUsername(registerUsernameField.getText());

                        String dstFilePath = "./assets/avatars/user" + UserManagement.getCurrentUser().getId() + ".png";
                        Files.copy(avatarFile.toPath(), (new File(dstFilePath)).toPath(), StandardCopyOption.REPLACE_EXISTING);

                        PopUpWindowUtils.showNotification("Done!", "Changes confirmed", Alert.AlertType.INFORMATION);
                        onBackButtonClick(actionEvent);
                    }
                }
            }
        }
    }

    public void onBackButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("ProfileView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }


    public void onChangeAvatarButtonClick(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose book cover");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.gif")
        );
        avatarFile = fileChooser.showOpenDialog(AppMain.window);
        try (InputStream inputStream = new FileInputStream(avatarFile)) {
            Image image = new Image(inputStream);
            avatarImageView.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
            editProfileResultLabel.setText("Failed to load image.");
        }

    }

    public void onKeyReleased(KeyEvent keyEvent) throws SQLException, IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            onConfirmButtonClick(new ActionEvent());
        }
    }
}
