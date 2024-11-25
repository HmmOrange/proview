package org.proview.test.Container;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.javafx.FontIcon;
import org.proview.modal.Tag.TagManagement;
import org.proview.modal.Tag.TagStyle;
import org.proview.test.AppMain;
import org.proview.test.Scene.BookInfoView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.SQLException;
import java.util.Map;

public class BookCellCompactView extends CellView {
    public ImageView coverImageView;
    public Label titleLabel;
    public HBox tagHBox;
    public Label copiesLabel;
    public Label authorLabel;
    public Label issuesLabel;
    public Label ratingLabel;
    public FontIcon issuesIcon;
    public FontIcon starRatingIcon;
    public HBox infoHBox;
    private int id = -1;

    @Override
    public void setData(String title, String authors, String imageUrl, String tags) throws IOException, SQLException {
        titleLabel.setText(title);
        authorLabel.setText(authors.replaceAll(",", ", "));
        copiesLabel.setText("View in Google Books 🔗");

        infoHBox.getChildren().clear();
        infoHBox.setPrefWidth(0);

        // System.out.println(imageUrl);
        InputStream stream = URI.create(imageUrl).toURL().openStream();
        Image image = new Image(stream);

        coverImageView.setImage(image);

        double targetWidth = 50;
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

        // Tag
        String[] tagList = tags.replaceAll(" {2}", " ").replaceAll(", ", ",").split(",");
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

    @Override
    public void setData(int id, String title, String author, String tags, double rating, int issueCount, int copiesAvailable) throws IOException, SQLException {
        this.id = id;
        titleLabel.setText(title);
        authorLabel.setText(author);
        ratingLabel.setText(String.format("%.2f", rating));
        issuesLabel.setText(String.valueOf(issueCount));

        copiesLabel.setText(copiesAvailable + (copiesAvailable == 1 ? " copy" : " copies") + " available");


        String imageUrl = "./assets/covers/cover" + id + ".png";
        InputStream stream = new FileInputStream(imageUrl);
        Image image = new Image(stream);

        coverImageView.setImage(image);
        double targetWidth = 50;
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
        String[] tagList = tags.replaceAll(" {2}", " ").replaceAll(", ", ",").split(",");
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
