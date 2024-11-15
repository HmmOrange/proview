package org.proview.modal.Book;

import org.proview.test.AppMain;

import java.sql.*;

public class BookLib extends Book {
    private int id;
    private String imagePath;
    private int copiesAvailable;
    private double rating = -1;
    private int issueCount = -1;
    private int issueCount7Days = -1;

    public BookLib(int id) throws SQLException {
        super();

        String sql = "SELECT * FROM book WHERE id = ?";
        PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            super.setTitle(resultSet.getString("name"));
            super.setAuthor(resultSet.getString("author"));
            super.setDescription(resultSet.getString("description"));
            this.id = id;
            this.copiesAvailable = resultSet.getInt("copies");
            this.imagePath = String.format("./assets/covers/cover%d.png", id);
        }
    }

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
        if (rating >= 0)
            return rating;

        Connection connection = AppMain.connection;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM rating WHERE book_id = " + id);

        double sum = 0, count = 0;
        while (resultSet.next()) {
            double rating = resultSet.getDouble("rating");
            sum += rating;
            count++;
        }

        return (double) Math.round(sum / count * 100) / 100;
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

    public int getCopiesAvailable() {
        return copiesAvailable;
    }
}