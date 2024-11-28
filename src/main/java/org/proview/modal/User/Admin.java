package org.proview.modal.User;

import javafx.scene.chart.XYChart;
import org.proview.test.AppMain;
import org.proview.utils.SQLUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Admin extends User {
    public Admin(int id, String username, String password, String firstName, String lastName, String email, Boolean cardView) {
        super(id, username, password, firstName, lastName, email, cardView);
    }

    public Admin(int id, String firstName, String lastName, String email) {
        super(id, firstName, lastName, email);
    }

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
            /// remain days
            String remainDaysSql = """
                        SELECT DATE(start_date) AS date, COUNT(*) AS total
                        FROM issue
                        WHERE DATE(start_date) < (SELECT MAX(DATE(start_date)) - INTERVAL 7 DAY FROM issue)
                        GROUP BY DATE(start_date)
                        ORDER BY date ASC;
                        """;
            ResultSet remainDaysResultSet = AppMain.connection.prepareStatement(remainDaysSql).executeQuery();
            while (remainDaysResultSet.next()) {
                String date = remainDaysResultSet.getDate("date").toString();
                int count = remainDaysResultSet.getInt("total");
                respond.getData().add(new XYChart.Data<>(date, count));
            }

            /// get statistics 7 days before today
            String sevenDaysSql = """
                            WITH RECURSIVE date_series AS (
                                SELECT MAX(DATE(start_date)) AS date
                                FROM issue
                                UNION
                                SELECT DATE_SUB(date, INTERVAL 1 DAY)
                                FROM date_series
                                WHERE date > (SELECT MAX(DATE(start_date)) - INTERVAL 7 DAY FROM issue)
                            )
                            SELECT ds.date, COALESCE(COUNT(i.start_date), 0) AS total
                            FROM date_series ds
                            LEFT JOIN issue i ON DATE(i.start_date) = ds.date
                            GROUP BY ds.date
                            ORDER BY ds.date
                            """;
            ResultSet sevenDaysResultSet = AppMain.connection.prepareStatement(sevenDaysSql).executeQuery();
            while (sevenDaysResultSet.next()) {
                String date = sevenDaysResultSet.getDate("date").toString();
                int count = sevenDaysResultSet.getInt("total");
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
