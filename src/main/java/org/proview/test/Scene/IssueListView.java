package org.proview.test.Scene;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.proview.model.User.Admin;
import org.proview.model.User.NormalUser;
import org.proview.model.User.UserManagement;
import org.proview.utils.SQLUtils;
import org.proview.test.AppMain;
import org.proview.utils.TableViewUtils;

import java.io.IOException;
import java.sql.*;
import java.util.Comparator;

public class IssueListView {
    private static void resetTableAfterChangeStatus() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("IssueListView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }

    private void initComboBox() {
        currentColumnComboBox.getItems().addAll("ID", "Username", "Title",
                "Author", "Book ID", "Due Date", "Remaining time", "Status");

        pastColumnComboBox.getItems().addAll("ID", "Username", "Title",
                "Author", "Book ID", "Start Date", "End Date", "Status");
    }

    private void initBorrowingTableView() {
        borrowingTableView.getColumns().clear();
        String[] columns1 = {"ID", "Username", "Title", "Author", "Book ID", "Due Date", "Days Left", "Status"};
        int[] prefWidthForBorrowingTable = {20, 70, 150, 150, 50, 100, 70, 150};
        for (int i = 0; i < columns1.length - 1; i++) {
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columns1[i]);
            int finalI = i;
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(finalI)));
            if (finalI == 0 || finalI == 4 || finalI == 6) {
                column.setComparator(Comparator.comparingInt(Integer::parseInt));
            }
            column.setPrefWidth(prefWidthForBorrowingTable[finalI]);
            TableViewUtils.setWrapTextToColumn(column);
            if (i == 2) {
                TableViewUtils.addCoverColumn(borrowingTableView, 4);
            }
            borrowingTableView.getColumns().add(column);
        }
        TableViewUtils.setBookViewRedirection(borrowingTableView, 4);

        ///xử lý trạng thái sách
        if (UserManagement.getCurrentUser() instanceof Admin) {
            TableColumn<ObservableList<String>, String> statusColumn = new TableColumn<>(columns1[columns1.length - 1]);
            statusColumn.setPrefWidth(prefWidthForBorrowingTable[columns1.length - 1]);
            statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columns1.length - 1)));
            statusColumn.setCellFactory(column -> new TableCell<>() {
                private final ComboBox<String> comboBox = new ComboBox<>();
                private final Button confirmButton = new Button("Confirm");
                private final HBox hBox = new HBox(5, comboBox, confirmButton); // HBox chứa ComboBox và Confirm
                {
                    hBox.setAlignment(Pos.CENTER);
                }

                {
                    comboBox.getItems().addAll("Picked up", "Not picked up", "Returned", "Missing");
                    confirmButton.setVisible(false); // Mặc định ẩn nút Confirm

                    comboBox.setOnAction(e -> {
                        int rowIndex = getIndex(); // Lấy chỉ số hàng hiện tại
                        if (rowIndex >= 0) {
                            ObservableList<String> rowData = getTableView().getItems().get(rowIndex);
                            String originalStatus = rowData.get(columns1.length - 1); // Giá trị gốc
                            String newStatus = comboBox.getValue(); // Giá trị mới

                            // Hiển thị nút Confirm nếu giá trị thay đổi
                            confirmButton.setVisible(!newStatus.equals(originalStatus));
                        }
                    });

                    confirmButton.setOnAction(e -> {
                        int rowIndex = getIndex();
                        if (rowIndex >= 0) {
                            ObservableList<String> rowData = getTableView().getItems().get(rowIndex);
                            String newStatus = comboBox.getValue();
                            rowData.set(columns1.length - 1, newStatus); // Cập nhật giá trị mới vào dữ liệu gốc
                            confirmButton.setVisible(false); // Ẩn nút Confirm sau khi xác nhận
                            try {
                                SQLUtils.alterStatusInDatabase(rowData, newStatus);
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                            try {
                                resetTableAfterChangeStatus();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });
                }

                @Override
                protected void updateItem(String status, boolean empty) {
                    super.updateItem(status, empty);
                    if (empty || status == null) {
                        setGraphic(null);
                    } else {
                        comboBox.setValue(status); // Hiển thị giá trị hiện tại
                        confirmButton.setVisible(false); // Đảm bảo nút Confirm ẩn khi cập nhật lại
                        setGraphic(hBox); // Gắn HBox chứa ComboBox và Confirm
                    }
                }
            });
            // Thêm cột vào TableView
            borrowingTableView.getColumns().add(statusColumn);
        } else {
            TableColumn<ObservableList<String>, String> statusColumn = new TableColumn<>(columns1[columns1.length - 1]);
            statusColumn.setPrefWidth(prefWidthForBorrowingTable[columns1.length - 1]);
            statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columns1.length - 1)));
            TableViewUtils.setWrapTextToColumn(statusColumn);
            borrowingTableView.getColumns().add(statusColumn);
        }
    }

    private void initBorrowedTableView() {
        borrowedTableView.getColumns().clear();
        String[] columns2 = {"ID", "Username", "Title", "Author", "Book ID", "Start Date", "End Date", "Status"};
        int[] prefWidthForBorrowedTable = {20, 70, 150, 100, 50, 100, 100, 100};
        for (int i = 0; i < columns2.length; i++) {
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columns2[i]);
            int finalI = i;
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(finalI)));
            column.setPrefWidth(prefWidthForBorrowedTable[finalI]);
            TableViewUtils.setWrapTextToColumn(column);
            if (columns2[finalI].equals("ID") || columns2[finalI].equals("Book ID")) {
                column.setComparator(Comparator.comparingInt(Integer::parseInt));
            }
            if (i == 2) {
                TableViewUtils.addCoverColumn(borrowedTableView, 4);
            }
            borrowedTableView.getColumns().add(column);
        }
        TableViewUtils.setBookViewRedirection(borrowedTableView, 4);
    }

    public TableView<ObservableList<String>> borrowingTableView = new TableView<>();
    public TableView<ObservableList<String>> borrowedTableView = new TableView<>();
    public BorderPane root;
    public Label totalIssuesLabel;
    public Label currentIssuesLabel;
    public Label overdueLabel;
    public ComboBox<String> pastColumnComboBox = new ComboBox<>();
    public TextField pastSearchTextField;
    public ComboBox<String> currentColumnComboBox = new ComboBox<>();
    public TextField currentSearchTextField;

    public void initialize() throws SQLException {
        totalIssuesLabel.setText(Integer.toString(SQLUtils.getTotalIssuesCount()));
        currentIssuesLabel.setText(Integer.toString(SQLUtils.getCurrentIssuesCount()));
        overdueLabel.setText(Integer.toString(SQLUtils.getOverdueIssuesCount()));

        if (UserManagement.getCurrentUser() instanceof NormalUser) {
            root.setLeft(null);
        }

        this.initComboBox();

        initBorrowingTableView();
        initBorrowedTableView();

        ///Search in currentIssuesTable
        ObservableList<ObservableList<String>> currentList = SQLUtils.getCurrentIssuesList();
        borrowingTableView.setItems(currentList);
        TableViewUtils.setSearchingFeature(currentList, currentSearchTextField, currentColumnComboBox, borrowingTableView);


        ///Search in pastIssuesTable
        ObservableList<ObservableList<String>> pastList = SQLUtils.getPastIssuesList();
        borrowedTableView.setItems(pastList);
        TableViewUtils.setSearchingFeature(pastList, pastSearchTextField, pastColumnComboBox, borrowedTableView);
    }
}