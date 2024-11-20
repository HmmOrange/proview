package org.proview.modal.Review;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.proview.test.AppMain;
import org.proview.test.Container.ReviewCellView;
import org.proview.utils.SQLUtils;

import java.io.IOException;
import java.sql.*;
import java.util.Comparator;

public class ReviewManagement {
    public static ObservableList<Review> getReviewListWithBookId(int id) throws SQLException {
        ObservableList<Review> reviewObservableList = FXCollections.observableArrayList();

        Connection connection = AppMain.connection;

        String sql = "SELECT * FROM review WHERE book_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int bookId = resultSet.getInt("book_id");
            int userId = resultSet.getInt("user_id");
            String review = resultSet.getString("review");
            Timestamp timestampAdded = resultSet.getTimestamp("time_added");

            int rating = SQLUtils.getRating(userId, bookId);
            Review curReview = new Review(
                bookId,
                userId,
                review,
                rating,
                timestampAdded
            );
            reviewObservableList.add(curReview);
        }
        // Sort by time added descending
        // TODO: remove this and use sql order by.
        // TODO: move this to SQLUtils.
        reviewObservableList.sort(Comparator.comparingDouble(Review::getTimestampAddedLong).reversed());

        return reviewObservableList;
    }

    public static void initReviewList(VBox reviewListVBox, ObservableList<Review> reviewList) throws IOException, SQLException {
       reviewListVBox.getChildren().clear();
        for (var item: reviewList) {
           FXMLLoader loader = new FXMLLoader(AppMain.class.getResource("ReviewCellView.fxml"));
           VBox cell = loader.load();
           ReviewCellView controller = loader.getController();

           String avatarUrl = "./assets/avatars/user" + item.getUserId() + ".png";
           controller.setData(
                   avatarUrl,
                   item.getUserId(),
                   item.getTimestampAdded(),
                   item.getReview(),
                   item.getRating()
           );
           reviewListVBox.getChildren().add(cell);
       }
    }
}
