package org.proview.test.Scene;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.proview.modal.User.Admin;
import org.proview.modal.User.UserManagement;
import org.proview.test.AppMain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IssueListView {
    //public TableView<Issue> issueTable;
    public TableView<ObservableList<String>> borrowingTableView = new TableView<>();
    public TableView<ObservableList<String>> borrowedTableView = new TableView<>();

    public void initialize() throws SQLException {

        borrowingTableView.getColumns().clear();
        String[] columns1 = {"ID", "Username", "Title", "Author", "Book ID", "Due Date", "Remaining time"};
        for (int i = 0; i < columns1.length; i++) {
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columns1[i]);
            int finalI = i;
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(finalI)));
            borrowingTableView.getColumns().add(column);
        }


        borrowedTableView.getColumns().clear();
        String[] columns2 = {"ID", "Username", "Title", "Author", "Book ID", "Start Date", "End Date"};
        for (int i = 0; i < columns2.length; i++) {
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columns2[i]);
            int finalI = i;
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(finalI)));
            borrowedTableView.getColumns().add(column);
        }

        ObservableList<ObservableList<String>> datas1 = FXCollections.observableArrayList();
        ObservableList<ObservableList<String>> datas2 = FXCollections.observableArrayList();
        Connection connection = AppMain.connection;
        if (UserManagement.getCurrentUser() instanceof Admin) {
            PreparedStatement borrowingPS = connection.prepareStatement(
                    "WITH book_issue AS " +
                            "(SELECT book.id AS BookID, book.name AS BookName, book.author AS Author, issue.username, issue.id AS ID, issue.start_date, issue.duration  FROM book  INNER JOIN issue ON book.id = issue.book_id )  " +
                            "SELECT ID, username, BookID, BookName ,Author AS author,  DATE_ADD(start_date, INTERVAL duration DAY) AS end_date, DATEDIFF(DATE_ADD(start_date, INTERVAL duration DAY), NOW()) AS Remaining_time  " +
                            "FROM book_issue  WHERE DATEDIFF(NOW(), start_date) < duration;"
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
                datas1.add(FXCollections.observableArrayList(id, username, title, author, bookId, end_date, remaining_time));
            }
            borrowingTableView.setItems(datas1);

            PreparedStatement borrowedPS = connection.prepareStatement("WITH book_issue AS " +
                    "(SELECT book.id AS BookID, book.name AS BookName, book.author AS Author, issue.id AS ID, issue.username, issue.start_date, issue.duration  FROM book  INNER JOIN issue ON book.id = issue.book_id )  " +
                    "SELECT ID, username, BookID, BookName, Author AS author,  start_date, DATE_ADD(start_date, INTERVAL duration DAY) AS end_date " +
                    "FROM book_issue WHERE DATEDIFF(NOW(), DATE_ADD(start_date, INTERVAL duration DAY)) > 0");
            ResultSet borrowedRS = borrowedPS.executeQuery();
            while (borrowedRS.next()) {
                String id = Integer.toString(borrowingRS.getInt("id"));
                String bookId = Integer.toString(borrowingRS.getInt("bookid"));
                String username = borrowedRS.getString("username");
                String title = borrowingRS.getString("bookname");
                String author = borrowingRS.getString("author");
                String end_date = borrowingRS.getDate("end_date").toString();
                String start_date = Integer.toString(borrowingRS.getInt("start_date"));
                datas2.add(FXCollections.observableArrayList(id, username, title, author, bookId, start_date, end_date));
            }
            borrowedTableView.setItems(datas2);
        } else {
            PreparedStatement borrowingPS = connection.prepareStatement(
                    "WITH book_issue AS " +
                            "(SELECT book.id AS BookID, book.name AS BookName, book.author AS Author, issue.username, issue.id AS ID, issue.start_date, issue.duration  FROM book  INNER JOIN issue ON book.id = issue.book_id )  " +
                            "SELECT ID, username, BookID, BookName ,Author AS author,  DATE_ADD(start_date, INTERVAL duration DAY) AS end_date, DATEDIFF(DATE_ADD(start_date, INTERVAL duration DAY), NOW()) AS Remaining_time  " +
                            "FROM book_issue  WHERE DATEDIFF(NOW(), start_date) < duration AND username = ?"
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
                datas1.add(FXCollections.observableArrayList(id, username, title, author, bookId, end_date, remaining_time));
            }
            borrowingTableView.setItems(datas1);

            PreparedStatement borrowedPS = connection.prepareStatement("WITH book_issue AS " +
                    "(SELECT book.id AS BookID, book.name AS BookName, book.author AS Author, issue.id AS ID, issue.username, issue.start_date, issue.duration  FROM book  INNER JOIN issue ON book.id = issue.book_id )  " +
                    "SELECT ID, username, BookID, BookName, Author AS author,  start_date, DATE_ADD(start_date, INTERVAL duration DAY) AS end_date " +
                    "FROM book_issue WHERE DATEDIFF(NOW(), DATE_ADD(start_date, INTERVAL duration DAY)) > 0 AND username = ?");
            borrowedPS.setString(1, UserManagement.getCurrentUser().getUsername());
            ResultSet borrowedRS = borrowedPS.executeQuery();
            while (borrowedRS.next()) {
                String id = Integer.toString(borrowingRS.getInt("id"));
                String bookId = Integer.toString(borrowingRS.getInt("bookid"));
                String username = borrowedRS.getString("username");
                String title = borrowingRS.getString("bookname");
                String author = borrowingRS.getString("author");
                String end_date = borrowingRS.getDate("end_date").toString();
                String start_date = Integer.toString(borrowingRS.getInt("start_date"));
                datas2.add(FXCollections.observableArrayList(id, username, title, author, bookId, start_date, end_date));
            }
            borrowedTableView.setItems(datas2);
        }




        /*TableColumn<ObservableList<String>, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));

        TableColumn<ObservableList<String>, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));

        TableColumn<ObservableList<String>, String> bookIdColumn = new TableColumn<>("Book ID");
        bookIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));

        TableColumn<ObservableList<String>, String> startDateColumn = new TableColumn<>("Start Date");
        startDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));

        TableColumn<ObservableList<String>, String> durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(4)));

        TableColumn<ObservableList<String>, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(5)));

        // Thêm các cột vào TableView
        borrowingTableView.getColumns().addAll(idColumn, titleColumn, bookIdColumn, startDateColumn, durationColumn, usernameColumn);

        // Tạo dữ liệu (Dữ liệu là ObservableList các ObservableList đơn giản)
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

        // Thêm dữ liệu vào TableView
        Connection connection = AppMain.connection;
        if (UserManagement.getCurrentUser() instanceof Admin) {
            String sql = "SELECT * FROM issue WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, UserManagement.getCurrentUser().getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int bookId = resultSet.getInt("book_id");
                String startDate;
                if (resultSet.getDate("start_date") != null) {
                    startDate = resultSet.getDate("start_date").toString();
                } else startDate = "null";
                String duration = Integer.toString(resultSet.getInt("duration"));
                String bookName = "";
                PreparedStatement bookNamePS = connection.prepareStatement("SELECT name FROM book WHERE id = ?");
                bookNamePS.setInt(1, bookId);
                ResultSet bookNameRS = bookNamePS.executeQuery();
                while (bookNameRS.next()) {
                    bookName = bookNameRS.getString("name");
                    System.out.println(bookName);
                }
                data.add(FXCollections.observableArrayList(Integer.toString(id), bookName, Integer.toString(bookId), startDate, duration, UserManagement.getCurrentUser().getUsername()));

            }

            // Đặt dữ liệu vào TableView
            issueTable.setItems(data);
        } else {
            String sql = "SELECT * FROM issue";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int bookId = resultSet.getInt("book_id");
                String startDate;
                if (resultSet.getDate("start_date") != null) {
                    startDate = resultSet.getDate("start_date").toString();
                } else startDate = "null";
                String duration = Integer.toString(resultSet.getInt("duration"));
                String bookName = "";
                PreparedStatement bookNamePS = connection.prepareStatement("SELECT name FROM book WHERE id = ?");
                bookNamePS.setInt(1, bookId);
                ResultSet bookNameRS = bookNamePS.executeQuery();
                while (bookNameRS.next()) {
                    bookName = bookNameRS.getString("name");
                    System.out.println(bookName);
                }
                String username = resultSet.getString("username");
                data.add(FXCollections.observableArrayList(Integer.toString(id), bookName, Integer.toString(bookId), startDate, duration, username));

            }

            // Đặt dữ liệu vào TableView
            issueTable.setItems(data);
        }*/
    }
}