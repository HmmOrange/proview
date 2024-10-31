package org.proview.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    public void setData(int id) throws FileNotFoundException, SQLException {
        Connection connection = AppMain.connection;
        String sql = "SELECT name, author, description FROM book WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        titleLabel.setText(resultSet.getString("name"));
        authorLabel.setText(resultSet.getString("author"));
        descriptionLabel.setText(resultSet.getString("description"));

        InputStream stream = new FileInputStream(String.format("./assets/covers/cover%d.png", id));
        Image image = new Image(stream);
        coverImage.setImage(image);
        coverImage.setFitWidth(112.5);
        coverImage.setPreserveRatio(true);
        coverImage.setSmooth(true);
        coverImage.setCache(true);

    }
    public void onBackButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("HomeView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }
}
