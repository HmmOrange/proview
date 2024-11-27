package org.proview.test.Scene;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import org.proview.modal.User.NormalUser;
import org.proview.modal.User.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.proview.modal.User.UserManagement;
import org.proview.test.AppMain;
import org.proview.utils.SQLUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
    public Label errorLabel;
    public ImageView avatarField;
    public Button addAvatarButton;
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField emailField;
    public Label successLabel;

    private File avatarFile;

    public void initialize() {
        avatarFile = new File("./assets/samples/defaultAvatar.png");
        Image image = new Image(avatarFile.toURI().toString());
        if (image.isError()) {
            System.out.println("Error loading image: " + image.getException().getMessage());
        }
        avatarField.setImage(image);
        avatarField.setFitWidth(300);
        avatarField.setPreserveRatio(true);
        avatarField.setSmooth(true);
        avatarField.setCache(true);

        successLabel.setText("");
        errorLabel.setText("");
    }

    public void onConfirmButtonClick(ActionEvent actionEvent) {
        try {
            if (registerUsernameField.getText().isEmpty()
                    || registerPasswordField.getText().isEmpty()
                    || registerConfirmPasswordField.getText().isEmpty()
                    || firstNameField.getText().isEmpty()
                    || lastNameField.getText().isEmpty()
                    || emailField.getText().isEmpty()) {
                throw new IllegalArgumentException("Username and password must not be empty.");
            }

            String usn = registerUsernameField.getText();
            String pass = registerPasswordField.getText();
            String cfpass = registerConfirmPasswordField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();


            if (!Objects.equals(pass, cfpass))
                throw new IllegalArgumentException("Confirm passwords do not match.");

            if (!usn.matches("^[a-zA-Z0-9_]+$")) {
                throw new IllegalArgumentException("Username can only contain letters, numbers, and underscores.");
            }

            if (pass.length() < 8)
                    //!pass.matches(".*[A-Z].*") ||
                    //!pass.matches(".*[a-z].*") ||
                    //!pass.matches(".*\\d.*") ||
                    //!pass.matches(".*[!@#$%^&*()-_=+{};:,<.>].*")) {
                throw new IllegalArgumentException("Password must be at least 8 characters long.");

            if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                throw new IllegalArgumentException("Invalid email format.");
            }

            // check if username exists
            if (SQLUtils.checkUser(usn))
                throw new IllegalArgumentException("Username already exists.");

            int latest_id = SQLUtils.getLatestID();
            if (latest_id < 0)
                throw new Exception("Failed to find latest ID");

            // Add avatar to folder
            String dstFilePath = "./assets/avatars/user" + (latest_id + 1) + ".png";
            try {
                Files.copy(avatarFile.toPath(), (new File(dstFilePath)).toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ioException) {
                throw new Exception("Failed to save avatar file: " + ioException.getMessage());
            }

            User user = new NormalUser(latest_id + 1, usn, pass, firstName, lastName, email, false);
            UserManagement.addNormalUser(user);

            FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("LoginView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
            LoginView tempLoginView = fxmlLoader.getController();
            tempLoginView.subHeaderLabel.setText("Register Success. Please log in!");
            tempLoginView.subHeaderLabel.setStyle("-fx-text-fill: green;");
            AppMain.window.setTitle("Hello!");
            AppMain.window.setScene(scene);
            AppMain.window.centerOnScreen();
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
        } catch (Exception e) {
            errorLabel.setText("An unexpected error occurred: " + e.getMessage());
        }
    }

    public void onBackButtonClick(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }

    public void onKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            onConfirmButtonClick(new ActionEvent());
        }
    }

    public void onAddAvatarButtonClick(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose book cover");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.gif")
        );
        avatarFile = fileChooser.showOpenDialog(AppMain.window);
        if (avatarFile == null) {
            avatarFile = new File("./assets/samples/defaultAvatar.png");
        }

        Image image = new Image(avatarFile.toURI().toString());
        if (image.isError()) {
            System.out.println("Error loading image: " + image.getException().getMessage());
        }
        avatarField.setImage(image);
        avatarField.setFitWidth(300);
        avatarField.setPreserveRatio(true);
        avatarField.setSmooth(true);
        avatarField.setCache(true);
    }
}
