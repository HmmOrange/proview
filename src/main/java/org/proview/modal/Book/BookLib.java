package org.proview.modal.Book;

import org.proview.test.AppMain;
import org.proview.utils.SQLUtils;

import java.sql.*;

public class BookLib extends Book {
    private int id;
    private String imagePath;
    private int copiesAvailable;
    private double rating = 0;
    private int ratingCount = 0;
    private int issueCount = -1;
    private int issueCount7Days = -1;

    public BookLib(int id, String title, String author) {
        super(title, author);
        this.id = id;
    }

    public BookLib(int id, String title, String author, String description, int copiesAvailable) throws SQLException {
        super(title, author, description);
        this.id = id;
        this.imagePath = "./assets/covers/cover" + id + ".png";
        this.copiesAvailable = copiesAvailable;

        this.issueCount = getIssueCount();
        this.issueCount7Days = getIssueCount7Days();
        this.rating = getRating();
    }

    public int getId() {
        return id;
    }

    @Override
    public String getTags() throws SQLException {
        Connection connection = AppMain.connection;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM book_tag WHERE book_id = " + id);

        StringBuilder result = new StringBuilder();
        while (resultSet.next()) {
            String tag = resultSet.getString("tag");
            result.append(tag).append(", ");
        }

        if (!result.isEmpty()) result = new StringBuilder(result.substring(0, result.length() - 2));
        return result.toString();
    }


    public double getRating() throws SQLException {
        Connection connection = AppMain.connection;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM rating WHERE book_id = " + id);

        double sum = 0;
        ratingCount = 0;
        while (resultSet.next()) {
            double rating = resultSet.getDouble("rating");
            sum += rating;
            ratingCount++;
        }

        return (double) Math.round(sum / ratingCount * 100) / 100;
    }

    public int getRatingCount() throws SQLException {
        return ratingCount;
    }

    public int getIssueCount() throws SQLException {
        if (issueCount >= 0)
            return issueCount;

        Connection connection = AppMain.connection;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS issue_count FROM issue WHERE book_id = " + id);

        int count = 0;
        if (resultSet.next()) {
            count = resultSet.getInt("issue_count");
        }

        return count;
    }

    public int getIssueCount7Days() throws SQLException {
        if (issueCount7Days >= 0)
            return issueCount7Days;

        Connection connection = AppMain.connection;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT COUNT(*) AS issue_count7 FROM issue WHERE book_id = " + id + " AND TIMESTAMPDIFF(DAY, start_date, CURRENT_TIMESTAMP) <= 7;"
        );

        int count = 0;
        if (resultSet.next()) {
            count = resultSet.getInt("issue_count7");
        }

        return count;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getCopiesAvailable() throws SQLException {
        copiesAvailable = SQLUtils.copiesAvailable(id);
        return copiesAvailable;

    }
}
