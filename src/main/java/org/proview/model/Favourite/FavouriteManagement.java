package org.proview.model.Favourite;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.proview.test.AppMain;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class FavouriteManagement {
    public static ObservableList<Favourite> getFavouriteListOfUser(int userId) throws SQLException {
        ObservableList<Favourite> favouriteObservableList = FXCollections.observableArrayList();
        String sql = """
                SELECT * FROM favourite
                WHERE user_id = ?;
                """;
        PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int bookId = resultSet.getInt("book_id");
            Timestamp time = resultSet.getTimestamp("time_added");
            favouriteObservableList.add(new Favourite(userId, bookId, time));
        }
        return favouriteObservableList;
    }
}
