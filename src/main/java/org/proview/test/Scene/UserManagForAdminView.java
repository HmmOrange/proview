package org.proview.test.Scene;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.proview.utils.SQLUtils;

import java.sql.SQLException;
import java.util.Comparator;

public class UserManagForAdminView {
    public TableView<ObservableList<String>> usersTableView = new TableView<>();

    public void initialize() throws SQLException {
        String[] columns = {"ID", "Username", "Full name", "Email", "Registration Date", "Current queries", "Total queries", "Reviews"};
        for (int i = 0; i < columns.length; i++) {
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columns[i]);
            int finalI = i;
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(finalI)));
            if (finalI == 0 || finalI == 7 || finalI == 5 || finalI == 6) {
                column.setComparator(Comparator.comparingInt(Integer::parseInt));
            }
            usersTableView.getColumns().add(column);
        }
        usersTableView.setItems(SQLUtils.getUsersData());
    }
}
