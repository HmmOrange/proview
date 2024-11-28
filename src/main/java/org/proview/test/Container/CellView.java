package org.proview.test.Container;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.proview.modal.Tag.Tag;
import org.proview.modal.Tag.TagManagement;
import org.proview.modal.Tag.TagStyle;
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
}
