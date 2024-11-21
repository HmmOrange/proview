package org.proview.test.Scene;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.util.Comparator;

public class TagManageView {
    public TableView<ObservableList<String>> tagTable;
    public TextField tagEditNameLabel;
    public TextField tagEditBgColorHex;
    public TextField tagEditTextColorHex;

    public void initialize() {
        String[] columnName = {"Tag Name", "Tag Background Color", "Tag Text Color", "Tag Preview"};
        for (int i = 0; i < 3; i++) {
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnName[i]);
            int finalI = i;
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(finalI)));
            tagTable.getColumns().add(column);
        }

        // For the last column, use Tag & TagView instead of String
        TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnName[columnName.length - 1]);
        column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columnName.length - 1)));
        column.setCellFactory(column -> );
    }
}
