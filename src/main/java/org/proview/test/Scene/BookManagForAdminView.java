package org.proview.test.Scene;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import org.proview.utils.SQLUtils;

import java.sql.SQLException;
import java.util.Comparator;

public class BookManagForAdminView {
    public TableView<ObservableList<String>> booksTableView = new TableView<>();
    public Label totalBooksLabel;
    public Label avgRatingLabel;
    public ComboBox<String> columnComboBox;
    public TextField searchTextField;

    public void initialize() throws SQLException {
        totalBooksLabel.setText(Integer.toString((int) (SQLUtils.getBooksCount().getFirst() - 0)));
        avgRatingLabel.setText(Double.toString(SQLUtils.getBooksCount().get(1)));

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
        ObservableList<ObservableList<String>> data = SQLUtils.getBooksData();
        booksTableView.setItems(data);

        columnComboBox.getItems().addAll(columns);
        FilteredList<ObservableList<String>> filteredData = new FilteredList<>(data, p -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            String selectedColumn = columnComboBox.getValue();
            if (selectedColumn != null) {
                int columnIndex = columnComboBox.getItems().indexOf(selectedColumn);
                filteredData.setPredicate(row -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true; // Show all rows if search text is empty
                    }
                    String cellValue = row.get(columnIndex); // Get value in the selected column
                    return cellValue != null && cellValue.toLowerCase().contains(newValue.toLowerCase());
                });
            }
        });
        booksTableView.setItems(filteredData);

    }
}
