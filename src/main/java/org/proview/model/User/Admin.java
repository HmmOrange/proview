package org.proview.model.User;

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
                    GROUP BY
                        issue_date
                    ORDER BY
                        issue_date ASC;
                    """;
            ResultSet resultSet = AppMain.connection.prepareStatement(sql).executeQuery();
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
        public static XYChart.Series<String, Number> getChartData() throws SQLException {
            XYChart.Series<String, Number> respond = new XYChart.Series<>();
            String sql = """
                        SELECT
                         DATE(DATE_SUB(CURRENT_DATE, INTERVAL a.DAY DAY)) AS registration_date,
                         COUNT(u.id) AS registration_count
                     FROM
                         (SELECT 0 AS DAY
                          UNION SELECT 1
                          UNION SELECT 2
                          UNION SELECT 3
                          UNION SELECT 4
                          UNION SELECT 5
                          UNION SELECT 6) AS a
                     LEFT JOIN
                         user u
                         ON DATE(u.registration_date) = DATE(DATE_SUB(CURRENT_DATE, INTERVAL a.DAY DAY))
                     GROUP BY
                         DATE(DATE_SUB(CURRENT_DATE, INTERVAL a.DAY DAY))
                     ORDER BY
                         registration_date ASC;
                     """;
            ResultSet resultSet = AppMain.connection.prepareStatement(sql).executeQuery();
            while (resultSet.next()) {
                String s = resultSet.getDate("registration_date").toString();
                int total = resultSet.getInt("registration_count");
                respond.getData().add(new XYChart.Data<>(s, total));
            }

            return respond;
        }
    }
}
