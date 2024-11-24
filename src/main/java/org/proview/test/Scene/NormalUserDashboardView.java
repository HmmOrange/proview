package org.proview.test.Scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import org.proview.utils.DashboardUtils;

import java.sql.SQLException;
import java.util.Objects;

public class NormalUserDashboardView {
    public LineChart<String, Integer> issuesCreatedNumberLineChart;
    public PieChart booksBorrowedPieChart;
    public PieChart issueStatusPieChart;
    public BarChart<String, Integer> avgRatingBookCountBarChart;

    private void initIssueCreatedNumberLineChart() throws SQLException {
        XYChart.Series<String, Integer> datas = DashboardUtils.NormalUser.IssuesCreatedLineChart.getChartData();
        issuesCreatedNumberLineChart.getData().add(datas);
        for (XYChart.Data<String, Integer> data : datas.getData()) {
            String tooltipString = data.getXValue() + ": ";
            int yValue = data.getYValue();
            if (yValue == 0) {
                tooltipString += "No new issue.";
            } else if (yValue == 1) {
                tooltipString += "1 new issue.";
            } else {
                tooltipString += yValue + " new issues.";
            }
            Tooltip tooltip = new Tooltip(tooltipString);
            Tooltip.install(data.getNode(), tooltip);
        }
    }

    private void initBooksBorrowedPieChart() throws SQLException {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Borrowed", DashboardUtils.NormalUser.BookBorrowedPieChart.getBooksBorrowedCount()),
                new PieChart.Data("Haven't Borrowed", DashboardUtils.NormalUser.BookBorrowedPieChart.getBooksHaventBorrowedCount())
        );
        booksBorrowedPieChart.setData(pieChartData);

        for (PieChart.Data data : pieChartData) {
            data.getNode().setOnMouseEntered(mouseEvent -> {
                Tooltip dataTooltip = new Tooltip(data.getName() + ": " + (int) data.getPieValue());
                Tooltip.install(data.getNode(), dataTooltip);
            });
        }
    }

    private void initIssueStatusPieChart() throws SQLException {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Picked up", DashboardUtils.NormalUser.IssueStatusPieChart.getCountOfStatus("Picked up")),
                new PieChart.Data("Not picked up", DashboardUtils.NormalUser.IssueStatusPieChart.getCountOfStatus("Not picked up")),
                new PieChart.Data("Returned", DashboardUtils.NormalUser.IssueStatusPieChart.getCountOfStatus("Returned")),
                new PieChart.Data("Missing", DashboardUtils.NormalUser.IssueStatusPieChart.getCountOfStatus("Missing"))
        );
        issueStatusPieChart.setData(pieChartData);

        for (PieChart.Data data : pieChartData) {
            data.getNode().setOnMouseEntered(mouseEvent -> {
                Tooltip dataTooltip = new Tooltip(data.getName() + ": " + (int) data.getPieValue());
                Tooltip.install(data.getNode(), dataTooltip);
            });
        }
    }

    private void initAvgRatingBookCountBarChart() throws SQLException {
        XYChart.Series<String, Integer> datas = DashboardUtils.NormalUser.AverageRatingBooksCountBarChart.getChartData();
        avgRatingBookCountBarChart.getData().add(datas);
        for (XYChart.Data<String, Integer> data : datas.getData()) {
            StringBuilder tooltipString = new StringBuilder();
            if (Objects.equals(data.getXValue(), "0")) {
                tooltipString.append("No rating: ");
            } else if (Objects.equals(data.getXValue(), "5")) {
                tooltipString.append("5 Stars: ");
            } else {
                tooltipString.append("From %s Stars: ".formatted(data.getXValue()));
            }
            tooltipString.append(data.getYValue());
            if (data.getYValue() < 2) {
                tooltipString.append(" book.");
            } else {
                tooltipString.append(" books.");
            }
            Tooltip tooltip = new Tooltip(tooltipString.toString());
            Tooltip.install(data.getNode(), tooltip);
        }
    }

    public void initialize() throws SQLException {
        initIssueCreatedNumberLineChart();
        initBooksBorrowedPieChart();
        initIssueStatusPieChart();
        initAvgRatingBookCountBarChart();
    }
}
