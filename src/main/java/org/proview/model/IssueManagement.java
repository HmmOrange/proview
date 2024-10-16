package org.proview.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.proview.test.AppMain;

import java.sql.*;

public class IssueManagement {
    public static void addIssue(String username, int documentId, int duration) throws SQLException {
        Connection connection = AppMain.connection;

        String sql = "INSERT INTO issue(username, document_id, duration) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.setInt(2, documentId);
        preparedStatement.setInt(3, duration);
        preparedStatement.executeUpdate();
    }

    public static ObservableList<Issue> getIssueListFrom(String username) throws SQLException {
        ObservableList<Issue> issues = FXCollections.observableArrayList();
        Connection connection = AppMain.connection;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM issue WHERE username = " + username);
        while (resultSet.next()) {
            int issueId = resultSet.getInt("id");
            int documentId = resultSet.getInt("document_id");
            int duration = resultSet.getInt("duration");
            Issue curIssue = new Issue(issueId, documentId, duration, username);
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
            Document curDocument = DocumentManagement.getDocument(i.getDocumentId());
            if (curDocument == null) {
                continue;
            }
            String newIssueItem = index + ". " + curDocument.getName() + " - " + curDocument.getAuthor()
                                + " for " + i.getDuration() + (i.getDuration() == 1 ? " day" : " days")
                                + " (document #" + curDocument.getId() + ", issue #" + i.getId() + ")";
            issueStringList.add(newIssueItem);
        }

        return issueStringList;
    }
}
