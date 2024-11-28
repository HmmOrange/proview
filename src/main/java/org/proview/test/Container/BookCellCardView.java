package org.proview.test.Container;

import javafx.event.ActionEvent;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.sql.SQLException;

public class BookCellCardView extends CellView {
    public ImageView coverImageView;
    public Label titleLabel;
    public HBox tagHBox;
    public Label ratingLabel;
    public Label issuesLabel;
    public Label copiesLabel;
    public Label authorLabel;
    public FontIcon starRatingIcon;
    public FontIcon issuesIcon;
    public HBox infoHBox;

    // For Google books
    @Override
    public void setData(String title, String authors, String imageUrl, String tags, String previewLink) throws SQLException, IOException {
        super.previewLink = previewLink;
        super.setData(
            titleLabel, title,
            authorLabel, authors,
            copiesLabel,
            infoHBox,
            imageUrl, coverImageView,
            83.333, 125,
            tags, tagHBox
        );
    }

    // For library books
    @Override
    public void setData(int id, String title, String author, double rating, int issueCount, int copiesAvailable) throws SQLException, IOException {
        super.id = id;
        super.setData(
            titleLabel, title,
            authorLabel, author,
            ratingLabel, rating,
            issuesLabel, issueCount,
            copiesLabel, copiesAvailable,
            coverImageView, id,
            83.333, 125,
            tagHBox
        );
    }

    public void onMouseClick(ActionEvent actionEvent) throws IOException, SQLException {
        super.onMouseClicked();
    }
}
