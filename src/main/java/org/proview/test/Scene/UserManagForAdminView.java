package org.proview.test.Scene;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import org.proview.utils.SQLUtils;
import org.proview.utils.TableViewUtils;

import java.sql.SQLException;
import java.util.Comparator;

public class UserManagForAdminView {
    public TableView<ObservableList<String>> usersTableView = new TableView<>();
    public Label totalUsersLabel;
    public Label todayRegisLabel;
    public Label thisWeekRegisLabel;
    public ComboBox<String> columnComboBox;
    public TextField searchTextField;

    public void initialize() throws SQLException {
        totalUsersLabel.setText(Integer.toString(SQLUtils.getUsersCount().getFirst()));
        todayRegisLabel.setText(Integer.toString(SQLUtils.getUsersCount().get(1)));
        thisWeekRegisLabel.setText(Integer.toString(SQLUtils.getUsersCount().get(2)));

        String[] columns = {"ID", "Username", "Full name", "Email", "Registration Date", "Current queries", "Total queries", "Reviews"};
        for (int i = 0; i < columns.length; i++) {
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columns[i]);
            int finalI = i;
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(finalI)));
            if (finalI == 0 || finalI == 7 || finalI == 5 || finalI == 6) {
                column.setComparator(Comparator.comparingInt(Integer::parseInt));
            }
            TableViewUtils.setWrapTextToColumn(column);
            usersTableView.getColumns().add(column);
        }
        ObservableList<ObservableList<String>> data = SQLUtils.getUsersData();
        usersTableView.setItems(data);

        columnComboBox.getItems().addAll(columns);

        TableViewUtils.setSearchingFeature(data, searchTextField, columnComboBox, usersTableView);
    }
}
