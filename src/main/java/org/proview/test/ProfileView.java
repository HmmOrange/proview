package org.proview.test;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.proview.model.UserManagement;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileView {
    public Label usernameField;
    public Label emailField;
    public ImageView avatarImageView;

    public void initialize() throws SQLException, IOException {
        usernameField.setText(UserManagement.getCurrentUser().getUsername());
        emailField.setText(UserManagement.getCurrentUser().getEmail());

        Connection connection = AppMain.connection;
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT avatar FROM user WHERE username = ?");
        preparedStatement.setString(1, usernameField.getText());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            InputStream inputStream = resultSet.getBinaryStream("avatar");
            String tempOutputFilePath = "./assets/avatars/tempAvatar.png";
            try (FileOutputStream outputStream = new FileOutputStream(tempOutputFilePath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                System.out.println("File saved to " + tempOutputFilePath);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Image avatar = new Image("file:" + tempOutputFilePath);
            avatarImageView.setImage(avatar);
            avatarImageView.setFitWidth(100);
            avatarImageView.setPreserveRatio(true);
            avatarImageView.setSmooth(true);
            avatarImageView.setCache(true);
            inputStream.close();
            File file = new File(tempOutputFilePath);
            file.delete();
        }
    }
}
