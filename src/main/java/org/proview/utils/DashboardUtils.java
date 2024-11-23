package org.proview.utils;

import javafx.scene.chart.XYChart;
import org.proview.test.AppMain;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardUtils {
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
}
