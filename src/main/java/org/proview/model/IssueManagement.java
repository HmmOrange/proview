package org.proview.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.proview.test.AppMain;

import java.sql.*;

public class IssueManagement {
    public static void addIssue(String username, int bookId, int duration) throws SQLException {
        Connection connection = AppMain.connection;

        String sql = "INSERT INTO issue(username, book_id, duration) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.setInt(2, bookId);
        preparedStatement.setString(3, String.valueOf(duration));
        preparedStatement.executeUpdate();
    }

    public static ObservableList<Issue> getIssueListFrom(String username) throws SQLException {
        ObservableList<Issue> issues = FXCollections.observableArrayList();
        Connection connection = AppMain.connection;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM issue WHERE username = " + username);
        while (resultSet.next()) {
            int issueId = resultSet.getInt("id");
            int bookId = resultSet.getInt("book_id");
            int duration = resultSet.getInt("duration");
            Issue curIssue = new Issue(issueId, bookId, duration, username);
            issues.add(curIssue);
        }
        return issues;
    }

    public static ObservableList<String> getIssueListViewFrom(String username) throws SQLException {
        ObservableList<Issue> currentIssueList = null;
        ObservableList<String> issueStringList = FXCollections.observableArrayList();
        try {
            currentIssueList = IssueManagement.getIssueListFrom(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int index = 0;
        for (Issue i : currentIssueList) {
            index++;
            Book curBook = BookManagement.getBook(i.getBookId());
            if (curBook == null) {
                continue;
            }
            String newIssueItem = index + ". " + curBook.getName() + " - " + curBook.getAuthor()
                                + " for " + i.getDuration() + (i.getDuration() == 1 ? " day" : " days")
                                + " (book #" + curBook.getId() + ", issue #" + i.getId() + ")";
            issueStringList.add(newIssueItem);
        }

        return issueStringList;
    }
}
