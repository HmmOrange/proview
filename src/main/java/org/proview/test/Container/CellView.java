package org.proview.test.Container;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;

import com.google.gson.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.proview.api.GoogleBooksAPI;
import org.proview.model.Tag.Tag;
import org.proview.model.Tag.TagManagement;
import org.proview.model.Tag.TagStyle;
import org.proview.test.AppMain;
import org.proview.test.Scene.BookInfoView;
import org.proview.utils.Utils;
import org.proview.utils.SQLUtils;

public abstract class CellView {
    protected String previewLink;
    protected int id = -1;

    public void setData(String title, String authors, String imageUrl, String tags, String previewLink) throws SQLException, IOException {}
    public void setData(int id, String title, String author, double rating, int issueCount, int copiesAvailable) throws SQLException, IOException {}

    // Lib book
    public void setData(
            Label titleLabel, String title,
            Label authorLabel, String author,
            Label ratingLabel, double rating,
            Label issuesLabel, int issueCount,
            Label copiesLabel, int copiesAvailable,
            ImageView coverImageView, int id,
            double targetWidth, double targetHeight,
            HBox tagHBox
    ) throws IOException, SQLException {
        titleLabel.setText(title);
        authorLabel.setText(author);
        ratingLabel.setText(String.format("%.2f", rating));
        issuesLabel.setText(String.valueOf(issueCount));

        if (copiesAvailable >= 0)
            copiesLabel.setText(copiesAvailable + (copiesAvailable == 1 ? " copy" : " copies") + " available");
        else {
            copiesLabel.setDisable(true);
            copiesLabel.setVisible(false);
        }

        Utils.insertBookImage(coverImageView, id, targetWidth, targetHeight);

        // Tags
        ObservableList<Tag> tagList = SQLUtils.getBookTags(id);
        if (tagList.size() > 3) {
            for (int i = 0; i < 3; i++) {
                tagHBox.getChildren().add(tagList.get(i).getLabel());
            }
            tagHBox.getChildren().add(new Tag("+" + (tagList.size() - 3)).getLabel());
        } else {
            for (Tag tag : tagList) {
                tagHBox.getChildren().add(tag.getLabel());
            }
        }
    }

    // Google book
    public void setData(
            Label titleLabel, String title,
            Label authorLabel, String authors,
            Label copiesLabel,
            HBox infoHBox,
            String imageUrl, ImageView coverImageView,
            double targetWidth, double targetHeight,
            String tags, HBox tagHBox

    ) throws IOException, SQLException {
        titleLabel.setText(title);
        authorLabel.setText(authors);
        copiesLabel.setText("View in Google Books 🔗");

        infoHBox.getChildren().clear();
        infoHBox.setPrefWidth(0);

        Utils.insertBookImage(coverImageView, imageUrl, targetWidth, targetHeight);

        // Tags
        String[] tagList = tags.replaceAll(" {2}", " ").replaceAll(", ", ",").split(",");
        Map<String, TagStyle> tagMap = TagManagement.getTagList();
        ObservableList<Tag> tagObservableList = FXCollections.observableArrayList();
        for (String tag : tagList) {
            if (tagMap.containsKey(tag)) {
                tagObservableList.add(new Tag(tag, tagMap.get(tag)));
            }
            else {
                tagObservableList.add(new Tag(tag));
            }
        }

        // System.out.println(tagObservableList.size());
        if (tagObservableList.size() > 3) {
            for (int i = 0; i < 3; i++) {
                tagHBox.getChildren().add(tagObservableList.get(i).getLabel());
            }
            tagHBox.getChildren().add(new Tag("+" + (tagObservableList.size() - 3)).getLabel());
        } else {
            for (Tag tag : tagObservableList) {
                tagHBox.getChildren().add(tag.getLabel());
            }
        }
    }

    public void onMouseClicked() throws IOException, SQLException {
        if (id >= 0) {
            FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("BookInfoView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
            AppMain.window.setTitle("Hello!");
            AppMain.window.setScene(scene);
            AppMain.window.centerOnScreen();

            BookInfoView tempBookInfoView = fxmlLoader.getController();
            tempBookInfoView.setData(id);
        } else {
            GoogleBooksAPI.openLink(previewLink);
        }
    }
}
