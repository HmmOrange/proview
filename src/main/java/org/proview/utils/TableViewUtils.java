package org.proview.utils;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.proview.test.AppMain;
import org.proview.test.Scene.BookInfoView;
import org.proview.test.Scene.ProfileView;

import java.io.IOException;
import java.sql.SQLException;

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

    public static void setBookViewRedirection(TableView<ObservableList<String>> booksTableView, int bookIdPos) {
        booksTableView.setRowFactory(tv -> {
            TableRow<ObservableList<String>> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    int bookId = Integer.parseInt(row.getItem().get(bookIdPos));
                    FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("BookInfoView.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(fxmlLoader.load(), 1300, 700);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    AppMain.window.setTitle("Hello!");
                    AppMain.window.setScene(scene);
                    AppMain.window.centerOnScreen();

                    BookInfoView tempBookInfoView = fxmlLoader.getController();
                    try {
                        tempBookInfoView.setData(bookId);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return row;
        });
    }

    public static void setUserProfileViewRedirection(TableView<ObservableList<String>> usersTableView, int userIdPos) {
        usersTableView.setRowFactory(tv -> {
            TableRow<ObservableList<String>> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    try {
                        ProfileView.setUserFromId(Integer.parseInt(row.getItem().get(userIdPos)));
                        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("ProfileView.fxml"));
                        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
                        AppMain.window.setTitle("Hello!");
                        AppMain.window.setScene(scene);
                        AppMain.window.centerOnScreen();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return row;
        });
    }
}
