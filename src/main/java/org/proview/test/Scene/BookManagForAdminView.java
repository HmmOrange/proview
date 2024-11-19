package org.proview.test.Scene;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.proview.utils.SQLUtils;

import java.sql.SQLException;
import java.util.Comparator;

public class BookManagForAdminView {
    public TableView<ObservableList<String>> booksTableView = new TableView<>();

    public void initialize() throws SQLException {
        String[] columns = {"ID", "Title", "Author", "Tag", "Description", "Copies", "Total queries", "Reviews", "Average Rating"};
        for (int i = 0; i < columns.length; i++) {
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columns[i]);
            int finalI = i;
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(finalI)));
            if (finalI == 0 || finalI == 5 || finalI == 6 || finalI == 7) {
                column.setComparator(Comparator.comparingInt(Integer::parseInt));
            }
            if (finalI == 8) column.setComparator(Comparator.comparingDouble(Double::parseDouble));
            booksTableView.getColumns().add(column);
        }
        booksTableView.setItems(SQLUtils.getBooksData());
    }
}
