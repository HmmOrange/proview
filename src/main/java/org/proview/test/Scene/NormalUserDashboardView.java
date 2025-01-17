package org.proview.test.Scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import org.proview.model.User.NormalUser;

import java.sql.SQLException;
import java.util.Objects;

public class NormalUserDashboardView {
    public LineChart<String, Number> issuesCreatedNumberLineChart;
    public PieChart booksBorrowedPieChart;
    public PieChart issueStatusPieChart;
    public BarChart<String, Number> avgRatingBookCountBarChart;

    private void initIssueCreatedNumberLineChart() throws SQLException {
        XYChart.Series<String, Number> datas = NormalUser.IssuesCreatedLineChart.getChartData();
        issuesCreatedNumberLineChart.getData().add(datas);
        for (XYChart.Data<String, Number> data : datas.getData()) {
            String tooltipString = data.getXValue() + ": ";
            int yValue = (int) data.getYValue();
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
                new PieChart.Data("Borrowed", NormalUser.BookBorrowedPieChart.getBooksBorrowedCount()),
                new PieChart.Data("Haven't Borrowed", NormalUser.BookBorrowedPieChart.getBooksHaventBorrowedCount())
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
                new PieChart.Data("Picked up", NormalUser.IssueStatusPieChart.getCountOfStatus("Picked up")),
                new PieChart.Data("Not picked up", NormalUser.IssueStatusPieChart.getCountOfStatus("Not picked up")),
                new PieChart.Data("Returned", NormalUser.IssueStatusPieChart.getCountOfStatus("Returned")),
                new PieChart.Data("Missing", NormalUser.IssueStatusPieChart.getCountOfStatus("Missing"))
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
        XYChart.Series<String, Number> datas = NormalUser.AverageRatingBooksCountBarChart.getChartData();
        avgRatingBookCountBarChart.getData().add(datas);
        for (XYChart.Data<String, Number> data : datas.getData()) {
            StringBuilder tooltipString = new StringBuilder();
            if (Objects.equals(data.getXValue(), "0")) {
                tooltipString.append("No rating: ");
            } else if (Objects.equals(data.getXValue(), "5")) {
                tooltipString.append("5 Stars: ");
            } else {
                tooltipString.append("From %s Stars: ".formatted(data.getXValue()));
            }
            tooltipString.append(data.getYValue());
            if ((int) data.getYValue() < 2) {
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
