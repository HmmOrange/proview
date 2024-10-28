package org.proview.model;

import java.sql.*;
import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.proview.test.AppMain;

public class BookManagement {
    public static void addBook(String name, String author, String description, int copies, String tag) throws SQLException {
        Connection connection = AppMain.connection;

        String sql = """
        INSERT INTO book(name, author, description, time_added, copies)
            VALUES (?, ?, ?, CURRENT_TIMESTAMP(), ?);
        """;
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, author);
        preparedStatement.setString(3, description);
        preparedStatement.setInt(4, copies);
        int affectedRows = preparedStatement.executeUpdate();

        if (affectedRows > 0) {
            System.out.println("Hi");
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);  // Get the first column from the ResultSet
                sql = "INSERT INTO tag(book_id, tag) VALUES (?, ?);";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, tag);
                preparedStatement.executeUpdate();
            }
        }
        else {
            System.out.println("Adding book failed");
        }
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

    public static ObservableList<BookCell> getBookCellList() throws SQLException {
        ObservableList<BookCell> bookCellObservableList = FXCollections.observableArrayList();

        Connection connection = AppMain.connection;
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT * FROM book");

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String title = resultSet.getString("name");
            String author = resultSet.getString("author");
            int copies = resultSet.getInt("copies");

            BookCell curBookCell = new BookCell(
                    id,
                    title,
                    author,
                    "./assets/covers/cover" + id + ".png",
                    copies);
            bookCellObservableList.add(curBookCell);
        }

        return bookCellObservableList;
    }

    public static ObservableList<BookCell> getTopRatedBookCellList() throws SQLException {
        ObservableList<BookCell> bookCellObservableList = getBookCellList();

        // Sort by rating descending
        bookCellObservableList.sort(Comparator.comparingDouble((BookCell a) -> {
            try {
                return a.getRating();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).reversed());

        return bookCellObservableList;
    }


    public static ObservableList<BookCell> getTrendingBookCellList() throws SQLException {
        ObservableList<BookCell> bookCellObservableList = getBookCellList();

        // Sort by trend descending
        bookCellObservableList.sort(Comparator.comparingInt((BookCell a) -> {
            try {
                return a.getIssueCount7Days();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).reversed());

        return bookCellObservableList;
    }
}
