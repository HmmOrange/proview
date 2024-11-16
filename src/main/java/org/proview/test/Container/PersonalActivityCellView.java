package org.proview.test.Container;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.proview.modal.Activity.Activity;
import org.proview.modal.Utils.SQLUtils;
import org.proview.test.AppMain;
import org.proview.test.Scene.BookInfoView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;

public class PersonalActivityCellView {
    public Label descriptionLabel;
    public Label timeLabel;
    public ImageView coverImageView;
    public Label activityTypeLabel;
    private int id = -1;

    public void setData(Activity.Type type, int bookId, String description, Timestamp timestamp) throws IOException, SQLException {
        switch (type) {
            case Activity.Type.REVIEW:
                activityTypeLabel.setText("Reviewed: " + Objects.requireNonNull(SQLUtils.getBookFromId(bookId)).getTitle());
        }

        descriptionLabel.setText(description);
        timeLabel.setText(TimeAgo.using(timestamp.getTime()).replace("about ", ""));

        this.id = bookId;

        String imageUrl = "./assets/covers/cover" + id + ".png";
        InputStream stream = new FileInputStream(imageUrl);
        Image image = new Image(stream);

        coverImageView.setImage(image);
        coverImageView.setFitWidth(62.5);
        coverImageView.setPreserveRatio(true);
        coverImageView.setSmooth(true);
        coverImageView.setCache(true);

        stream.close();
    }

    public void onMouseClick(MouseEvent mouseEvent) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("BookInfoView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();

        BookInfoView tempBookInfoView = fxmlLoader.getController();
        tempBookInfoView.setData(this.id);
    }

}
