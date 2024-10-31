package org.proview.test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class BookCellView {
    public ImageView coverImageView;
    public Label titleLabel;
    public Label tagLabel;
    public Label infoLabel;
    public Label copiesLabel;
    public Label authorLabel;
    private int id;

    public void setData(int id, String title, String author, String tags, double rating, int issueCount, String imagePath, int copiesAvailable) throws FileNotFoundException {
        this.id = id;
        titleLabel.setText(title);
        authorLabel.setText(author);
        tagLabel.setText("Tags: " + tags);
        infoLabel.setText(rating + " 🌟 " + issueCount + " 👀 ");
        copiesLabel.setText(copiesAvailable + (copiesAvailable == 1 ? " copy" : " copies") + " available");

        InputStream stream = new FileInputStream(imagePath);
        Image image = new Image(stream);
        coverImageView.setImage(image);
        coverImageView.setFitWidth(112.5);
        coverImageView.setPreserveRatio(true);
        coverImageView.setSmooth(true);
        coverImageView.setCache(true);
    }

    public void onMouseClick(MouseEvent mouseEvent) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("BookInfo.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();

        BookInfoView tempBookInfoView = fxmlLoader.getController();
        tempBookInfoView.setData(this.id);
    }

}
