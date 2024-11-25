package org.proview.utils;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class TableViewUtils {
    public static void setWrapTextToColumn(TableColumn<ObservableList<String>, String> column) {
        column.setCellFactory(col -> new TableCell<>() {
            private final Text text;

            {
                text = new Text();
                text.wrappingWidthProperty().bind(col.widthProperty());
                setGraphic(text);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    text.setText(null);
                } else {
                    text.setText(item);
                    setTextAlignment(TextAlignment.LEFT);
                    setAlignment(Pos.CENTER_LEFT);
                }
            }
        });
    }

    public static void setSearchingFeature(ObservableList<ObservableList<String>> currentList,
                                           TextField searchTextField, ComboBox<String> columnComboBox,
                                           TableView<ObservableList<String>> tableView) {
        FilteredList<ObservableList<String>> currentFilteredData = new FilteredList<>(currentList, p -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            String selectedColumn = columnComboBox.getValue();
            if (selectedColumn != null) {
                int columnIndex = columnComboBox.getItems().indexOf(selectedColumn);
                currentFilteredData.setPredicate(row -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true; // Show all rows if search text is empty
                    }
                    String cellValue = row.get(columnIndex); // Get value in the selected column
                    return cellValue != null && cellValue.toLowerCase().contains(newValue.toLowerCase());
                });
            }
        });
        SortedList<ObservableList<String>> sortedData = new SortedList<>(currentFilteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
    }
}
