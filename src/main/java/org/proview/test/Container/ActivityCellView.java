package org.proview.test.Container;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.proview.model.Activity.Activity;
import org.proview.utils.SQLUtils;
import org.proview.test.AppMain;
import org.proview.test.Scene.BookInfoView;
import org.proview.utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;

// There surely is a way to create inheritance for ActivityCellView and PersonalActivityCellView, right...?

public class ActivityCellView extends CellView {
    public Label titleLabel;
    public Label descriptionLabel;
    public Label timeLabel;
    public ImageView coverImageView;
    public Label activityTypeLabel;
    public HBox descriptionHBox;

    public void setData(Activity.Type type, int userId, int bookId, String description, Timestamp timestamp) throws IOException, SQLException {
        super.id = bookId;
        titleLabel.setText(Objects.requireNonNull(SQLUtils.getUser(userId)).getFullName());
        if (type == Activity.Type.REVIEW) {
            activityTypeLabel.setText("Reviewed: " + Objects.requireNonNull(SQLUtils.getBook(bookId)).getTitle());
            descriptionLabel.setText(description);
        } else if (type == Activity.Type.RATING) {
            activityTypeLabel.setText("Rated: " + Objects.requireNonNull(Objects.requireNonNull(SQLUtils.getBook(bookId)).getTitle()));
            descriptionHBox.getChildren().clear();
            FontIcon[] starIconList = new FontIcon[5];
            for (int i = 0; i < 5; i++) {
                starIconList[i] = new FontIcon();
                if (i < Integer.parseInt(description)) {
                    starIconList[i].getStyleClass().setAll("ikonli-font-icon-rated");
                } else {
                    starIconList[i].getStyleClass().setAll("ikonli-font-icon-default");
                }
                descriptionHBox.getChildren().add(starIconList[i]);
            }
        }

        timeLabel.setText(TimeAgo.using(timestamp.getTime()).replace("about ", ""));
        Utils.insertBookImage(coverImageView, id, 50, 75);
    }

    public void onMouseClick(ActionEvent mouseEvent) throws IOException, SQLException {
        super.onMouseClicked();
    }
}
