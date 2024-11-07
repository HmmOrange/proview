package org.proview.test;

import com.mysql.cj.xdevapi.Table;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.proview.model.Issue;
import org.proview.model.IssueManagement;
import org.proview.model.User;
import org.proview.model.UserManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IssueListView {
    //public TableView<Issue> issueTable;
    public TableView<ObservableList<String>> issueTable = new TableView<>();

    public void initialize() throws SQLException {
        /*issueTable.getColumns().clear();
        TableColumn<Issue, Integer> idColumn = new TableColumn<> ("id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Issue, Integer> bookIdColumn = new TableColumn<> ("Book ID");
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("Book ID"));

        //TableColumn<Issue, String> bookNameColumn = new TableColumn<>("Book Name");
        //bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("Book Name"));

        //TableColumn<Issue, String> startDateColumn = new TableColumn<>("Start Date");
        //startDateColumn.setCellValueFactory(new PropertyValueFactory<>("Start Date"));

        TableColumn<Issue, Integer> durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("Duration"));


        issueTable.getColumns().add(idColumn);
        issueTable.getColumns().add(bookIdColumn);
        //issueTable.getColumns().add(bookNameColumn);
        //issueTable.getColumns().add(startDateColumn);
        issueTable.getColumns().add(durationColumn);

        issueTable.setItems(IssueManagement.getIssueListFrom(UserManagement.getCurrentUser().getUsername()));*/

        issueTable.getColumns().clear();
        TableColumn<ObservableList<String>, String> idColumn = new TableColumn<>("ID");
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
        issueTable.getColumns().addAll(idColumn, titleColumn, bookIdColumn, startDateColumn, durationColumn, usernameColumn);

        // Tạo dữ liệu (Dữ liệu là ObservableList các ObservableList đơn giản)
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

        // Thêm dữ liệu vào TableView
        Connection connection = AppMain.connection;
        if (UserManagement.getCurrentUser().getType() == 1) {
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
        }
    }
}