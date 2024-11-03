package org.proview.model;

import java.sql.*;
import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import org.proview.test.AppMain;
import org.proview.test.BookCellView;

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

    public static BookLib getBook(int id) throws SQLException {
        Statement statement = AppMain.connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM book WHERE id = " + id);

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String author = resultSet.getString("author");
            return new BookLib(id, name, author);
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

    public static ObservableList<BookLib> getBookList() throws SQLException {
        ObservableList<BookLib> books = FXCollections.observableArrayList();
        Connection connection = AppMain.connection;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM book");
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String author = resultSet.getString("author");
            BookLib curBook = new BookLib(id, name, author);
            books.add(curBook);
        }
        return books;
    }

    public static ObservableList<String> getBookListView() throws SQLException {
        ObservableList<BookLib> currentBookList = null;
        ObservableList<String> bookStringList = FXCollections.observableArrayList();
        try {
            currentBookList = BookManagement.getBookList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (var d : currentBookList) {
            String newbookItem = d.getId() + ". " + d.getTitle() + " - " + d.getAuthor();
            bookStringList.add(newbookItem);
        }

        return bookStringList;
    }

    public static ObservableList<BookLib> getBookCellList() throws SQLException {
        ObservableList<BookLib> bookCellObservableList = FXCollections.observableArrayList();

        Connection connection = AppMain.connection;
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT * FROM book");

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String title = resultSet.getString("name");
            String author = resultSet.getString("author");
            String description = resultSet.getString("description");
            int copies = resultSet.getInt("copies");

            BookLib curBookCell = new BookLib(
                    id,
                    title,
                    author,
                    description,
                    "./assets/covers/cover" + id + ".png",
                    copies);
            bookCellObservableList.add(curBookCell);
        }

        return bookCellObservableList;
    }

    public static ObservableList<BookLib> getTopRatedBookCellList() throws SQLException {
        ObservableList<BookLib> bookCellObservableList = getBookCellList();

        // Sort by rating descending
        bookCellObservableList.sort(Comparator.comparingDouble((BookLib a) -> {
            try {
                return a.getRating();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).reversed());

        return bookCellObservableList;
    }


    public static ObservableList<BookLib> getTrendingBookCellList() throws SQLException {
        ObservableList<BookLib> bookCellObservableList = getBookCellList();

        // Sort by trend descending
        bookCellObservableList.sort(Comparator.comparingInt((BookLib a) -> {
            try {
                return a.getIssueCount7Days();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).reversed());

        return bookCellObservableList;
    }

    public static void initLibBookList(ListView<BookLib> bookListView, ObservableList<BookLib> bookList) {
        bookListView.setItems(bookList);
        bookListView.setCellFactory(param -> new ListCell<>() {
            {
                setStyle("-fx-padding: 0px; -fx-margin: 0px; -fx-background-insets: 0px; -fx-border-insets: 0px;");
            }
            @Override
            protected void updateItem(BookLib item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(AppMain.class.getResource("BookCellView.fxml"));
                        HBox hbox = loader.load();

                        // Get the controller of the cell
                        BookCellView cellView = loader.getController();

                        cellView.setData(
                                item.getId(),
                                "#" + (getIndex() + 1) + ". " + item.getTitle(),
                                item.getAuthor(),
                                item.getTags(),
                                item.getRating(),
                                item.getIssueCount(),
                                item.getImagePath(),
                                item.getCopiesAvailable()
                        );

                        setGraphic(hbox);
                    } catch (Exception e) {
                        System.out.println(e);
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public static void initGoogleBookList(ListView<BookLib> bookListView, ObservableList<BookLib> bookList) {
        bookListView.setItems(bookList);
        bookListView.setCellFactory(param -> new ListCell<>() {
            {
                setStyle("-fx-padding: 0px; -fx-margin: 0px; -fx-background-insets: 0px; -fx-border-insets: 0px;");
            }
            @Override
            protected void updateItem(BookLib item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(AppMain.class.getResource("BookCellView.fxml"));
                        HBox hbox = loader.load();

                        // Get the controller of the cell
                        BookCellView cellView = loader.getController();

                        cellView.setData(
                                item.getId(),
                                "#" + (getIndex() + 1) + ". " + item.getTitle(),
                                item.getAuthor(),
                                item.getTags(),
                                item.getRating(),
                                item.getIssueCount(),
                                item.getImagePath(),
                                item.getCopiesAvailable()
                        );

                        setGraphic(hbox);
                    } catch (Exception e) {
                        System.out.println(e);
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}
