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
                            SELECT
                                DATE(DATE_SUB(CURRENT_DATE, INTERVAL a.DAY DAY)) AS issue_date,
                                COUNT(i.id) AS issue_count
                            FROM
                                (SELECT 0 AS DAY UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4
                                 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) AS a
                            LEFT JOIN
                                issue i ON DATE(i.start_date) = DATE(DATE_SUB(CURRENT_DATE, INTERVAL a.DAY DAY))
                            WHERE i.user_id = ?
                            GROUP BY
                                issue_date
                            ORDER BY
                                issue_date ASC;
                            """;
            PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
            preparedStatement.setInt(1, UserManagement.getCurrentUser().getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String date = resultSet.getDate("issue_date").toString();
                int count = resultSet.getInt("issue_count");
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
