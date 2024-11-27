package org.proview.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.proview.modal.Book.BookLib;
import org.proview.modal.Review.Review;
import org.proview.modal.Tag.Tag;
import org.proview.modal.Tag.TagStyle;
import org.proview.modal.User.Admin;
import org.proview.modal.User.NormalUser;
import org.proview.modal.User.User;
import org.proview.modal.User.UserManagement;
import org.proview.test.AppMain;
import org.proview.test.Scene.ProfileView;

import java.sql.*;
import java.util.*;

public class SQLUtils {
    static Connection connection = AppMain.connection;

    public static void addReview(int book_id, int user_id, String review) throws SQLException {
        String sql = """
        INSERT INTO review(book_id, user_id, review, time_added) VALUES (?, ?, ?, NOW())
        ON DUPLICATE KEY UPDATE
            review = VALUES(review),
            time_added = CURRENT_TIMESTAMP;
        """;
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

    public static Boolean checkUser(String username) throws SQLException {
        String sql = "SELECT * FROM user WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();

        return resultSet.next();
    }

    public static int getLatestID() throws SQLException {
        String sql = "SELECT MAX(id) AS latest_id FROM user";
        PreparedStatement idPS = connection.prepareStatement(sql);
        ResultSet idRS = idPS.executeQuery();
        if (idRS.next()) {
            return idRS.getInt("latest_id");
        }
        return -1;
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
            int copiesAvailable = copiesAvailable(id);

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
                    getRating(userId, bookId),
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
                    getRating(userId, bookId),
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
        String sql = """
                INSERT INTO favourite(user_id, book_id, time_added) VALUES (?, ?, CURRENT_TIMESTAMP())
                ON DUPLICATE KEY UPDATE time_added = CURRENT_TIMESTAMP;
        """;
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

        return resultSet.next();
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
        return res;
    }

    public static int copiesAvailable(int book_id) throws SQLException {
        int res = 0;
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
            res = copies - numOfQueries;
        }
        preparedStatement.close();
        resultSet.close();
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

    public static ObservableList<ObservableList<String>> getBooksData() throws SQLException {
        ObservableList<ObservableList<String>> respond = FXCollections.observableArrayList();
        String sql = """
                WITH avg_rating AS (
                    SELECT book_id, ROUND(AVG(rating), 1) AS average_rating 
                    FROM rating
                    GROUP BY book_id
                ),
                book_rating AS (
                    SELECT book.*, COALESCE(avg_rating.average_rating, 0) AS average_rating FROM book
                    LEFT JOIN avg_rating ON book.id = avg_rating.book_id
                ),
                total_issue AS (
                    SELECT book_id, COUNT(*) AS total
                    FROM issue
                    GROUP BY book_id
                ), 
                bri AS (
                    SELECT book_rating.*, COALESCE(total_issue.total, 0) AS total_issues FROM book_rating
                    LEFT JOIN total_issue ON book_rating.id = total_issue.book_id
                ),
                reviews AS (
                    SELECT book_id, COUNT(*) AS totalreviews FROM review
                    GROUP BY book_id
                ),
                brir AS (
                    SELECT bri.*, COALESCE(totalreviews, 0) AS totalreviews FROM bri
                    LEFT JOIN reviews ON bri.id = reviews.book_id
                ),
                tags AS (
                    SELECT
                        book_id,
                        GROUP_CONCAT(tag ORDER BY tag SEPARATOR ', ') AS concatenated_tags
                    FROM
                        book_tag
                    GROUP BY
                        book_id
                ),
                brirt AS (
                    SELECT brir.*, concatenated_tags FROM brir
                    LEFT JOIN tags ON brir.id = tags.book_id
                )
                SELECT * FROM brirt
                """;
        ResultSet resultSet = AppMain.connection.prepareStatement(sql).executeQuery();
        while (resultSet.next()) {
            String id = String.format("%03d", resultSet.getInt("id"));
            String name = resultSet.getString("name");
            String author = resultSet.getString("author");
            String description = resultSet.getString("description");
            String copies = Integer.toString(resultSet.getInt("copies"));
            String issues = Integer.toString(resultSet.getInt("total_issues"));
            String tags = resultSet.getString("concatenated_tags");
            String reviews = resultSet.getString("totalreviews");
            String rating = resultSet.getString("average_rating");
            respond.add(FXCollections.observableArrayList(id, name, author, tags, description, copies, issues, reviews, rating));
        }
        return respond;
    }

    public static ObservableList<ObservableList<String>> getUsersData() throws SQLException {
        ObservableList<ObservableList<String>> respond = FXCollections.observableArrayList();
        String sql = """
                WITH allissues AS (
                    SELECT user_id, COUNT(*) AS allissuesnum 
                    FROM issue
                    GROUP BY user_id
                ),
                presentissues AS (
                    SELECT user_id, COUNT(*) AS presentissuesnum
                    FROM issue
                    WHERE end_date IS NULL
                    GROUP BY user_id
                ),
                reviews AS (
                    SELECT user_id, COUNT(*) AS reviewsnum
                    FROM review
                    GROUP BY user_id
                ),
                user_all AS (
                    SELECT user.*, COALESCE(allissuesnum, 0) AS allissuesnum FROM user
                    LEFT JOIN allissues ON user.id = allissues.user_id
                ),
                user_all_present AS (
                    SELECT user_all.*, COALESCE(presentissuesnum, 0) AS presentissuesnum FROM user_all
                    LEFT JOIN presentissues ON user_all.id = presentissues.user_id
                ),
                user_all_present_reviews AS (
                    SELECT user_all_present.*, COALESCE(reviewsnum, 0) AS reviewsnum FROM user_all_present
                    LEFT JOIN reviews ON user_all_present.id = reviews.user_id
                )
                SELECT id, username, CONCAT(firstname, ' ', lastname) AS fullname, email, registration_date, presentissuesnum, allissuesnum, 
                reviewsnum 
                FROM user_all_present_reviews
                """;
        PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String id = String.format("%03d", resultSet.getInt("id"));
            String username = resultSet.getString("username");
            String fullname = resultSet.getString("fullname");
            String email = resultSet.getString("email");
            String present = Integer.toString(resultSet.getInt("presentissuesnum"));
            String allissuesnum = Integer.toString(resultSet.getInt("allissuesnum"));
            String reviewsnum = Integer.toString(resultSet.getInt("reviewsnum"));
            String regisDate = resultSet.getTimestamp("registration_date").toString();
            respond.add(FXCollections.observableArrayList(id, username, fullname, email, regisDate, present, allissuesnum, reviewsnum));
        }
        return respond;
    }

    public static int getTotalIssuesCount() throws SQLException {
        int respond = 0;
        ResultSet resultSet;
        if (UserManagement.getCurrentUser() instanceof Admin) {
            String sql = """
                    SELECT COUNT(*) AS total FROM issue
                    """;
            resultSet = AppMain.connection.prepareStatement(sql).executeQuery();
        } else {
            String sql = """
                    SELECT COUNT(*) AS total FROM issue
                    WHERE user_id = ?
                    """;
            PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
            preparedStatement.setInt(1, UserManagement.getCurrentUser().getId());
            resultSet = preparedStatement.executeQuery();
        }
        if (resultSet.next()) {
            respond = resultSet.getInt("total");
        }
        resultSet.close();
        return respond;
    }

    public static int getCurrentIssuesCount() throws SQLException {
        int respond = 0;
        ResultSet resultSet;
        if (UserManagement.getCurrentUser() instanceof Admin) {
            String sql = """
                    SELECT COUNT(*) AS total FROM issue
                    WHERE end_date IS NULL
                    """;
            resultSet = AppMain.connection.prepareStatement(sql).executeQuery();
        } else {
            String sql = """
                    SELECT COUNT(*) AS total FROM issue
                    WHERE user_id = ? AND end_date IS NULL
                    """;
            PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
            preparedStatement.setInt(1, UserManagement.getCurrentUser().getId());
            resultSet = preparedStatement.executeQuery();
        }
        if (resultSet.next()) {
            respond = resultSet.getInt("total");
        }
        resultSet.close();
        return respond;
    }

    public static int getOverdueIssuesCount() throws SQLException {
        int respond = 0;
        ResultSet resultSet;
        if (UserManagement.getCurrentUser() instanceof Admin) {
            String sql = """
                    SELECT COUNT(*) AS total FROM issue
                    WHERE DATEDIFF(NOW(), DATE_ADD(start_date, INTERVAL duration DAY)) > 0
                    AND end_date IS NULL
                    """;
            resultSet = AppMain.connection.prepareStatement(sql).executeQuery();
        } else {
            String sql = """
                    SELECT COUNT(*) AS total FROM issue
                    WHERE user_id = ? AND DATEDIFF(NOW(), DATE_ADD(start_date, INTERVAL duration DAY)) > 0
                    AND end_date IS NULL
                    """;
            PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
            preparedStatement.setInt(1, UserManagement.getCurrentUser().getId());
            resultSet = preparedStatement.executeQuery();
        }
        if (resultSet.next()) {
            respond = resultSet.getInt("total");
        }
        resultSet.close();
        return respond;
    }

    public static List<Integer> getUsersCount() throws SQLException {
        List<Integer> respond = new ArrayList<>();
        String sql = """
                SELECT
                    total.total AS total,
                    today.today AS today,
                    thisweek.thisweek AS thisweek
                FROM
                    (SELECT COUNT(*) AS total FROM user) AS total,
                    (SELECT COUNT(*) AS today FROM user WHERE DATEDIFF(CURRENT_TIMESTAMP, registration_date) < 1) AS today,
                    (SELECT COUNT(*) AS thisweek FROM user WHERE DATEDIFF(CURRENT_TIMESTAMP, registration_date) < 7) AS thisweek;
                """;
        ResultSet resultSet = AppMain.connection.prepareStatement(sql).executeQuery();
        if (resultSet.next()) {
            respond.add(resultSet.getInt("total"));
            respond.add(resultSet.getInt("today"));
            respond.add(resultSet.getInt("thisweek"));
        }
        return respond;
    }

    public static ObservableList<ObservableList<String>> getCurrentIssuesList() throws SQLException {
        ObservableList<ObservableList<String>> respond = FXCollections.observableArrayList();

        if (UserManagement.getCurrentUser() instanceof Admin) {
            PreparedStatement borrowingPS = connection.prepareStatement(
                    "WITH book_issue AS " +
                            "(SELECT book.id AS BookID, book.name AS BookName, book.author AS Author, issue.username, issue.id AS ID, issue.start_date, issue.duration, issue.status  FROM book  INNER JOIN issue ON book.id = issue.book_id )  " +
                            "SELECT ID, username, BookID, BookName ,Author AS author,  DATE_ADD(start_date, INTERVAL duration DAY) AS end_date, DATEDIFF(DATE_ADD(start_date, INTERVAL duration DAY), NOW()) AS Remaining_time, status  " +
                            "FROM book_issue  WHERE NOT (status = 'Returned');"
            );
            ResultSet borrowingRS = borrowingPS.executeQuery();
            while(borrowingRS.next()) {
                String id = String.format("%03d", borrowingRS.getInt("id"));
                String username = borrowingRS.getString("username");
                String bookId = String.format("%03d", borrowingRS.getInt("bookid"));
                String title = borrowingRS.getString("bookname");
                String author = borrowingRS.getString("author");
                String end_date = borrowingRS.getTimestamp("end_date").toString();
                String remaining_time = Integer.toString(borrowingRS.getInt("remaining_time"));
                String status = borrowingRS.getString("status");
                respond.add(FXCollections.observableArrayList(id, username, title, author, bookId, end_date, remaining_time, status));
            }
        } else {
            PreparedStatement borrowingPS = connection.prepareStatement(
                    "WITH book_issue AS " +
                            "(SELECT book.id AS BookID, book.name AS BookName, book.author AS Author, issue.username, issue.id AS ID, issue.start_date, issue.duration, issue.status  FROM book  INNER JOIN issue ON book.id = issue.book_id )  " +
                            "SELECT ID, username, BookID, BookName ,Author AS author,  DATE_ADD(start_date, INTERVAL duration DAY) AS end_date, DATEDIFF(DATE_ADD(start_date, INTERVAL duration DAY), NOW()) AS Remaining_time, status  " +
                            "FROM book_issue  WHERE NOT (status = 'Returned') AND username = ?"
            );
            borrowingPS.setString(1, UserManagement.getCurrentUser().getUsername());
            ResultSet borrowingRS = borrowingPS.executeQuery();
            while(borrowingRS.next()) {
                String id = String.format("%03d", borrowingRS.getInt("id"));
                String username = borrowingRS.getString("username");
                String bookId = String.format("%03d", borrowingRS.getInt("bookid"));
                String title = borrowingRS.getString("bookname");
                String author = borrowingRS.getString("author");
                String end_date = borrowingRS.getTimestamp("end_date").toString();
                String remaining_time = Integer.toString(borrowingRS.getInt("remaining_time"));
                String status = borrowingRS.getString("status");
                respond.add(FXCollections.observableArrayList(id, username, title, author, bookId, end_date, remaining_time, status));
            }
        }
        return respond;
    }

    public static ObservableList<ObservableList<String>> getPastIssuesList() throws SQLException {
        ObservableList<ObservableList<String>> datas2 = FXCollections.observableArrayList();
        if (UserManagement.getCurrentUser() instanceof Admin) {
            PreparedStatement borrowedPS = connection.prepareStatement("WITH book_issue AS " +
                    "(SELECT book.id AS BookID, book.name AS BookName, book.author AS Author, issue.id AS ID, issue.username, issue.start_date, issue.duration, issue.status, issue.end_date  " +
                    "FROM book  INNER JOIN issue ON book.id = issue.book_id )  " +
                    "SELECT ID, username, BookID, BookName, Author AS author,  start_date, end_date, status " +
                    "FROM book_issue WHERE status = 'Returned'");
            ResultSet borrowedRS = borrowedPS.executeQuery();
            while (borrowedRS.next()) {
                String id = String.format("%03d", borrowedRS.getInt("id"));
                String bookId = String.format("%03d", borrowedRS.getInt("bookid"));
                String username = borrowedRS.getString("username");
                String title = borrowedRS.getString("bookname");
                String author = borrowedRS.getString("author");
                String end_date = borrowedRS.getTimestamp("end_date").toString();
                String start_date = borrowedRS.getTimestamp("start_date").toString();
                String status = borrowedRS.getString("status");
                datas2.add(FXCollections.observableArrayList(id, username, title, author, bookId, start_date, end_date, status));
            }
        } else {
            PreparedStatement borrowedPS = connection.prepareStatement("WITH book_issue AS " +
                    "(SELECT book.id AS BookID, book.name AS BookName, book.author AS Author, issue.id AS ID, issue.username, issue.start_date, issue.duration, issue.status, issue.end_date  " +
                    "FROM book  INNER JOIN issue ON book.id = issue.book_id )  " +
                    "SELECT ID, username, BookID, BookName, Author AS author,  start_date, end_date, status " +
                    "FROM book_issue WHERE (status = 'Returned') AND username = ?");
            borrowedPS.setString(1, UserManagement.getCurrentUser().getUsername());
            ResultSet borrowedRS = borrowedPS.executeQuery();
            while (borrowedRS.next()) {
                String id = String.format("%03d", borrowedRS.getInt("id"));
                String bookId = String.format("%03d", borrowedRS.getInt("bookid"));
                String username = borrowedRS.getString("username");
                String title = borrowedRS.getString("bookname");
                String author = borrowedRS.getString("author");
                String end_date = borrowedRS.getTimestamp("end_date").toString();
                String start_date = borrowedRS.getTime("start_date").toString();
                String status = borrowedRS.getString("status");
                datas2.add(FXCollections.observableArrayList(id, username, title, author, bookId, start_date, end_date, status));
            }
        }
        return datas2;
    }

    public static List<Double> getBooksCount() throws SQLException {
        List<Double> respond = new ArrayList<>();
        String sql = """
            SELECT total.total, reviews.reviews
            FROM (
                (SELECT COUNT(*) AS total FROM book) AS total,
                (SELECT COUNT(*) AS reviews FROM review) AS reviews
            );
        """;
        ResultSet resultSet = AppMain.connection.prepareStatement(sql).executeQuery();
        if (resultSet.next()) {
            respond.add(resultSet.getDouble("total"));
            respond.add(resultSet.getDouble("reviews"));
        }
        return respond;
    }

    public static Map<String, TagStyle> getTagMap() throws SQLException {
        String sql = """
            SELECT * FROM tag;
        """;
        ResultSet resultSet = AppMain.connection.prepareStatement(sql).executeQuery();
        Map<String, TagStyle> tagMap = new HashMap<>();
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            String bgColorHex = resultSet.getString("bg_color_hex");
            String textColorHex = resultSet.getString("text_color_hex");

            tagMap.put(name, new TagStyle(bgColorHex, textColorHex));
        }
        return tagMap;
    }

    public static Boolean hasReview(int userId, int bookId) throws SQLException {
        String sql = """
            SELECT * FROM review
            WHERE user_id = ? AND book_id = ?
            LIMIT 1;
        """;
        PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, bookId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    public static void removeReview(int userId, int bookId) throws SQLException {
        String sql = """
            DELETE FROM review
            WHERE user_id = ? AND book_id = ?;
        """;
        PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, bookId);
        preparedStatement.executeUpdate();
    }

    public static String getReview(int userId, int bookId) throws SQLException {
        String sql = """
            SELECT * FROM review
            WHERE user_id = ? AND book_id = ?;
        """;
        PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, bookId);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString("review");
        }
        return null;
    }

    public static ObservableList<Tag> getTagList() throws SQLException {
        String sql = """
            SELECT * FROM tag
        """;
        ResultSet resultSet = AppMain.connection.prepareStatement(sql).executeQuery();
        ObservableList<Tag> tagList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            String bgColorHex = resultSet.getString("bg_color_hex");
            String textColorHex = resultSet.getString("text_color_hex");

            tagList.add(new Tag(name, bgColorHex, textColorHex));
        }
        return tagList;
    }

    public static int getTotalCopiesCount() throws SQLException {
        int respond = 0;
        String sql = """
                SELECT SUM(copies) AS total FROM book;
                """;
        ResultSet resultSet = AppMain.connection.prepareStatement(sql).executeQuery();
        if (resultSet.next()) {
            respond = resultSet.getInt("total");
        }
        return respond;
    }

    public static void addTag(String name, String bgColorHex, String textColorHex) throws SQLException {
        String sql = """
            INSERT INTO tag (name, bg_color_hex, text_color_hex)
            VALUES (?, ?, ?);
        """;
        PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, bgColorHex);
        preparedStatement.setString(3, textColorHex);
        preparedStatement.executeUpdate();
    }

    public static void removeTag(String name) throws SQLException {
        String sql = "DELETE FROM tag WHERE name = ?;";
        PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
        preparedStatement.setString(1, name);
        preparedStatement.executeUpdate();
    }

    public static void updateTag(String name, String bgColorHex, String textColorHex) throws SQLException {
        if (!bgColorHex.startsWith("#"))
            bgColorHex = "#" + bgColorHex;
        if (!textColorHex.startsWith("#"))
            textColorHex = "#" + textColorHex;
        String sql = """
            UPDATE tag SET bg_color_hex = ?, text_color_hex = ?
            WHERE name = ?;
        """;
        PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
        preparedStatement.setString(1, bgColorHex);
        preparedStatement.setString(2, textColorHex);
        preparedStatement.setString(3, name);
        preparedStatement.executeUpdate();
    }

    public static void upsertBookTags(int bookId, ObservableList<Tag> tagList) throws SQLException {
        // Clear all old book tags
        String sql = """
            DELETE FROM book_tag WHERE book_id = ?;
        """;
        PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
        preparedStatement.setInt(1, bookId);
        preparedStatement.executeUpdate();

        // Add new book tags
        sql = """
            INSERT INTO book_tag (book_id, tag) VALUES (?, ?);
        """;
        try (PreparedStatement preparedInsertStatement = AppMain.connection.prepareStatement(sql)) {
            for (Tag tag : tagList) {
                preparedInsertStatement.setInt(1, bookId);
                preparedInsertStatement.setString(2, tag.getTagName());
                preparedInsertStatement.addBatch();
            }
            preparedInsertStatement.executeBatch();
        }
    }

    public static ObservableList<Tag> getBookTags(int bookId) throws SQLException {
        String sql = """
            SELECT t.name, t.bg_color_hex, t.text_color_hex
            FROM book_tag bt
            INNER JOIN tag t ON bt.tag = t.name
            WHERE bt.book_id = ?;
        """;
        PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
        preparedStatement.setInt(1, bookId);
        ResultSet resultSet = preparedStatement.executeQuery();
        ObservableList<Tag> tagList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            String bgColorHex = resultSet.getString("bg_color_hex");
            String textColorHex = resultSet.getString("text_color_hex");
            tagList.add(new Tag(name, bgColorHex, textColorHex));
        }
        return tagList;
    }

    public static ObservableList<BookLib> getBookList() throws SQLException {
        String sql = "SELECT * FROM book";
        PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        ObservableList<BookLib> bookLib = FXCollections.observableArrayList();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String title = resultSet.getString("name");
            String author = resultSet.getString("author");
            String description = resultSet.getString("description");
            int copiesAvailable = copiesAvailable(id);

            bookLib.add(new BookLib(id, title, author, description, copiesAvailable));
        }
        return bookLib;
    }
}

