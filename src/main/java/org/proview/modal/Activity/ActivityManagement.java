package org.proview.modal.Activity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import org.proview.modal.Review.Review;
import org.proview.modal.User.UserManagement;
import org.proview.modal.Utils.SQLUtils;
import org.proview.test.AppMain;
import org.proview.test.Container.ActivityCellView;
import org.proview.test.Container.PersonalActivityCellView;
import org.proview.test.Container.ReviewCellView;

import java.sql.SQLException;
import java.sql.Timestamp;

public class ActivityManagement {
    public static void initActivityList(ListView<Activity> activityListView, ObservableList<Activity> activityList) {
        activityListView.setItems(activityList);
        activityListView.setCellFactory(param -> new ListCell<>() {
            {
                setStyle("-fx-padding: 0px; -fx-margin: 0px; -fx-background-insets: 0px; -fx-border-insets: 0px;");
            }

            @Override
            protected void updateItem(Activity item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(AppMain.class.getResource("ActivityCellView.fxml"));
                        HBox hbox = loader.load();

                        // Get the controller of the cell
                        ActivityCellView cellView = loader.getController();
                        cellView.setData(
                                item.getType(),
                                item.getUserId(),
                                item.getBookId(),
                                item.getDescription(),
                                item.getTimestampAdded()
                        );

                        setGraphic(hbox);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public static void initPersonalActivityList(ListView<Activity> activityListView, ObservableList<Activity> activityList) {
        activityListView.setItems(activityList);
        activityListView.setCellFactory(param -> new ListCell<>() {
            {
                setStyle("-fx-padding: 0px; -fx-margin: 0px; -fx-background-insets: 0px; -fx-border-insets: 0px;");
            }

            @Override
            protected void updateItem(Activity item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(AppMain.class.getResource("PersonalActivityCellView.fxml"));
                        HBox hbox = loader.load();

                        // Get the controller of the cell
                        PersonalActivityCellView cellView = loader.getController();
                        cellView.setData(
                                item.getType(),
                                item.getBookId(),
                                item.getDescription(),
                                item.getTimestampAdded()
                        );

                        setGraphic(hbox);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public static ObservableList<Activity> getReviewActivityList() throws SQLException {
        ObservableList<Review> reviewObservableList = SQLUtils.getReviewList();
        ObservableList<Activity> activityObservableList = FXCollections.observableArrayList();
        for (var i : reviewObservableList) {
            int user_id = i.getUserId();
            int book_id = i.getBookId();
            String description = i.getReview();
            Timestamp timestamp = i.getTimestampAdded();
            activityObservableList.add(new Activity(book_id, user_id, description, timestamp, Activity.Type.REVIEW));
        }
        return activityObservableList;
    }

    public static ObservableList<Activity> getAllActivityListFromUserId(int userId) throws SQLException {
        // Get all Activity.Type.REVIEW
        ObservableList<Review> reviewObservableList = SQLUtils.getReviewListFromUserId(userId);
        ObservableList<Activity> activityObservableList = FXCollections.observableArrayList();
        for (var i : reviewObservableList) {
            int user_id = i.getUserId();
            int book_id = i.getBookId();
            String description = i.getReview();
            Timestamp timestamp = i.getTimestampAdded();
            activityObservableList.add(new Activity(book_id, user_id, description, timestamp, Activity.Type.REVIEW));
        }
        return activityObservableList;
    }
}
