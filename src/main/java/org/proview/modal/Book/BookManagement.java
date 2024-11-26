package org.proview.modal.Book;

import java.io.IOException;
import java.sql.*;
import java.util.Comparator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.proview.api.GoogleBooksAPI;
import org.proview.test.AppMain;
import org.proview.test.Container.BookCellCardView;
import org.proview.test.Container.CellView;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.proview.test.Scene.SearchResultView;
import org.proview.utils.SearchUtils;

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

    @Deprecated
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
    
    @Deprecated
    public static ObservableList<BookLib> getOldBookList() throws SQLException {
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

    @Deprecated
    public static ObservableList<String> getBookListView() throws SQLException {
        ObservableList<BookLib> currentBookList = null;
        ObservableList<String> bookStringList = FXCollections.observableArrayList();
        try {
            currentBookList = BookManagement.getOldBookList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (var d : currentBookList) {
            String newbookItem = d.getId() + ". " + d.getTitle() + " - " + d.getAuthor();
            bookStringList.add(newbookItem);
        }

        return bookStringList;
    }

    public static ObservableList<BookLib> getBookList() throws SQLException {
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

            BookLib curBook = new BookLib(
                    id,
                    title,
                    author,
                    description,
                    copies
            );
            bookCellObservableList.add(curBook);
        }

        return bookCellObservableList;
    }

    public static ObservableList<BookLib> getTopRatedBookList() throws SQLException {
        ObservableList<BookLib> bookCellObservableList = getBookList();

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


    public static ObservableList<BookLib> getTrendingBookList() throws SQLException {
        ObservableList<BookLib> bookCellObservableList = getBookList();

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

    public static ObservableList<BookGoogle> getGoogleBookList(String query) throws SQLException, IOException {
        String jsonResponse = GoogleBooksAPI.getBooksFromAPI(query);
        if (jsonResponse == null) {
            System.out.println("Uh oh~");
            return null;
        }

        ObservableList<BookGoogle> bookList = FXCollections.observableArrayList();

        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        JsonArray items = jsonObject.getAsJsonArray("items");
        if (items != null) {
            for (var item : items) {
                JsonObject volumeInfo = item.getAsJsonObject().getAsJsonObject("volumeInfo");

                // Book details
                String title = volumeInfo.get("title").getAsString();
                String authors = volumeInfo.has("authors")
                        ? volumeInfo.getAsJsonArray("authors").toString()
                        .replace("[", "")
                        .replace("]", "")
                        .replace("\"", "")
                        : "Unknown";
                String description = volumeInfo.has("description") ? volumeInfo.get("description").getAsString() : "No description available";
                String tags = volumeInfo.has("categories")
                        ? volumeInfo.getAsJsonArray("categories").toString()
                        .replace("[", "")
                        .replace("]", "")
                        .replace("\"", "")
                        : "Unknown";

                // Cover image URL
                String coverImageUrl = "";
                if (volumeInfo.has("imageLinks")) {
                    JsonObject imageLinks = volumeInfo.getAsJsonObject("imageLinks");
                    coverImageUrl = imageLinks.get("thumbnail").getAsString();
                }

                bookList.add(new BookGoogle(title, authors, description, coverImageUrl, tags));
            }
        }
        return bookList;
    }

    public static <T> void initBookList(VBox bookListVBox, ObservableList<T> bookList, Boolean cardView, Boolean showCopiesAvailable, Boolean showRanking) {
        bookListVBox.getChildren().clear();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                int index = 0;
                for (T item : bookList) {
                    index++;
                    try {
                        FXMLLoader loader;
                        if (cardView) {
                            loader = new FXMLLoader(AppMain.class.getResource("BookCellCardView.fxml"));
                        } else {
                            loader = new FXMLLoader(AppMain.class.getResource("BookCellCompactView.fxml"));
                        }
                        Button button = loader.load();

                        CellView cellView = loader.getController();

                        if (item instanceof BookLib bookLib) {
                            cellView.setData(
                                    bookLib.getId(),
                                    showRanking ? "#" + index + ". " + bookLib.getTitle() : bookLib.getTitle(),
                                    bookLib.getAuthor(),
                                    bookLib.getTags(),
                                    bookLib.getRating(),
                                    bookLib.getIssueCount(),
                                    showCopiesAvailable ? bookLib.getCopiesAvailable() : -1
                            );
                        } else {
                            BookGoogle bookGoogle = (BookGoogle) item;
                            cellView.setData(
                                    bookGoogle.getTitle(),
                                    bookGoogle.getAuthor(),
                                    bookGoogle.getCoverImageUrl(),
                                    bookGoogle.getTags()
                            );
                        }

                        // Add the button to the VBox on the JavaFX Application Thread
                        Platform.runLater(() -> bookListVBox.getChildren().add(button));
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
