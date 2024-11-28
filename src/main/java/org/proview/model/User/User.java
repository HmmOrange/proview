package org.proview.model.User;

import javafx.scene.chart.XYChart;
import org.proview.test.AppMain;
import org.proview.utils.SQLUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class User {
    protected int id;
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected Boolean cardView;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.cardView = true;
    }

    public User(int id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.cardView = true;
    }

    public User(int id, String username, String password, String firstName, String lastName, String email, Boolean cardView) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.cardView = cardView;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getCardView() {
        return cardView;
    }

    public void setCardView(Boolean cardView) throws SQLException {
        this.cardView = cardView;
        SQLUtils.setUserPreferredView(id, cardView);
    }

    public String getAvatarUrl() {
        return "./assets/avatars/user" + id + ".png";
    }

    public void addComment(int book_id, String review) throws SQLException {
        SQLUtils.addReview(book_id, id, review);
    }

    public static class AverageRatingBooksCountBarChart {
        public static XYChart.Series<String, Number> getChartData() throws SQLException {
            XYChart.Series<String, Number> respond = new XYChart.Series<>();
            String noRatingSql = """
                        SELECT COUNT(*) AS count FROM book
                        WHERE id NOT IN (SELECT book_id FROM rating)
                        """;
            ResultSet noRatingResultSet = AppMain.connection.prepareStatement(noRatingSql).executeQuery();
            if (noRatingResultSet.next()) {
                respond.getData().add(new XYChart.Data<>("0", noRatingResultSet.getInt("count")));
            }

            String between0And5Sql = """
                        WITH avg_rating AS (
                            SELECT AVG(rating) AS avg FROM rating
                            GROUP BY book_id
                        )
                        SELECT COUNT(*) AS count
                        FROM avg_rating
                        WHERE FLOOR(avg) = ?
                        """;
            PreparedStatement preparedStatement = AppMain.connection.prepareStatement(between0And5Sql);
            for (int i = 0; i <= 4; i++) {
                preparedStatement.setInt(1, i);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    respond.getData().add(new XYChart.Data<>("%d - <%d".formatted(i, i+1),
                            resultSet.getInt("count")));
                }
            }

            String perfectSql = """
                        WITH avg_rating AS (
                            SELECT AVG(rating) AS avg FROM rating
                            GROUP BY book_id
                        )
                        SELECT COUNT(*) AS count
                        FROM avg_rating
                        WHERE avg = 5
                        """;
            ResultSet perfectRS = AppMain.connection.prepareStatement(perfectSql).executeQuery();
            if (perfectRS.next()) {
                respond.getData().add(new XYChart.Data<>("5", perfectRS.getInt("count")));
            }

            return respond;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof User)) {
            return false;
        }

        User u = (User) o;
        return (u.getId() == id);
    }
}
