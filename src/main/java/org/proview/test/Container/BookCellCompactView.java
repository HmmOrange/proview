package org.proview.test.Container;

import com.google.gson.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.apache.commons.lang3.StringEscapeUtils;
import org.proview.api.GoogleBooksAPI;
import org.proview.test.AppMain;
import org.proview.test.Scene.BookInfoView;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;

public class BookCellCompactView {
    public ImageView coverImageView;
    public Label titleLabel;
    public Label tagLabel;
    public Label infoLabel;
    public Label copiesLabel;
    public Label authorLabel;
    private int id = -1;

    public void setData(int id, String title, String author, String tags, double rating, int issueCount, int copiesAvailable) throws IOException {
        this.id = id;
        titleLabel.setText(title);
        authorLabel.setText(author);
        tagLabel.setText("Tags: " + tags);
        infoLabel.setText(rating + " 🌟 " + issueCount + " 👀 ");
        if (copiesAvailable > 0)
            copiesLabel.setText(copiesAvailable + (copiesAvailable == 1 ? " copy" : " copies") + " available");
        else {
            copiesLabel.setDisable(true);
            copiesLabel.setVisible(false);
        }

        String imageUrl = "./assets/covers/cover" + id + ".png";
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
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("BookInfoView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();

        BookInfoView tempBookInfoView = fxmlLoader.getController();
        tempBookInfoView.setData(this.id);
    }
}
