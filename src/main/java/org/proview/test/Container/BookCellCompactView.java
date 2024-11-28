package org.proview.test.Container;

import com.google.gson.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.apache.commons.lang3.StringEscapeUtils;
import org.kordamp.ikonli.javafx.FontIcon;
import org.proview.api.GoogleBooksAPI;
import org.proview.test.AppMain;
import org.proview.test.Scene.BookInfoView;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;

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

    @Override
    public void setData(String title, String authors, String imageUrl, String tags, String previewLink) throws IOException, SQLException {
        super.previewLink = previewLink;
        super.setData(
                titleLabel, title,
                authorLabel, authors,
                copiesLabel,
                infoHBox,
                imageUrl, coverImageView,
                60, 90,
                tags, tagHBox
        );
    }

    @Override
    public void setData(int id, String title, String author, double rating, int issueCount, int copiesAvailable) throws IOException, SQLException {
        super.id = id;
        super.setData(
                titleLabel, title,
                authorLabel, author,
                ratingLabel, rating,
                issuesLabel, issueCount,
                copiesLabel, copiesAvailable,
                coverImageView, id,
                60, 90,
                tagHBox
        );
    }

    public void onMouseClick(ActionEvent actionEvent) throws IOException, SQLException {
        super.onMouseClicked();
    }
}
