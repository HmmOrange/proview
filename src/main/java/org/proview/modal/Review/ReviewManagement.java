package org.proview.modal.Review;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import org.proview.test.AppMain;
import org.proview.test.Container.ReviewCellView;

import java.sql.*;
import java.util.Comparator;

public class ReviewManagement {
    public static ObservableList<Review> getReviewListWithBookId(int id) throws SQLException {
        ObservableList<Review> reviewObservableList = FXCollections.observableArrayList();

        Connection connection = AppMain.connection;
        Statement statement = connection.createStatement();

        String sql = "SELECT * FROM review WHERE book_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int book_id = resultSet.getInt("book_id");
            int user_id = resultSet.getInt("user_id");
            String review = resultSet.getString("review");
            Timestamp timestampAdded = resultSet.getTimestamp("time_added");

            Review curReview = new Review(
                book_id,
                user_id,
                review,
                timestampAdded
            );
            reviewObservableList.add(curReview);
        }
        // Sort by time added descending
        reviewObservableList.sort(Comparator.comparingDouble(Review::getTimestampAddedLong).reversed());

        return reviewObservableList;
    }

    public static void initReviewList(ListView<Review> reviewListView, ObservableList<Review> reviewList) {
        reviewListView.setItems(reviewList);
        reviewListView.setCellFactory(param -> new ListCell<>() {
            {
                setStyle("-fx-padding: 0px; -fx-margin: 0px; -fx-background-insets: 0px; -fx-border-insets: 0px;");
            }

            @Override
            protected void updateItem(Review item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(AppMain.class.getResource("ReviewCellView.fxml"));
                        HBox hbox = loader.load();

                        // Get the controller of the cell
                        ReviewCellView cellView = loader.getController();

                        String avatarUrl = "./assets/avatars/user" + item.getUserId() + ".png";
                        cellView.setData(
                                avatarUrl,
                                item.getUserId(),
                                item.getTimestampAdded(),
                                item.getReview()
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
}
