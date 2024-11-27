package org.proview.modal.Rating;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.proview.test.AppMain;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class RatingManagement {
    public static ObservableList<Rating> getRatingListFromUser(int userId) throws SQLException {
        ObservableList<Rating> ratingObservableList = FXCollections.observableArrayList();

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
        return ratingObservableList;
    }
}
