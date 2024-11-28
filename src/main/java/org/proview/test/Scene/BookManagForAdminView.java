package org.proview.test.Scene;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.proview.model.User.NormalUser;
import org.proview.model.User.UserManagement;
import org.proview.utils.SQLUtils;
import org.proview.utils.TableViewUtils;

import java.sql.SQLException;
import java.util.Comparator;

public class BookManagForAdminView {
    public TableView<ObservableList<String>> booksTableView = new TableView<>();
    public Label totalBooksLabel;
    public Label reviewsLabel;
    public ComboBox<String> columnComboBox;
    public TextField searchTextField;
    public Label tagsLabel;
    public BorderPane homeBorderPane;

    public void initialize() throws SQLException {
        if (UserManagement.getCurrentUser() instanceof NormalUser) {
            homeBorderPane.setLeft(null);
        }
        totalBooksLabel.setText(Integer.toString((int) (SQLUtils.getBooksCount().getFirst() - 0)));
        reviewsLabel.setText(Integer.toString((int) (SQLUtils.getBooksCount().get(1) - 0)));
        tagsLabel.setText(Integer.toString(SQLUtils.getTagList().size()));

        String[] columns = {"ID", "Title", "Author", "Tags", "Description", "Copies", "Total queries", "Reviews", "Average Rating"};
        int[] prefWidthForEachColumn = {30, 100, 100, 100, 300, 40, 70, 40, 60};

        for (int i = 0; i < columns.length; i++) {
            if (i == 1) {
                TableViewUtils.addCoverColumn(booksTableView, 0);
            }
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columns[i]);
            int finalI = i;
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(finalI)));
            if (finalI == 0 || finalI == 5 || finalI == 6) {
                column.setComparator(Comparator.comparingInt(Integer::parseInt));
            }

            column.setPrefWidth(prefWidthForEachColumn[finalI]);
            TableViewUtils.setWrapTextToColumn(column);

            booksTableView.getColumns().add(column);

            if (UserManagement.getCurrentUser() instanceof NormalUser && i == 6) {
                booksTableView.getColumns().remove(column);
            }
        }
        ObservableList<ObservableList<String>> data = SQLUtils.getBooksData();
        booksTableView.setItems(data);
        TableViewUtils.setBookViewRedirection(booksTableView, 0);

        ///searching feature
        columnComboBox.getItems().addAll(columns);

        TableViewUtils.setSearchingFeature(data, searchTextField, columnComboBox, booksTableView);

    }

}
