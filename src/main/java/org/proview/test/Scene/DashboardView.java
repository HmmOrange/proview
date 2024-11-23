package org.proview.test.Scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import org.proview.utils.DashboardUtils;

import java.sql.SQLException;

public class DashboardView {

    private void initCopiesBorrowedPieChart() throws SQLException {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Borrowed", DashboardUtils.CopiesBorrowedPieChart.getCopiesBorrowed()),
                new PieChart.Data("Available", DashboardUtils.CopiesBorrowedPieChart.getCopiesAvailable())
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
        XYChart.Series<String, Integer> datas = DashboardUtils.IssuesNumberLineChart.getChartData();
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

    public PieChart copiesBorrowedPieChart;
    public LineChart<String, Integer> issuesCreatedNumberLineChart;

    public void initialize() throws SQLException {
        initCopiesBorrowedPieChart();
        initIssueCreatedNumberLineChart();
    }

}
