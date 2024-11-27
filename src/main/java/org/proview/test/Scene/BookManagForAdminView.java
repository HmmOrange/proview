package org.proview.test.Scene;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.proview.utils.SQLUtils;
import org.proview.utils.TableViewUtils;

import java.io.File;
import java.sql.SQLException;
import java.util.Comparator;

public class BookManagForAdminView {
    public TableView<ObservableList<String>> booksTableView = new TableView<>();
    public Label totalBooksLabel;
    public Label reviewsLabel;
    public ComboBox<String> columnComboBox;
    public TextField searchTextField;

    public void initialize() throws SQLException {
        totalBooksLabel.setText(Integer.toString((int) (SQLUtils.getBooksCount().getFirst() - 0)));
        reviewsLabel.setText(Integer.toString((int) (SQLUtils.getBooksCount().get(1) - 0)));

        String[] columns = {"ID", "Title", "Author", "Tags", "Description", "Copies", "Total queries", "Reviews", "Average Rating"};
        int[] prefWidthForEachColumn = {30, 100, 100, 100, 300, 40, 70, 40, 60};

        for (int i = 0; i < columns.length; i++) {
            if (i == 1) {
                addCoverColumn();
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
        }
        ObservableList<ObservableList<String>> data = SQLUtils.getBooksData();
        booksTableView.setItems(data);
        TableViewUtils.setBookViewRedirection(booksTableView, 0);

        ///searching feature
        columnComboBox.getItems().addAll(columns);

        TableViewUtils.setSearchingFeature(data, searchTextField, columnComboBox, booksTableView);

    }

    private void addCoverColumn() {
        TableColumn<ObservableList<String>, String> statusColumn = new TableColumn<>("Cover");

        // Set the cell value factory to provide some data to the column
        statusColumn.setCellValueFactory(cellData -> {
            ObservableList<String> row = cellData.getValue();
            if (row != null && !row.isEmpty()) {
                String bookId = row.getFirst(); // Giả sử cột đầu tiên là bookId
                String path = "./assets/covers/cover%s.png".formatted(String.valueOf(Integer.parseInt(bookId)));  // Tạo đường dẫn
                return new SimpleStringProperty(path);
            }
            return new SimpleStringProperty(""); // Trả về chuỗi rỗng nếu không có dữ liệu
        });

        // Set the cell factory to render an image
        statusColumn.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null); // Clear the cell content
                } else {
                    // Set an image based on the cell value (item)
                    File file = new File(item); // Use the item as the path
                    if (file.exists()) { // Check if the file exists
                        Image image = new Image(file.toURI().toString());
                        imageView.setImage(image);
                        imageView.setFitWidth(100); // Set desired width
                        imageView.setFitHeight(140); // Set desired height
                        setGraphic(imageView);      // Add the ImageView to the cell
                    } else {
                        System.err.println("File not found: " + file.getAbsolutePath());
                        setGraphic(null);
                    }
                }
            }
        });

        booksTableView.getColumns().add(statusColumn);
    }

}
