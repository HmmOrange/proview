package org.proview.test.Container;

import com.google.gson.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import org.apache.commons.lang3.StringEscapeUtils;
import org.proview.api.GoogleBooksAPI;
import org.proview.modal.Tag.TagManagement;
import org.proview.modal.Tag.TagStyle;
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
import java.util.Map;

public class BookCellCompactView {
    public ImageView coverImageView;
    public Label titleLabel;
    public HBox tagHBox;
    public Label infoLabel;
    public Label copiesLabel;
    public Label authorLabel;
    private int id = -1;

    public void setData(int id, String title, String author, String tags, double rating, int issueCount, int copiesAvailable) throws IOException, SQLException {
        this.id = id;
        titleLabel.setText(title);
        authorLabel.setText(author);
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
        double targetWidth = 46.875;
        double targetHeight = 75;
        double scaleX = targetWidth / image.getWidth();
        double scaleY = targetHeight / image.getHeight();
        double scale = Math.max(scaleX, scaleY);

        coverImageView.setImage(image);
        coverImageView.setFitWidth(image.getWidth() * scale);
        coverImageView.setFitHeight(image.getHeight() * scale);
        coverImageView.setPreserveRatio(true);
        Rectangle clip = new Rectangle(targetWidth, targetHeight);
        coverImageView.setClip(clip);
        coverImageView.setSmooth(true);
        coverImageView.setCache(true);

        stream.close();

        // Tags
        String[] tagList = tags.replace(" ", "").split(",");
        Map<String, TagStyle> tagMap = TagManagement.getTagList();

        for (String tag : tagList) {
            FXMLLoader loader = new FXMLLoader(AppMain.class.getResource("TagView.fxml"));
            Label tagLabel = loader.load();
            TagView tagView = loader.getController();

            String bgColorHex = null;
            String textColorHex = null;
            if (tagMap.containsKey(tag)) {
                bgColorHex = tagMap.get(tag).getBgColorHex();
                textColorHex = tagMap.get(tag).getTextColorHex();
            }

            tagView.setData(
                    tag,
                    bgColorHex,
                    textColorHex
            );
            tagHBox.getChildren().add(tagLabel);
        }
    }

    public void onMouseClick(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("BookInfoView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();

        BookInfoView tempBookInfoView = fxmlLoader.getController();
        tempBookInfoView.setData(this.id);
    }
}
