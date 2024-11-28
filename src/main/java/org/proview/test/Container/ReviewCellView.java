package org.proview.test.Container;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.kordamp.ikonli.javafx.FontIcon;
import org.proview.utils.SQLUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;

public class ReviewCellView {
    public Circle avatarCircle;
    public Label reviewerLabel;
    public Label reviewDateLabel;
    public Label reviewLabel;
    public HBox ratingHBox;

    public void setData(String avatarUrl, int userId, Timestamp timestamp, String review, int rating) throws IOException, SQLException {
        reviewerLabel.setText(Objects.requireNonNull(SQLUtils.getUser(userId)).getFullName());

        reviewDateLabel.setText(TimeAgo.using(timestamp.getTime()).replace("about ", ""));

        reviewLabel.setText(review);

        InputStream stream = new FileInputStream(avatarUrl);
        Image image = new Image(stream);
        avatarCircle.setFill(new ImagePattern(image));
        avatarCircle.setStroke(Color.WHITE);
        stream.close();

        // Set rating bar
        if (rating == 0)
            return;

        FontIcon[] starIconList = new FontIcon[5];
        for (int i = 0; i < 5; i++) {
            starIconList[i] = new FontIcon();
            if (i < rating) {
                starIconList[i].getStyleClass().setAll("ikonli-font-icon-rated");
            } else {
                starIconList[i].getStyleClass().setAll("ikonli-font-icon-default");
            }
            ratingHBox.getChildren().add(starIconList[i]);
        }
        ratingHBox.setId("star-rating-review");
    }
}
