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

public class PersonalActivityCellView extends CellView {
    public Label descriptionLabel;
    public Label timeLabel;
    public ImageView coverImageView;
    public Label activityTypeLabel;
    public HBox descriptionHBox;
    private int id = -1;

    public void setData(Activity.Type type, int bookId, String description, Timestamp timestamp) throws IOException, SQLException {
        switch (type) {
            case Activity.Type.REVIEW:
                activityTypeLabel.setText("Reviewed: " + Objects.requireNonNull(SQLUtils.getBook(bookId)).getTitle());
                break;
            case Activity.Type.ISSUE_START:
                activityTypeLabel.setText("Borrowed: " + Objects.requireNonNull(SQLUtils.getBook(bookId)).getTitle());
                break;
            case Activity.Type.ISSUE_END:
                activityTypeLabel.setText("Returned: " + Objects.requireNonNull(SQLUtils.getBook(bookId).getTitle()));
                break;
            case Activity.Type.OVERDUE:
                activityTypeLabel.setText("Overdue: " + Objects.requireNonNull(SQLUtils.getBook(bookId).getTitle()));
                activityTypeLabel.setStyle("-fx-text-fill: red;");
                break;
            case Activity.Type.WARNING:
                activityTypeLabel.setText("About to due: " + Objects.requireNonNull(SQLUtils.getBook(bookId).getTitle()));
                activityTypeLabel.setStyle("-fx-text-fill: red;");
                break;
            case Activity.Type.FAVOURITE:
                activityTypeLabel.setText("Favourited: " + Objects.requireNonNull(SQLUtils.getBook(bookId).getTitle()));
                break;
            case Activity.Type.RATING:
                activityTypeLabel.setText("Rated: " + Objects.requireNonNull(SQLUtils.getBook(bookId).getTitle()));
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
                break;
        }

        if (type != Activity.Type.RATING)
            descriptionLabel.setText(description);
        timeLabel.setText(TimeAgo.using(timestamp.getTime()).replace("about ", ""));

        this.id = bookId;

        Utils.insertBookImage(coverImageView, id, 50, 75);
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
