package org.proview.model;

import org.proview.test.AppMain;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BookCell {
    private int id;
    private String title;
    private String author;
    private int issueCount;
    private String imagePath;
    private int copiesAvailable;

    public BookCell(int id, String title, String author, int issueCount, String imagePath, int copiesAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.issueCount = issueCount;
        this.imagePath = imagePath;
        this.copiesAvailable = copiesAvailable;
    }

    public String getTitle() {
        return "#" + id + ". " + title;
    }

    public String getAuthor() {
        return author;
    }

    public String getTags() throws SQLException {
        Connection connection = AppMain.connection;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM tag WHERE book_id = " + id);

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
        ResultSet resultSet = statement.executeQuery("SELECT * FROM review WHERE book_id = " + id);

        double sum = 0, count = 0;
        while (resultSet.next()) {
            double rating = resultSet.getDouble("rating");
            sum += rating;
            count++;
        }

        return (double) Math.round(sum / count * 100) / 100;
    }

    public int getIssueCount() {
        return issueCount;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getCopiesAvailable() {
        return copiesAvailable;
    }
}
