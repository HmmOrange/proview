package org.proview.test.Scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.scene.control.Tooltip;
import org.proview.model.User.Admin;

import java.sql.SQLException;
import java.util.Objects;

public class AdminDashboardView {

    private void initCopiesBorrowedPieChart() throws SQLException {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Borrowed", Admin.CopiesBorrowedPieChart.getCopiesBorrowed()),
                new PieChart.Data("Available", Admin.CopiesBorrowedPieChart.getCopiesAvailable())
        );
        copiesBorrowedPieChart.setData(pieChartData);

        for (PieChart.Data data : pieChartData) {
            data.getNode().setOnMouseEntered(mouseEvent -> {
                Tooltip dataTooltip = new Tooltip(data.getName() + ": " + (int) data.getPieValue());
                Tooltip.install(data.getNode(), dataTooltip);
            });
        }
    }

    private void initIssueCreatedNumberLineChart() throws SQLException {
        XYChart.Series<String, Number> datas = Admin.IssuesNumberLineChart.getChartData();
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

    private void initIssueStatusPieChart() throws SQLException {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Picked up", Admin.IssueStatusPieChart.getCountOfStatus("Picked up")),
                new PieChart.Data("Not picked up", Admin.IssueStatusPieChart.getCountOfStatus("Not picked up")),
                new PieChart.Data("Returned", Admin.IssueStatusPieChart.getCountOfStatus("Returned")),
                new PieChart.Data("Missing", Admin.IssueStatusPieChart.getCountOfStatus("Missing"))
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
        XYChart.Series<String, Number> datas = Admin.AverageRatingBooksCountBarChart.getChartData();
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

    private void initNewRegistrationLineChart() throws SQLException {
        XYChart.Series<String, Number> datas = Admin.NewRegistrationLineChart.getChartData();
        newRegistrationLineChart.getData().add(datas);
        for (XYChart.Data<String, Number> data : datas.getData()) {
            int number = (int) data.getYValue();
            StringBuilder sb = new StringBuilder(data.getXValue()).append(": ").append(number);
            if (number < 2) {
                sb.append(" new registration.");
            } else {
                sb.append(" new registrations.");
            }
            Tooltip tooltip = new Tooltip(sb.toString());
            Tooltip.install(data.getNode(), tooltip);
        }
    }

    public PieChart issueStatusPieChart;
    public BarChart<String, Number> avgRatingBookCountBarChart;
    public LineChart<String, Number> newRegistrationLineChart;
    public PieChart copiesBorrowedPieChart;
    public LineChart<String, Number> issuesCreatedNumberLineChart;

    public void initialize() throws SQLException {
        initCopiesBorrowedPieChart();
        initIssueCreatedNumberLineChart();
        initIssueStatusPieChart();
        initAvgRatingBookCountBarChart();
        initNewRegistrationLineChart();
    }

}
