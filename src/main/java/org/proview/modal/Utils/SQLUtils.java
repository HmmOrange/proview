package org.proview.modal.Utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.proview.modal.Review.Review;
import org.proview.modal.User.Admin;
import org.proview.modal.User.NormalUser;
import org.proview.modal.User.User;
import org.proview.test.AppMain;

import java.sql.*;
import java.util.Objects;

public class SQLUtils {
    static Connection connection = AppMain.connection;

    public static void addComment(int book_id, int user_id, String review) throws SQLException {
        String sql = "INSERT INTO review(book_id, user_id, review, time_added) VALUES (?, ?, ?, NOW());";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, book_id);
        preparedStatement.setInt(2, user_id);
        preparedStatement.setString(3, review);
        preparedStatement.executeUpdate();
    }

    public static User getUserFromId(int id) throws SQLException {
        String sql = "SELECT * FROM user WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.next()) {
            return null;
        } else {
            resultSet.isFirst();
            String username = resultSet.getString("username");
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            String email = resultSet.getString("email");

            if (username.equalsIgnoreCase("admin"))
                return new Admin(id, firstName, lastName, email);

            return new NormalUser(id, firstName, lastName, email);
        }
    }

    public static User getUserFromLoginCred(String username, String password) throws SQLException {
        Connection connection = AppMain.connection;
        String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.next()) {
            return null;
        } else {
            resultSet.isFirst();
            int id = resultSet.getInt("id");
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            String email = resultSet.getString("email");

            if (username.equalsIgnoreCase("admin"))
                return new Admin(id, username, password, firstName, lastName, email);

            return new NormalUser(id, username, password, firstName, lastName, email);
        }
    }

    public static ObservableList<Review> getReviewList() throws SQLException {
        Connection connection = AppMain.connection;
        String sql = """
            SELECT * FROM review
            ORDER BY time_added DESC;
        """;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        ObservableList<Review> reviewObservableList = FXCollections.observableArrayList();
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
        return reviewObservableList;
    }

    public static void alterStatusInDatabase(ObservableList<String> rowData, String newStatus) throws SQLException {
        int issueId = Integer.parseInt(rowData.getFirst());
        String sql = "UPDATE issue SET status = ? WHERE id = ?";
        Connection connection = AppMain.connection;
        PreparedStatement alterStatusPS = connection.prepareStatement(sql);
        alterStatusPS.setString(1, newStatus);
        alterStatusPS.setInt(2, issueId);
        alterStatusPS.executeUpdate();
        alterStatusPS.close();

        if (Objects.equals(newStatus, "Returned")) {
            String endDateSql = "UPDATE issue SET end_date = CURRENT_TIMESTAMP WHERE id = ?";
            PreparedStatement alterEndDatePS = AppMain.connection.prepareStatement(endDateSql);
            alterEndDatePS.setInt(1, issueId);
            alterEndDatePS.executeUpdate();
            alterEndDatePS.close();
        }
    }
}
