package org.proview.modal.Activity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.proview.modal.Issue.Issue;
import org.proview.modal.Issue.IssueManagement;
import org.proview.modal.Review.Review;
import org.proview.utils.SQLUtils;
import org.proview.test.AppMain;
import org.proview.test.Container.ActivityCellView;
import org.proview.test.Container.PersonalActivityCellView;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ActivityManagement {
    public static void initActivityList(VBox activityListVBox, ObservableList<Activity> activityList) {
        activityListVBox.getChildren().clear();
        int index = 0;
        for (var item: activityList) {
            index++;
            try {
                FXMLLoader loader = new FXMLLoader(AppMain.class.getResource("ActivityCellView.fxml"));
                Button button = loader.load();

                // Get the controller of the cell
                ActivityCellView cellView = loader.getController();
                cellView.setData(
                        item.getType(),
                        item.getUserId(),
                        item.getBookId(),
                        item.getDescription(),
                        item.getTimestampAdded()
                );
                activityListVBox.getChildren().add(button);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void initPersonalActivityList(VBox activityListVBox, ObservableList<Activity> activityList) {
        activityListVBox.getChildren().clear();
        int index = 0;
        for (var item: activityList) {
            index++;
            try {
                FXMLLoader loader = new FXMLLoader(AppMain.class.getResource("PersonalActivityCellView.fxml"));
                Button button = loader.load();

                // Get the controller of the cell
                PersonalActivityCellView cellView = loader.getController();
                cellView.setData(
                        item.getType(),
                        item.getBookId(),
                        item.getDescription(),
                        item.getTimestampAdded()
                );
                activityListVBox.getChildren().add(button);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
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

    public static ObservableList<Activity> getAllActivityList(int userId) throws SQLException {
        ObservableList<Activity> activityObservableList = FXCollections.observableArrayList();
        // Get all Activity.Type.REVIEW
        ObservableList<Review> reviewObservableList = SQLUtils.getReviewList(userId);
        for (var i : reviewObservableList) {
            int bookId = i.getBookId();
            String description = i.getReview();
            Timestamp timestamp = i.getTimestampAdded();
            activityObservableList.add(new Activity(bookId, userId, description, timestamp, Activity.Type.REVIEW));
        }
        // Get Activity.Type.ISSUE_START and Activity.Type.ISSUE_END
        ObservableList<Issue> issueObservableList = IssueManagement.getIssueListFrom(userId);
        for (var i : issueObservableList) {
            int bookId = i.getBookId();
            String description = "";
            Timestamp start_time = i.getStart_time();
            activityObservableList.add(new Activity(bookId, userId, description, start_time, Activity.Type.ISSUE_START));
            if (i.getStatus().equals("Returned")) {
                Timestamp end_time = i.getEnd_time();
                activityObservableList.add(new Activity(bookId, userId, description, end_time, Activity.Type.ISSUE_END));
            }
        }
        return activityObservableList;
    }
}
