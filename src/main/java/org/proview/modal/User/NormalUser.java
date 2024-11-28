package org.proview.modal.User;

import javafx.scene.chart.XYChart;
import org.proview.test.AppMain;
import org.proview.utils.SQLUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NormalUser extends User {
    public NormalUser(int id, String username, String password, String firstName, String lastName, String email, Boolean cardView) {
        super(id, username, password, firstName, lastName, email, cardView);
    }

    public NormalUser(int id, String firstName, String lastName, String email) {
        super(id, firstName, lastName, email);
    }

    private static User getCurrentUser() {
        return UserManagement.getCurrentUser();
    }

    public static class BookBorrowedPieChart {
        public static int getBooksBorrowedCount() throws SQLException {
            int respond = 0;
            String sql = """
                        WITH user_borrow_book AS (
                            SELECT COUNT(*) FROM issue
                            WHERE user_id = ?
                            GROUP BY book_id
                        )
                        SELECT COUNT(*) AS borrow
                        FROM user_borrow_book
                        """;
            PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
            preparedStatement.setInt(1, getCurrentUser().getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                respond = resultSet.getInt("borrow");
            }
            return respond;
        }

        public static int getBooksHaventBorrowedCount() throws SQLException {
            return (int) (SQLUtils.getBooksCount().getFirst() - getBooksBorrowedCount());
        }
    }

    public static class IssuesCreatedLineChart {
        public static XYChart.Series<String, Number> getChartData() throws SQLException {
            XYChart.Series<String, Number> respond = new XYChart.Series<>();
            String sql = """
                            WITH RECURSIVE date_series AS (
                                SELECT MIN(DATE(start_date)) AS date
                                FROM issue
                                WHERE user_id = 2
                                UNION ALL
                                SELECT DATE_ADD(date, INTERVAL 1 DAY)
                                FROM date_series
                                WHERE date < (SELECT MAX(DATE(start_date)) FROM issue WHERE user_id = 2)
                            )
                            SELECT COALESCE(ds.date, CURRENT_TIMESTAMP) AS date, COALESCE(COUNT(i.start_date), 0) AS total
                            FROM date_series ds
                            LEFT JOIN issue i ON DATE(i.start_date) = ds.date AND i.user_id = 2
                            GROUP BY ds.date
                            ORDER BY ds.date;
                            """;
            ResultSet resultSet = AppMain.connection.prepareStatement(sql).executeQuery();
            while (resultSet.next()) {
                String date = resultSet.getDate("date").toString();
                int count = resultSet.getInt("total");
                respond.getData().add(new XYChart.Data<>(date, count));
            }
            return respond;
        }
    }

    public static class IssueStatusPieChart {
        public static int getCountOfStatus(String status) throws SQLException {
            int respond = 0;
            String sql = """
                        SELECT COUNT(*) AS count
                        FROM issue
                        WHERE user_id = ? AND status = '%s'
                        GROUP BY status
                        """.formatted(status);
            PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
            preparedStatement.setInt(1, getCurrentUser().getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                respond = resultSet.getInt("count");
            }
            return respond;
        }
    }
}
