package org.proview.modal.Issue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.proview.modal.Book.BookLib;
import org.proview.modal.Book.BookManagement;
import org.proview.modal.Utils.SQLUtils;
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

    public static ObservableList<Issue> getIssueListFrom(String username) throws SQLException {
        ObservableList<Issue> issues = FXCollections.observableArrayList();
        Connection connection = AppMain.connection;
        /*Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM issue WHERE username = " + username);
        while (resultSet.next()) {
            int issueId = resultSet.getInt("id");
            int bookId = resultSet.getInt("book_id");
            int duration = resultSet.getInt("duration");
            Issue curIssue = new Issue(issueId, bookId, duration, username);
            issues.add(curIssue);
        }*/
        String sql = "SELECT * FROM issue WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int issueId = resultSet.getInt("id");
            int bookId = resultSet.getInt("book_id");
            int duration = resultSet.getInt("duration");
            Issue curIssue = new Issue(issueId, bookId, duration, username);
            issues.add(curIssue);
        }
        return issues;
    }

    /*public static ObservableList<Issue> getUserIssueListFrom(String username) throws SQLException {
        ObservableList<Issue> issues = FXCollections.observableArrayList();
        Connection connection = AppMain.connection;
        String sql = "SELECT * FROM issue WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int issueId = resultSet.getInt("id");
            int bookId = resultSet.getInt("book_id");
            int duration = resultSet.getInt("duration");
            String startDate = resultSet.getDate("start_date").toString();
            String bookName = "";

            String nameSql = "SELECT name FROM book WHERE id = ?";
            PreparedStatement namePreparedStatement = connection.prepareStatement(nameSql);
            namePreparedStatement.setInt(1, bookId);
            ResultSet nameResultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                bookName = nameResultSet.getString("name");
            }
            Issue curIssue = new Issue(issueId, bookId, bookName, startDate, duration);
            issues.add(curIssue);
        }
        return issues;
    }*/

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
            BookLib curBook = BookManagement.getBook(i.getBookId());
            if (curBook == null) {
                continue;
            }
            String newIssueItem = index + ". " + curBook.getTitle() + " - " + curBook.getAuthor()
                                + " for " + i.getDuration() + (i.getDuration() == 1 ? " day" : " days")
                                + " (book #" + curBook.getId() + ", issue #" + i.getId() + ")";
            issueStringList.add(newIssueItem);
        }

        return issueStringList;
    }
}
