package org.proview.model;

import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.proview.test.AppMain;

public class BookManagement {
    public static void addBook(String name, String author) throws SQLException {
        Connection connection = AppMain.connection;

        String sql = "INSERT INTO book(name, author, time_added) VALUES (?, ?, CURRENT_TIMESTAMP())";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, author);
        preparedStatement.executeUpdate();
    }

    public static void removeBook(int id) throws SQLException {
        Connection connection = AppMain.connection;
        String sql = "DELETE FROM book WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    public static Book getBook(int id) throws SQLException {
        Statement statement = AppMain.connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM book WHERE id = " + id);

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String author = resultSet.getString("author");
            return new Book(id, name, author);
        }

        return null;
    }

    public static int getBookCount() throws SQLException {
        Statement statement = AppMain.connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM book");
        if (resultSet.next()) {
            return resultSet.getInt("COUNT(*)");
        }
        return 0;
    }

    public static ObservableList<Book> getBookList() throws SQLException {
        ObservableList<Book> books = FXCollections.observableArrayList();
        Connection connection = AppMain.connection;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM book");
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String author = resultSet.getString("author");
            Book curBook = new Book(id, name, author);
            books.add(curBook);
        }
        return books;
    }

    public static ObservableList<String> getBookListView() throws SQLException {
        ObservableList<Book> currentBookList = null;
        ObservableList<String> bookStringList = FXCollections.observableArrayList();
        try {
            currentBookList = BookManagement.getBookList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Book d : currentBookList) {
            String newbookItem = d.getId() + ". " + d.getName() + " - " + d.getAuthor();
            bookStringList.add(newbookItem);
        }

        return bookStringList;
    }
}
