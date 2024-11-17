package org.proview.test.Container;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
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

    public void setData(String avatarUrl, int userId, Timestamp timestamp, String review) throws IOException, SQLException {
        reviewerLabel.setText(Objects.requireNonNull(SQLUtils.getUser(userId)).getFullName());

        String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(timestamp);
        reviewDateLabel.setText(date);

        reviewLabel.setText(review);

        InputStream stream = new FileInputStream(avatarUrl);
        Image image = new Image(stream);
        avatarCircle.setFill(new ImagePattern(image));
        avatarCircle.setStroke(Color.WHITE);
        stream.close();
    }
}
