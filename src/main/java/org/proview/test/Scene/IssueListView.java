package org.proview.test.Scene;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import org.proview.modal.User.Admin;
import org.proview.modal.User.UserManagement;
import org.proview.test.AppMain;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Objects;

public class IssueListView {
    public TableView<ObservableList<String>> borrowingTableView = new TableView<>();
    public TableView<ObservableList<String>> borrowedTableView = new TableView<>();

    public void initialize() throws SQLException {

        borrowingTableView.getColumns().clear();
        String[] columns1 = {"ID", "Username", "Title", "Author", "Book ID", "Due Date", "Remaining time", "Status"};
        for (int i = 0; i < columns1.length - 1; i++) {
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columns1[i]);
            int finalI = i;
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(finalI)));
            if (columns1[finalI].equals("ID") || columns1[finalI].equals("Book ID")) {
                column.setComparator(Comparator.comparingInt(Integer::parseInt));
            }
            borrowingTableView.getColumns().add(column);
        }

        ///xử lý trạng thái sách
        if (UserManagement.getCurrentUser() instanceof Admin) {
            TableColumn<ObservableList<String>, String> statusColumn = new TableColumn<>(columns1[columns1.length - 1]);
            statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columns1.length - 1)));
            statusColumn.setCellFactory(column -> new TableCell<>() {
                private final ComboBox<String> comboBox = new ComboBox<>();
                private final Button confirmButton = new Button("Confirm");
                private final HBox hBox = new HBox(5, comboBox, confirmButton); // HBox chứa ComboBox và Confirm

                {
                    comboBox.getItems().addAll("Borrowing", "Returned", "Missing");
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
                                alterStatusInDatabase(rowData, newStatus);
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
            statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columns1.length - 1)));
            borrowingTableView.getColumns().add(statusColumn);
        }


        borrowedTableView.getColumns().clear();
        String[] columns2 = {"ID", "Username", "Title", "Author", "Book ID", "Start Date", "End Date", "Status"};
        for (int i = 0; i < columns2.length; i++) {
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columns2[i]);
            int finalI = i;
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(finalI)));
            if (columns2[finalI].equals("ID") || columns2[finalI].equals("Book ID")) {
                column.setComparator(Comparator.comparingInt(Integer::parseInt));
            }
            borrowedTableView.getColumns().add(column);
        }


        ///thêm các data vào bảng
        ObservableList<ObservableList<String>> datas1 = FXCollections.observableArrayList();
        ObservableList<ObservableList<String>> datas2 = FXCollections.observableArrayList();
        Connection connection = AppMain.connection;
        if (UserManagement.getCurrentUser() instanceof Admin) {
            PreparedStatement borrowingPS = connection.prepareStatement(
                    "WITH book_issue AS " +
                            "(SELECT book.id AS BookID, book.name AS BookName, book.author AS Author, issue.username, issue.id AS ID, issue.start_date, issue.duration, issue.status  FROM book  INNER JOIN issue ON book.id = issue.book_id )  " +
                            "SELECT ID, username, BookID, BookName ,Author AS author,  DATE_ADD(start_date, INTERVAL duration DAY) AS end_date, DATEDIFF(DATE_ADD(start_date, INTERVAL duration DAY), NOW()) AS Remaining_time, status  " +
                            "FROM book_issue  WHERE status = 'Borrowing' OR status = 'Missing';"
            );
            ResultSet borrowingRS = borrowingPS.executeQuery();
            while(borrowingRS.next()) {
                String id = Integer.toString(borrowingRS.getInt("id"));
                String username = borrowingRS.getString("username");
                String bookId = Integer.toString(borrowingRS.getInt("bookid"));
                String title = borrowingRS.getString("bookname");
                String author = borrowingRS.getString("author");
                String end_date = borrowingRS.getDate("end_date").toString();
                String remaining_time = Integer.toString(borrowingRS.getInt("remaining_time"));
                String status = borrowingRS.getString("status");
                datas1.add(FXCollections.observableArrayList(id, username, title, author, bookId, end_date, remaining_time, status));
            }
            borrowingTableView.setItems(datas1);
            borrowingPS.close();

            PreparedStatement borrowedPS = connection.prepareStatement("WITH book_issue AS " +
                    "(SELECT book.id AS BookID, book.name AS BookName, book.author AS Author, issue.id AS ID, issue.username, issue.start_date, issue.duration, issue.status, issue.end_date  " +
                    "FROM book  INNER JOIN issue ON book.id = issue.book_id )  " +
                    "SELECT ID, username, BookID, BookName, Author AS author,  start_date, end_date, status " +
                    "FROM book_issue WHERE status = 'Returned'");
            ResultSet borrowedRS = borrowedPS.executeQuery();
            while (borrowedRS.next()) {
                String id = Integer.toString(borrowedRS.getInt("id"));
                String bookId = Integer.toString(borrowedRS.getInt("bookid"));
                String username = borrowedRS.getString("username");
                String title = borrowedRS.getString("bookname");
                String author = borrowedRS.getString("author");
                String end_date = borrowedRS.getDate("end_date").toString();
                String start_date = borrowedRS.getDate("start_date").toString();
                String status = borrowedRS.getString("status");
                datas2.add(FXCollections.observableArrayList(id, username, title, author, bookId, start_date, end_date, status));
            }
            borrowedTableView.setItems(datas2);
            borrowedPS.close();
        } else {
            PreparedStatement borrowingPS = connection.prepareStatement(
                    "WITH book_issue AS " +
                            "(SELECT book.id AS BookID, book.name AS BookName, book.author AS Author, issue.username, issue.id AS ID, issue.start_date, issue.duration, issue.status  FROM book  INNER JOIN issue ON book.id = issue.book_id )  " +
                            "SELECT ID, username, BookID, BookName ,Author AS author,  DATE_ADD(start_date, INTERVAL duration DAY) AS end_date, DATEDIFF(DATE_ADD(start_date, INTERVAL duration DAY), NOW()) AS Remaining_time, status  " +
                            "FROM book_issue  WHERE (status = 'Borrowing' OR status = 'Missing') AND username = ?"
            );
            borrowingPS.setString(1, UserManagement.getCurrentUser().getUsername());
            ResultSet borrowingRS = borrowingPS.executeQuery();
            while(borrowingRS.next()) {
                String id = Integer.toString(borrowingRS.getInt("id"));
                String username = borrowingRS.getString("username");
                String bookId = Integer.toString(borrowingRS.getInt("bookid"));
                String title = borrowingRS.getString("bookname");
                String author = borrowingRS.getString("author");
                String end_date = borrowingRS.getDate("end_date").toString();
                String remaining_time = Integer.toString(borrowingRS.getInt("remaining_time"));
                String status = borrowingRS.getString("status");
                datas1.add(FXCollections.observableArrayList(id, username, title, author, bookId, end_date, remaining_time, status));
            }
            borrowingTableView.setItems(datas1);
            borrowingPS.close();

            PreparedStatement borrowedPS = connection.prepareStatement("WITH book_issue AS " +
                    "(SELECT book.id AS BookID, book.name AS BookName, book.author AS Author, issue.id AS ID, issue.username, issue.start_date, issue.duration, issue.status, issue.end_date  " +
                    "FROM book  INNER JOIN issue ON book.id = issue.book_id )  " +
                    "SELECT ID, username, BookID, BookName, Author AS author,  start_date, end_date, status " +
                    "FROM book_issue WHERE (status = 'Returned') AND username = ?");
            borrowedPS.setString(1, UserManagement.getCurrentUser().getUsername());
            ResultSet borrowedRS = borrowedPS.executeQuery();
            while (borrowedRS.next()) {
                String id = Integer.toString(borrowedRS.getInt("id"));
                String bookId = Integer.toString(borrowedRS.getInt("bookid"));
                String username = borrowedRS.getString("username");
                String title = borrowedRS.getString("bookname");
                String author = borrowedRS.getString("author");
                String end_date = borrowedRS.getDate("end_date").toString();
                String start_date = borrowedRS.getDate("start_date").toString();
                String status = borrowedRS.getString("status");
                datas2.add(FXCollections.observableArrayList(id, username, title, author, bookId, start_date, end_date, status));
            }
            borrowedTableView.setItems(datas2);
            borrowedPS.close();
        }
    }

    public static void resetTableAfterChangeStatus() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("IssueListView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }

    public static void alterStatusInDatabase(ObservableList<String> rowData, String newStatus) throws SQLException {
        int issueId = Integer.parseInt(rowData.getFirst());
        String sql = "UPDATE issue SET status = ? WHERE id = ?";
        Connection connection = AppMain.connection;
        PreparedStatement alterStatusPS = connection.prepareStatement(sql);
        alterStatusPS.setString(1, newStatus);
        alterStatusPS.setInt(2, issueId);
        alterStatusPS.executeUpdate();
        alterStatusPS.close();

        if (Objects.equals(newStatus, "Returned")) {
            String endDateSql = "UPDATE issue SET end_date = CURRENT_TIMESTAMP WHERE id = ?";
            PreparedStatement alterEndDatePS = AppMain.connection.prepareStatement(endDateSql);
            alterEndDatePS.setInt(1, issueId);
            alterEndDatePS.executeUpdate();
            alterEndDatePS.close();
        }
    }
}