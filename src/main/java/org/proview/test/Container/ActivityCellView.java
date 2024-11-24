package org.proview.test.Container;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import org.proview.modal.Activity.Activity;
import org.proview.utils.SQLUtils;
import org.proview.test.AppMain;
import org.proview.test.Scene.BookInfoView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;

// There surely is a way to create inheritance for ActivityCellView and PersonalActivityCellView, right...?

public class ActivityCellView {
    public Label titleLabel;
    public Label descriptionLabel;
    public Label timeLabel;
    public ImageView coverImageView;
    public Label activityTypeLabel;
    private int id = -1;

    public void setData(Activity.Type type, int userId, int bookId, String description, Timestamp timestamp) throws IOException, SQLException {
        titleLabel.setText(Objects.requireNonNull(SQLUtils.getUser(userId)).getFullName());
        if (type == Activity.Type.REVIEW)
            activityTypeLabel.setText("Reviewed: " + Objects.requireNonNull(SQLUtils.getBook(bookId)).getTitle());
        descriptionLabel.setText(description);
        timeLabel.setText(TimeAgo.using(timestamp.getTime()).replace("about ", ""));

        this.id = bookId;

        String imageUrl = "./assets/covers/cover" + id + ".png";
        InputStream stream = new FileInputStream(imageUrl);
        Image image = new Image(stream);

        coverImageView.setImage(image);
        double targetWidth = 60 ;
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
    }

    public void onMouseClick(ActionEvent mouseEvent) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("BookInfoView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();

        BookInfoView tempBookInfoView = fxmlLoader.getController();
        tempBookInfoView.setData(this.id);
    }

}
