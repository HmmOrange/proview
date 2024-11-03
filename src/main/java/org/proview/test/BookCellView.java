package org.proview.test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;

public class BookCellView {
    public ImageView coverImageView;
    public Label titleLabel;
    public Label tagLabel;
    public Label infoLabel;
    public Label copiesLabel;
    public Label authorLabel;
    private int id = -1;

    public void setData(String title, String authors, String imageUrl) throws IOException {
        titleLabel.setText(title);
        authorLabel.setText(authors);
        tagLabel.setText("Tags: ");
        infoLabel.setText(0 + " 🌟 " + 0 + " 👀 ");
        copiesLabel.setText("View in Google Books 🔗");

        InputStream stream = URI.create(imageUrl).toURL().openStream();
        Image image = new Image(stream);

        coverImageView.setImage(image);
        coverImageView.setFitWidth(112.5);
        coverImageView.setPreserveRatio(true);
        coverImageView.setSmooth(true);
        coverImageView.setCache(true);

        stream.close();
    }

    public void setData(int id, String title, String author, String tags, double rating, int issueCount, String imageUrl, int copiesAvailable) throws IOException {
        this.id = id;
        titleLabel.setText(title);
        authorLabel.setText(author);
        tagLabel.setText("Tags: " + tags);
        infoLabel.setText(rating + " 🌟 " + issueCount + " 👀 ");
        copiesLabel.setText(copiesAvailable + (copiesAvailable == 1 ? " copy" : " copies") + " available");

        InputStream stream = new FileInputStream(imageUrl);
        Image image = new Image(stream);

        coverImageView.setImage(image);
        coverImageView.setFitWidth(112.5);
        coverImageView.setPreserveRatio(true);
        coverImageView.setSmooth(true);
        coverImageView.setCache(true);

        stream.close();
    }

    public void onMouseClick(MouseEvent mouseEvent) throws IOException, SQLException {
        if (id < 0) { // this seems tricky, maybe there is a better way to handle this
            FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("BookInfo.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
            AppMain.window.setTitle("Hello!");
            AppMain.window.setScene(scene);
            AppMain.window.centerOnScreen();

            BookInfoView tempBookInfoView = fxmlLoader.getController();
            tempBookInfoView.setData(this.id);
        }
    }

}
