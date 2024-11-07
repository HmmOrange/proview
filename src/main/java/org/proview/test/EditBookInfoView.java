package org.proview.test;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.proview.model.BookManagement;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;

public class EditBookInfoView {
    public TextField titleField;
    public TextField authorField;
    public TextField tagField;
    public TextArea descriptionField;
    public TextField copiesField;
    public ImageView coverImage;
    public Button backButton;
    public Button confirmButton;
    public Button changeCoverButton;
    private int id;
    private File coverFile;

    public void initialize(int id) throws SQLException, IOException {
        this.id = id;
        Connection connection = AppMain.connection;
        String sql = "SELECT name, author, description, copies FROM book WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            titleField.setText(resultSet.getString("name"));
            authorField.setText(resultSet.getString("author"));
            descriptionField.setText(resultSet.getString("description"));
            copiesField.setText(resultSet.getString("copies"));
        }

        String tagSql = "SELECT tag FROM tag WHERE book_id = ?";
        PreparedStatement tagPreparedStatement = connection.prepareStatement(tagSql);
        tagPreparedStatement.setInt(1, id);
        ResultSet tagResultSet = tagPreparedStatement.executeQuery();
        if (tagResultSet.next()) {
            StringBuilder tagSB = new StringBuilder(tagResultSet.getString("tag"));
            while (tagResultSet.next()) tagSB.append(", ").append(tagResultSet.getString("tag"));
            tagField.setText(tagSB.toString());
        }

        InputStream stream = new FileInputStream(String.format("./assets/covers/cover%d.png", id));
        Image image = new Image(stream);
        coverImage.setImage(image);
        coverImage.setFitWidth(300);
        coverImage.setPreserveRatio(true);
        coverImage.setSmooth(true);
        coverImage.setCache(true);
        stream.close();

        coverFile = new File(String.format("./assets/covers/cover%d.png", id));
    }
    public void onChangeCoverButtonClick(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose book cover");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.gif")
        );
        coverFile = fileChooser.showOpenDialog(AppMain.window);
        if (coverFile != null) {
            Image image = new Image(coverFile.toURI().toString());
            if (image.isError()) {
                System.out.println("Error loading image: " + image.getException().getMessage());
            }
            coverImage.setImage(image);
            coverImage.setFitWidth(300);
            coverImage.setPreserveRatio(true);
            coverImage.setSmooth(true);
            coverImage.setCache(true);
        }
    }

    public void onConfirmButtonClick(ActionEvent actionEvent) throws SQLException, IOException {
        Connection connection = AppMain.connection;
        String sql = "UPDATE book SET name = ?, author = ?, description = ?, copies = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, titleField.getText());
        preparedStatement.setString(2, authorField.getText());
        preparedStatement.setString(3, descriptionField.getText());
        preparedStatement.setString(4, copiesField.getText());
        preparedStatement.setInt(5, id);
        preparedStatement.executeUpdate();

        String dstFilePath = String.format("./assets/covers/cover%d.png", id);
        // Store cover images in a folder (in practice this is stored in a CDN)
        Files.copy(coverFile.toPath(), (new File(dstFilePath)).toPath(), StandardCopyOption.REPLACE_EXISTING);

        //tag

        this.onBackButtonClick(actionEvent);
    }

    public void onBackButtonClick(ActionEvent actionEvent) throws SQLException, IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("BookInfo.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();

        BookInfoView tempBookInfoView = fxmlLoader.getController();
        tempBookInfoView.setData(this.id);
    }
}
