package org.proview.model.Issue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.proview.model.Book.BookLib;
import org.proview.model.Book.BookManagement;
import org.proview.utils.SQLUtils;
import org.proview.test.AppMain;

import java.sql.*;

public class IssueManagement {
    public static void addIssue(String username, int bookId, int duration) throws SQLException {
        Connection connection = AppMain.connection;

        String sql = "INSERT INTO issue(username, book_id, duration, start_date, status, user_id) VALUES (?, ?, ?, NOW(), 'Not picked up', ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.setInt(2, bookId);
        preparedStatement.setString(3, String.valueOf(duration));
        preparedStatement.setInt(4, SQLUtils.getUserIdFrom(username));
        preparedStatement.executeUpdate();
    }

    public static ObservableList<Issue> getIssueListFrom(int userId) throws SQLException {
        ObservableList<Issue> issues = FXCollections.observableArrayList();
        Connection connection = AppMain.connection;
        String sql = "SELECT * FROM issue WHERE user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int issueId = resultSet.getInt("id");
            int bookId = resultSet.getInt("book_id");
            int duration = resultSet.getInt("duration");
            Timestamp start_time = resultSet.getTimestamp("start_date");
            String status = resultSet.getString("status");
            String username = resultSet.getString("username");
            Issue curIssue = new Issue(issueId, bookId, duration, username, status, start_time);
            if (status.equals("Returned")) {
                Timestamp end_time = resultSet.getTimestamp("end_date");
                curIssue = new Issue(issueId, bookId, duration, username, status, start_time, end_time);
            }
            issues.add(curIssue);
        }
        return issues;
    }
}
