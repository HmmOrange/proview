package org.proview.test.Container;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Map;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.proview.modal.Tag.Tag;
import org.proview.modal.Tag.TagManagement;
import org.proview.modal.Tag.TagStyle;
import org.proview.test.AppMain;
import org.proview.utils.Utils;
import org.proview.utils.SQLUtils;

public abstract class CellView {
    public abstract void setData(String title, String authors, String imageUrl, String tags) throws IOException, SQLException;
    public abstract void setData(int id, String title, String author, double rating, int issueCount, int copiesAvailable) throws IOException, SQLException;

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

        Utils.insertImage(coverImageView, id, targetWidth, targetHeight);

        // Tags
        ObservableList<Tag> tagList = SQLUtils.getBookTags(id);
        for (Tag tag : tagList) {
            tagHBox.getChildren().add(tag.getLabel());
        }
    }

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

        Utils.insertImage(coverImageView, imageUrl, targetWidth, targetHeight);

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
}
