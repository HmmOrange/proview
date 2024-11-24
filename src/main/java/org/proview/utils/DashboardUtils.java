package org.proview.utils;

import javafx.scene.chart.XYChart;
import org.proview.modal.User.User;
import org.proview.modal.User.UserManagement;
import org.proview.test.AppMain;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DashboardUtils {
    private static abstract class GeneralUser {
        public static class AverageRatingBooksCountBarChart {
            public static XYChart.Series<String, Integer> getChartData() throws SQLException {
                XYChart.Series<String, Integer> respond = new XYChart.Series<>();
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
    }

    public static class Admin extends GeneralUser {
        public static class CopiesBorrowedPieChart {
            public static int getCopiesBorrowed() throws SQLException {
                return SQLUtils.getCurrentIssuesCount();
            }

            public static int getCopiesAvailable() throws SQLException {
                return SQLUtils.getTotalCopiesCount() - SQLUtils.getCurrentIssuesCount();
            }
        }

        public static class IssuesNumberLineChart {
            public static XYChart.Series<String, Integer> getChartData() throws SQLException {
                XYChart.Series<String, Integer> respond = new XYChart.Series<>();
                String sql = """
                            WITH RECURSIVE date_series AS (
                              SELECT MIN(DATE(start_date)) AS date
                              FROM issue
                              UNION
                              SELECT DATE_ADD(date, INTERVAL 1 DAY)
                              FROM date_series
                              WHERE date < (SELECT MAX(DATE(start_date)) FROM issue)
                            )
                            SELECT ds.date, COALESCE(COUNT(i.start_date), 0) AS total
                            FROM date_series ds
                            LEFT JOIN issue i ON DATE(i.start_date) = ds.date
                            GROUP BY ds.date
                            ORDER BY ds.date
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
                        WHERE status = '%s'
                        GROUP BY status
                        """.formatted(status);
                ResultSet resultSet = AppMain.connection.prepareStatement(sql).executeQuery();
                if (resultSet.next()) {
                    respond = resultSet.getInt("count");
                }
                return respond;
            }
        }

        public static class NewRegistrationLineChart {
            public static XYChart.Series<String, Integer> getChartData() throws SQLException {
                XYChart.Series<String, Integer> respond = new XYChart.Series<>();
                List<String> xAxisData = new ArrayList<>();
                List<Integer> yAxisData = new ArrayList<>();
                String sevenDaysSql = """
                        WITH RECURSIVE date_series AS (
                            SELECT MAX(DATE(registration_date)) AS date
                            FROM user
                            UNION
                            SELECT DATE_SUB(date, INTERVAL 1 DAY)
                            FROM date_series
                            WHERE date > (SELECT MAX(DATE(registration_date)) - INTERVAL 7 DAY FROM user)
                            )
                            SELECT ds.date, COALESCE(COUNT(u.registration_date), 0) AS total
                            FROM date_series ds
                            LEFT JOIN user u ON DATE(u.registration_date) = ds.date
                            GROUP BY ds.date
                            ORDER BY ds.date DESC;
                       """;
                ResultSet sevenDaysRS = AppMain.connection.prepareStatement(sevenDaysSql).executeQuery();
                while (sevenDaysRS.next()) {
                    xAxisData.add(sevenDaysRS.getDate("date").toString());
                    yAxisData.add(sevenDaysRS.getInt("total"));
                }

                String remainDaysSql = """
                        SELECT DATE(registration_date) AS date, COUNT(*) AS total
                        FROM user
                        WHERE DATE(registration_date) < (SELECT MAX(DATE(registration_date)) - INTERVAL 7 DAY FROM user)
                        GROUP BY DATE(registration_date)
                        ORDER BY date DESC;
                        """;
                ResultSet remainDaysRS = AppMain.connection.prepareStatement(remainDaysSql).executeQuery();
                while (remainDaysRS.next()) {
                    xAxisData.add(remainDaysRS.getDate("date").toString());
                    yAxisData.add(remainDaysRS.getInt("total"));
                }

                Collections.reverse(xAxisData);
                Collections.reverse(yAxisData);

                for (int i = 0; i < xAxisData.size(); i++) {
                    respond.getData().add(new XYChart.Data<>(xAxisData.get(i), yAxisData.get(i)));
                }
                return respond;
            }
        }
    }

    public static class NormalUser extends GeneralUser {
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
            public static XYChart.Series<String, Integer> getChartData() throws SQLException {
                XYChart.Series<String, Integer> respond = new XYChart.Series<>();
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
                            SELECT ds.date, COALESCE(COUNT(i.start_date), 0) AS total
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
}
