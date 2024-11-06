package org.proview.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.proview.model.UserManagement;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookInfoView {
    public Button backButton;
    public ImageView coverImage;
    public Label titleLabel;
    public Label authorLabel;
    public Label tagLabel;
    public Label descriptionLabel;
    public Button editButton;
    public Label copiesLabel;

    private int ID;

    public void initialize() {
        if(UserManagement.getCurrentUser().getType() == 1) {
            editButton.setText("Borrow");
            editButton.setOnAction(actionEvent -> {
                FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("CreateIssueView.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 500, 500);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                AppMain.window.setTitle("Hello!");
                AppMain.window.setScene(scene);
                AppMain.window.centerOnScreen();
            });
        }
    }

    public void setData(int id) throws IOException, SQLException {
        ID = id;
        Connection connection = AppMain.connection;
        String sql = "SELECT name, author, description, copies FROM book WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        titleLabel.setText(resultSet.getString("name"));
        authorLabel.setText(resultSet.getString("author"));
        descriptionLabel.setText(resultSet.getString("description"));
        copiesLabel.setText("Number of copies available: " + resultSet.getString("copies"));

        String tagSql = "SELECT tag FROM tag WHERE book_id = ?";
        PreparedStatement tagPreparedStatement = connection.prepareStatement(tagSql);
        tagPreparedStatement.setInt(1, id);
        ResultSet tagResultSet = tagPreparedStatement.executeQuery();
        if (tagResultSet.next()) {
            StringBuilder tagSB = new StringBuilder(tagResultSet.getString("tag"));
            while (tagResultSet.next()) tagSB.append(", ").append(tagResultSet.getString("tag"));
            tagLabel.setText(tagSB.toString());
        }

        InputStream stream = new FileInputStream(String.format("./assets/covers/cover%d.png", id));
        Image image = new Image(stream);
        coverImage.setImage(image);
        coverImage.setFitWidth(1000);
        coverImage.setPreserveRatio(true);
        coverImage.setSmooth(true);
        coverImage.setCache(true);
        stream.close();
    }
    public void onBackButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("HomeView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }

    public void onBorrowButtonClick(ActionEvent actionEvent) {
    }

    public void onEditButtonClick(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("EditBookInfoView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        EditBookInfoView thisEditBookInfoView = fxmlLoader.getController();
        thisEditBookInfoView.initialize(ID);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }
}
