package org.proview.test.Scene;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import org.proview.modal.Book.BookManagement;
import org.proview.modal.User.NormalUser;
import org.proview.modal.User.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.proview.modal.User.UserManagement;
import org.proview.test.AppMain;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
    public ImageView avatarField;
    public Button addAvatarButton;
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField emailField;

    private File avatarFile;

    public void initialize() {
        avatarFile = new File("./assets/samples/defaultAvatar.png");
        if (avatarFile != null) {
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

    public void onConfirmButtonClick(ActionEvent actionEvent) throws IOException, SQLException {
        if (Objects.equals(registerUsernameField.getText(), "") ||
            Objects.equals(registerPasswordField.getText(), "") || Objects.equals(registerConfirmPasswordField.getText(), "") ||
            Objects.equals(firstNameField.getText(), "") || Objects.equals(lastNameField.getText(), "")
                || Objects.equals(emailField.getText(), "")) {
            registerResultLabel.setText("Please fill in all blanks!");
        }
        else {
            String usn = registerUsernameField.getText();
            String pass = registerPasswordField.getText();
            String cfpass = registerConfirmPasswordField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();

            /// check if username exists
            String sql = "SELECT username FROM user WHERE username = ?";
            Connection connection = AppMain.connection;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, usn);
            ResultSet resultSet = preparedStatement.executeQuery();
            ///

            if (!Objects.equals(pass, cfpass)) registerResultLabel.setText("Password is wrong");
            else if (resultSet.next()) registerResultLabel.setText("Username already exists!");
            else {
                ///get the latest id
                int latest_id = 0;
                String idSql = "SELECT MAX(id) AS latest_id FROM user";
                PreparedStatement idPS = connection.prepareStatement(idSql);
                ResultSet idRS = idPS.executeQuery();
                if(idRS.next()) {
                    latest_id = idRS.getInt("latest_id");
                } else System.out.println("Cannot find latest ID");
                ///

                ///add avatar to folder
                String dstFilePath = "./assets/avatars/user" + (latest_id + 1) + ".png";
                Files.copy(avatarFile.toPath(), (new File(dstFilePath)).toPath(), StandardCopyOption.REPLACE_EXISTING);
                ///

                User user = new NormalUser(latest_id + 1, usn, pass, firstName, lastName, email, false);
                UserManagement.addNormalUser(user);
                registerResultLabel.setText("Register Success");

                FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("LoginView.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
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
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }

    public void onKeyReleased(KeyEvent keyEvent) throws SQLException, IOException {
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
        if (avatarFile != null) {
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
}
