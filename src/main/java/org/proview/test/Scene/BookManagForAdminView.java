package org.proview.test.Scene;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.proview.utils.SQLUtils;
import org.proview.utils.TableViewUtils;

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

        String[] columns = {"ID", "Title", "Author", "TagView", "Description", "Copies", "Total queries"};
        int[] prefWidthForEachColumn = {30, 100, 100, 100, 300, 40, 70};

        for (int i = 0; i < columns.length; i++) {
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columns[i]);
            int finalI = i;
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(finalI)));
            if (finalI == 0 || finalI == 5 || finalI == 6) {
                column.setComparator(Comparator.comparingInt(Integer::parseInt));
            }

            column.setPrefWidth(prefWidthForEachColumn[finalI]);
            TableViewUtils.setWrapTextToColumn(column);

            booksTableView.getColumns().add(column);
        }
        ObservableList<ObservableList<String>> data = SQLUtils.getBooksData();
        booksTableView.setItems(data);
        TableViewUtils.setBookViewRedirection(booksTableView, 0);

        ///searching feature
        columnComboBox.getItems().addAll(columns);

        TableViewUtils.setSearchingFeature(data, searchTextField, columnComboBox, booksTableView);

    }
}
