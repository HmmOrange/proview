package org.proview.model.Rating;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.proview.test.AppMain;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class RatingManagement {
    public static ObservableList<Rating> getRatingListFromUser(int userId) throws SQLException {
//        userId >= 0 return current user list, userId = -1 return all users list
        ObservableList<Rating> ratingObservableList = FXCollections.observableArrayList();

        if (userId >= 0) {
            String sql = """
                    SELECT * FROM rating
                    WHERE user_id = ?
                    """;
            PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int bookId = resultSet.getInt("book_id");
                int star = resultSet.getInt("rating");
                Timestamp timestamp = resultSet.getTimestamp("time_added");
                ratingObservableList.add(new Rating(userId, bookId, star, timestamp));
            }
        } else if (userId == -1) {
            String sql = "SELECT * FROM rating";
            ResultSet resultSet = AppMain.connection.prepareStatement(sql).executeQuery();
            while (resultSet.next()) {
                int realUserId = resultSet.getInt("user_id");
                int bookId = resultSet.getInt("book_id");
                int star = resultSet.getInt("rating");
                Timestamp timestamp = resultSet.getTimestamp("time_added");
                ratingObservableList.add(new Rating(realUserId, bookId, star, timestamp));
            }
        }
        return ratingObservableList;
    }
}
