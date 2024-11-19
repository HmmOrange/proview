package org.proview.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.proview.modal.Book.BookLib;
import org.proview.modal.Review.Review;
import org.proview.modal.User.Admin;
import org.proview.modal.User.NormalUser;
import org.proview.modal.User.User;
import org.proview.test.AppMain;
import org.proview.test.Scene.ProfileView;

import java.sql.*;
import java.util.Objects;

public class SQLUtils {
    static Connection connection = AppMain.connection;

    public static void addComment(int book_id, int user_id, String review) throws SQLException {
        String sql = "INSERT INTO review(book_id, user_id, review, time_added) VALUES (?, ?, ?, NOW());";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, book_id);
        preparedStatement.setInt(2, user_id);
        preparedStatement.setString(3, review);
        preparedStatement.executeUpdate();
    }

    public static User getUser(int id) throws SQLException {
        String sql = "SELECT * FROM user WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.next()) {
            return null;
        } else {
            resultSet.isFirst();
            String username = resultSet.getString("username");
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            String email = resultSet.getString("email");

            if (username.equalsIgnoreCase("admin"))
                return new Admin(id, firstName, lastName, email);

            return new NormalUser(id, firstName, lastName, email);
        }
    }

    public static User getUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.next()) {
            return null;
        } else {
            resultSet.isFirst();
            int id = resultSet.getInt("id");
            String firstName = resultSet.getString("firstName");
            String lastName = resultSet.getString("lastName");
            String email = resultSet.getString("email");
            boolean cardView = resultSet.getBoolean("card_view");
            System.out.println(id + " " + firstName + " " + lastName + " " + email);

            if (username.equalsIgnoreCase("admin"))
                return new Admin(id, username, password, firstName, lastName, email, cardView);

            return new NormalUser(id, username, password, firstName, lastName, email, cardView);
        }
    }

    public static BookLib getBook(int id) throws SQLException {
        String sql = "SELECT * FROM book WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            String title = resultSet.getString("name");
            String author = resultSet.getString("author");
            String description = resultSet.getString("description");
            int copiesAvailable = resultSet.getInt("copies");

            return new BookLib(id, title, author, description, copiesAvailable);
        }

        return null;
    }

    public static ObservableList<Review> getReviewList(int userId) throws SQLException {
        Connection connection = AppMain.connection;
        String sql = """
            SELECT * FROM review
            WHERE user_id = ?
            ORDER BY time_added DESC;
        """;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();

        ObservableList<Review> reviewObservableList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            int bookId = resultSet.getInt("book_id");
            String review = resultSet.getString("review");
            Timestamp timestampAdded = resultSet.getTimestamp("time_added");

            Review curReview = new Review(
                    bookId,
                    userId,
                    review,
                    timestampAdded
            );
            reviewObservableList.add(curReview);
        }

        return reviewObservableList;
    }

    public static ObservableList<Review> getReviewList() throws SQLException {
        Connection connection = AppMain.connection;
        String sql = """
            SELECT * FROM review
            ORDER BY time_added DESC;
        """;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        ObservableList<Review> reviewObservableList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            int bookId = resultSet.getInt("book_id");
            int userId = resultSet.getInt("user_id");
            String review = resultSet.getString("review");
            Timestamp timestampAdded = resultSet.getTimestamp("time_added");

            Review curReview = new Review(
                    bookId,
                    userId,
                    review,
                    timestampAdded
            );
            reviewObservableList.add(curReview);
        }
        return reviewObservableList;
    }

    public static ObservableList<BookLib> getBorrowingBookList(int userId) throws SQLException {
        Connection connection = AppMain.connection;
        String sql = """
            SELECT * FROM book b
            JOIN issue i ON b.id = i.book_id
            WHERE i.user_id = ? AND NOT (i.status = 'Returned');
        """;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        ObservableList<BookLib> bookLibObservableList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            int bookId = resultSet.getInt("b.id");
            String title = resultSet.getString("name");
            String author = resultSet.getString("author");
            String description = resultSet.getString("description");
            int copiesAvailable = resultSet.getInt("copies");

            bookLibObservableList.add(new BookLib(bookId, title, author, description, copiesAvailable));
        }
        return bookLibObservableList;
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

    public static int getUserIdFrom(String username) throws SQLException {
        int res = -1;
        PreparedStatement preparedStatement = AppMain.connection.prepareStatement("SELECT id FROM user WHERE username = ?");
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            res = resultSet.getInt("id");
        }
        preparedStatement.close();
        resultSet.close();
        return res;
    }

    public static ObservableList<BookLib> getOverdueBookList(int userId) throws SQLException {
        Connection connection = AppMain.connection;
        String sql = """
            SELECT * FROM book b
            JOIN issue i ON b.id = i.book_id
            WHERE i.user_id = ? AND DATEDIFF(NOW(), DATE_ADD(start_date, INTERVAL duration DAY)) > 0 
            AND NOT (status = 'Returned');
        """;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        ObservableList<BookLib> bookLibObservableList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            int bookId = resultSet.getInt("b.id");
            String title = resultSet.getString("name");
            String author = resultSet.getString("author");
            String description = resultSet.getString("description");
            int copiesAvailable = resultSet.getInt("copies");

            bookLibObservableList.add(new BookLib(bookId, title, author, description, copiesAvailable));
        }
        return bookLibObservableList;
    }

    public static ObservableList<BookLib> getPastIssuesBookList(int userId) throws SQLException {
        Connection connection = AppMain.connection;
        String sql = """
            SELECT * FROM book b
            JOIN issue i ON b.id = i.book_id
            WHERE i.user_id = ? AND status = 'Returned';
        """;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        ObservableList<BookLib> bookLibObservableList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            int bookId = resultSet.getInt("b.id");
            String title = resultSet.getString("name");
            String author = resultSet.getString("author");
            String description = resultSet.getString("description");
            int copiesAvailable = resultSet.getInt("copies");

            bookLibObservableList.add(new BookLib(bookId, title, author, description, copiesAvailable));
        }
        return bookLibObservableList;
    }

    public static void addFavourite(int userId, int bookId) throws SQLException {
        String sql = "INSERT INTO favourite(user_id, book_id, time_added) VALUES (?, ?, CURRENT_TIMESTAMP());";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, bookId);
        preparedStatement.executeUpdate();
        ProfileView.loadFavouriteBookList();
    }

    public static void removeFavourite(int userId, int bookId) throws SQLException {
        String sql = "DELETE FROM favourite WHERE user_id = ? AND book_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, bookId);
        preparedStatement.executeUpdate();
        ProfileView.loadFavouriteBookList();
    }

    public static ObservableList<BookLib> getFavouriteBookList(int userId) throws SQLException {
        Connection connection = AppMain.connection;
        String sql = """
            SELECT * FROM book b
            JOIN favourite f ON b.id = f.book_id
            WHERE user_id = ?;
        """;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        ObservableList<BookLib> bookLibObservableList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            int bookId = resultSet.getInt("b.id");
            String title = resultSet.getString("name");
            String author = resultSet.getString("author");
            String description = resultSet.getString("description");
            int copiesAvailable = resultSet.getInt("copies");

            bookLibObservableList.add(new BookLib(bookId, title, author, description, copiesAvailable));
        }
        return bookLibObservableList;
    }

    public static boolean isFavouriteBook(int user_id, int book_id) throws SQLException {
        boolean res = false;
        String sql = """
                SELECT * FROM favourite
                WHERE book_id = ? AND user_id = ?
                """;
        PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
        preparedStatement.setInt(1, book_id);
        preparedStatement.setInt(2, user_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            res = true;
        }
        preparedStatement.close();
        resultSet.close();
        return res;
    }

    public static boolean ifUserBorrowingBook(int user_id, int book_id) throws SQLException {
        boolean res = false;
        String sql = """
                SELECT * FROM issue
                WHERE user_id = ? AND book_id = ? AND end_date IS NULL;
                """;
        PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
        preparedStatement.setInt(1, user_id);
        preparedStatement.setInt(2, book_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            res = true;
        }
        preparedStatement.close();
        resultSet.close();
        System.out.println("You are borrowing this book " + res);
        return res;
    }

    public static boolean ifBookUnavailable(int book_id) throws SQLException {
        boolean res = false;
        String sql = """
                WITH borrowednum AS (
                    SELECT book_id, COUNT(*) AS numofqueries
                    FROM issue
                    WHERE end_date IS NULL
                    GROUP BY book_id
                )
                SELECT
                    book.*,
                    COALESCE(borrowednum.numofqueries, 0) AS numofqueries,
                    COALESCE(borrowednum.book_id, book.id) AS book_id
                FROM book
                LEFT JOIN borrowednum
                    ON book.id = borrowednum.book_id
                WHERE book.id = ?;
                """;
        PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
        preparedStatement.setInt(1, book_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int copies = resultSet.getInt("copies");
            int numOfQueries = resultSet.getInt("numofqueries");
            if (copies - numOfQueries <= 0) res = true;
        }
        preparedStatement.close();
        resultSet.close();
        System.out.println("book id " + book_id + " unavailable is " + res);
        return res;
    }

    public static void setUserPreferredView(int userId, boolean cardView) throws SQLException {
        String sql = "UPDATE user SET card_view = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setBoolean(1, cardView);
        preparedStatement.setInt(2, userId);
        preparedStatement.executeUpdate();
    }

    public static int getRating(int userId, int bookId) throws SQLException {
        String sql = """
                SELECT * FROM rating
                WHERE user_id = ? AND book_id = ?;
                """;
        PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, bookId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("rating");
        }
        return 0;
    }

    public static void setRating(int userId, int bookId, int curRating) throws SQLException {
        String sql = """
        INSERT INTO rating (user_id, book_id, rating, time_added)
        VALUES (?, ?, ?, CURRENT_TIMESTAMP)
        ON DUPLICATE KEY UPDATE
            rating = VALUES(rating),
            time_added = CURRENT_TIMESTAMP;
        """;
        PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, bookId);
        preparedStatement.setInt(3, curRating);
        preparedStatement.executeUpdate();
    }
}

