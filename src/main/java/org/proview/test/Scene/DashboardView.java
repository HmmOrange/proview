package org.proview.test.Scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
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

    public PieChart copiesBorrowedPieChart;

    public void initialize() throws SQLException {
        initCopiesBorrowedPieChart();
    }

}
