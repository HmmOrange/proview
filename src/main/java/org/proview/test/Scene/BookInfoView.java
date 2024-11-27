package org.proview.test.Scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.proview.modal.Book.BookLib;
import org.proview.modal.Issue.IssueManagement;
import org.proview.modal.Review.Review;
import org.proview.modal.Review.ReviewManagement;
import org.proview.modal.Tag.Tag;
import org.proview.modal.Tag.TagManagement;
import org.proview.modal.User.NormalUser;
import org.proview.modal.User.User;
import org.proview.modal.User.UserManagement;
import org.proview.utils.PopUpWindowUtils;
import org.proview.utils.SQLUtils;
import org.proview.test.AppMain;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

/**
 * The BookInfoView class represents a view for displaying detailed information about a book,
 * including its title, author, description, cover image, available copies, tags, rating, and reviews.
 * It also provides functionality to edit book information, borrow a book, and add reviews.
 */
public class BookInfoView {
    public Button backButton;
    public ImageView coverImage;
    public Label titleLabel;
    public Label authorLabel;
    public Label tagLabel;
    public Label descriptionLabel;
    public Button editButton;
    public Label copiesLabel;
    public TextField durationField;
    public Label ratingLabel;
    public Label issueLabel;
    public TextArea reviewTextArea;
    public ListView<Review> reviewListView;
    public Button starButton;
    public BorderPane borderPane;
    public Label borrowingProblemLabel;
    public HBox starRatingBar;
    public FontIcon[] starIconList;
    public HBox ratingHBox;
    public VBox reviewHBox;
    public VBox reviewListVBox;
    public Button submitReviewButton;
    public Button loadPrevReviewButton;
    public Button removePrevReviewButton;
    public HBox tagListHBox;
    public Button deleteButton;
    private int starMouseEntered = 0;
    private int bookId;
    private int curRating = 3;

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    private void reloadReviewList() throws SQLException, IOException {
        // Review list
        ObservableList<Review> reviewList = ReviewManagement.getReviewListWithBookId(bookId);
        ReviewManagement.initReviewList(reviewListVBox, reviewList);
    }

    private void reloadTagList() throws SQLException, IOException {
        ObservableList<Tag> tagList = SQLUtils.getBookTags(bookId);
        for (Tag tag : tagList) {
            tagListHBox.getChildren().add(tag.getLabel());
        }
    }

    private void reloadRatingLabel() throws SQLException {
        BookLib book = SQLUtils.getBook(bookId);
        assert book != null;
        ratingLabel.setText(
                ratingLabel.getText().split(" ", 2)[0] // If the label is already filled, only get the first default word (which in fxml is "Ratings:")
                        + " " + String.format("%.2f", book.getRating())
                        + " / 5.0 from "
                        + book.getRatingCount() + " rating" + (book.getRatingCount() != 1 ? "s" : "")
        );
    }

    public void initialize() throws SQLException {
        durationField.setVisible(false);
        durationField.setDisable(true);
        borrowingProblemLabel.setVisible(false);
        borrowingProblemLabel.setDisable(true);
        if (UserManagement.getCurrentUser() instanceof NormalUser) {
            deleteButton.setDisable(true);
            deleteButton.setVisible(false);
            editButton.setText("Borrow");
            editButton.setOnAction(actionEvent -> {
                try {
                    this.onBorrowButtonClick(actionEvent);
                } catch (SQLException | IOException e) {
                    throw new RuntimeException(e);
                }
            });
            durationField.setVisible(true);
            durationField.setDisable(false);
        }

        // Set star button
        FontIcon fontIcon = new FontIcon();
        fontIcon.getStyleClass().add("ikonli-font-icon");
        starButton.setGraphic(fontIcon);
        if (SQLUtils.isFavouriteBook(UserManagement.getCurrentUser().getId(), bookId))
            starButton.setId("star-icon-clicked");
        else
            starButton.setId("star-icon-default");
        starButton.applyCss();
    }

    public void setData(int id) throws IOException, SQLException {
        setBookId(id);
        BookLib book = Objects.requireNonNull(SQLUtils.getBook(id));

        titleLabel.setText(book.getTitle());
        authorLabel.setText(authorLabel.getText() + " " + book.getAuthor());
        descriptionLabel.setText(descriptionLabel.getText() + " " + book.getDescription());
        copiesLabel.setText(copiesLabel.getText() + " " + book.getCopiesAvailable());
        reloadTagList();
        reloadRatingLabel();
        issueLabel.setText(issueLabel.getText() + " " + book.getIssueCount());

        InputStream stream = new FileInputStream(book.getImagePath());
        Image image = new Image(stream);
        coverImage.setImage(image);
        coverImage.setFitWidth(250);
        coverImage.setPreserveRatio(true);
        coverImage.setSmooth(true);
        coverImage.setCache(true);
        stream.close();

        reloadReviewList();

        if (SQLUtils.isFavouriteBook(UserManagement.getCurrentUser().getId(), id)) {
            starButton.setId("star-icon-clicked");
        }
        if (UserManagement.getCurrentUser() instanceof NormalUser
                && SQLUtils.ifUserBorrowingBook(UserManagement.getCurrentUser().getId(), id)) {
            borrowingProblemLabel.setDisable(false);
            borrowingProblemLabel.setVisible(true);
            borrowingProblemLabel.setText("You are borrowing this book");
            editButton.setVisible(false);
            editButton.setDisable(true);
            durationField.setVisible(false);
            durationField.setDisable(true);
        } else if (UserManagement.getCurrentUser() instanceof NormalUser
                && SQLUtils.ifBookUnavailable(id)) {
            borrowingProblemLabel.setDisable(false);
            borrowingProblemLabel.setVisible(true);
            borrowingProblemLabel.setText("Book is currently unavailable");
            editButton.setVisible(false);
            editButton.setDisable(true);
            durationField.setVisible(false);
            durationField.setDisable(true);
        }

        // Load star rating bar
        curRating = SQLUtils.getRating(UserManagement.getCurrentUser().getId(), bookId);
        starIconList = new FontIcon[5];
        for (int i = 0; i < 5; i++) {
            starIconList[i] = new FontIcon();
            if (i < curRating) {
                starIconList[i].getStyleClass().setAll("ikonli-font-icon-rated");
            }
            else {
                starIconList[i].getStyleClass().setAll("ikonli-font-icon-default");
            }

            int index = i;
            starIconList[i].setOnMouseEntered(event -> {
                if (index + 1 != starMouseEntered) {
                    starMouseEntered = 0;
                    for (int j = 0; j <= index; j++) {
                        starIconList[j].getStyleClass().setAll("ikonli-font-icon-hover");
                    }
                    for (int j = index + 1; j < 5; j++) {
                        starIconList[j].getStyleClass().setAll("ikonli-font-icon-default");
                    }
                }
            });

            starIconList[i].setOnMouseClicked(event -> {
                starMouseEntered = index + 1;
                curRating = index + 1;
                try {
                    SQLUtils.setRating(UserManagement.getCurrentUser().getId(), bookId, curRating);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                for (int j = 0; j < curRating; j++) {
                    starIconList[j].getStyleClass().setAll("ikonli-font-icon-rated");
                }
                for (int j = curRating; j < 5; j++) {
                    starIconList[j].getStyleClass().setAll("ikonli-font-icon-default");
                }
                try {
                    reloadRatingLabel();
                    reloadReviewList();
                } catch (SQLException | IOException e) {
                    throw new RuntimeException(e);
                }
            });

            starRatingBar.getChildren().add(starIconList[i]);
        }
        starRatingBar.setId("star-rating");
        starRatingBar.setOnMouseExited(event -> {
            starMouseEntered = 0;
            for (int j = 0; j < curRating; j++) {
                starIconList[j].getStyleClass().setAll("ikonli-font-icon-rated");
            }
            for (int j = curRating; j < 5; j++) {
                starIconList[j].getStyleClass().setAll("ikonli-font-icon-default");
            }
        });
        starRatingBar.applyCss();

        // Review-related buttons
        if (SQLUtils.hasReview(UserManagement.getCurrentUser().getId(), bookId)) {
            loadPrevReviewButton.setVisible(true);
            loadPrevReviewButton.setDisable(false);

            removePrevReviewButton.setVisible(true);
            removePrevReviewButton.setDisable(false);

            submitReviewButton.setText("Repost Review");
        }
        else {
            loadPrevReviewButton.setVisible(false);
            loadPrevReviewButton.setDisable(true);

            removePrevReviewButton.setVisible(false);
            removePrevReviewButton.setDisable(true);

            submitReviewButton.setText("Submit Review");
        }
    }

    public void onBackButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("HomeView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }

    public void onBorrowButtonClick(ActionEvent actionEvent) throws SQLException, IOException {
        IssueManagement.addIssue(UserManagement.getCurrentUser().getUsername(), bookId, Integer.parseInt(durationField.getText()));
        this.onBackButtonClick(actionEvent);
    }

    public void onEditButtonClick(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("EditBookInfoView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        EditBookInfoView thisEditBookInfoView = fxmlLoader.getController();
        thisEditBookInfoView.initialize(bookId);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }

    public void onStarButtonClicked(ActionEvent mouseEvent) throws SQLException {
        if (starButton.getId().equals("star-icon-default")) {
            starButton.setId("star-icon-clicked");
            SQLUtils.addFavourite(UserManagement.getCurrentUser().getId(), this.bookId);
        } else {
            starButton.setId("star-icon-default");
            SQLUtils.removeFavourite(UserManagement.getCurrentUser().getId(), this.bookId);
        }
    }

    public void onSubmitReviewButtonClick(ActionEvent actionEvent) throws SQLException, IOException {
        User currentUser = UserManagement.getCurrentUser();
        currentUser.addComment(bookId, reviewTextArea.getText());

        submitReviewButton.setText("Repost Review");

        loadPrevReviewButton.setVisible(true);
        loadPrevReviewButton.setDisable(false);

        removePrevReviewButton.setVisible(true);
        removePrevReviewButton.setDisable(false);

        reviewTextArea.clear();

        reloadReviewList();
    }
    public void onLoadPrevReviewButtonClicked(ActionEvent actionEvent) throws SQLException {
        String review = SQLUtils.getReview(UserManagement.getCurrentUser().getId(), bookId);
        reviewTextArea.setText(review);
    }
    public void onRemovePrevReviewButtonClicked(ActionEvent actionEvent) throws SQLException, IOException {
        SQLUtils.removeReview(UserManagement.getCurrentUser().getId(), bookId);

        submitReviewButton.setText("Submit Review");

        loadPrevReviewButton.setVisible(false);
        loadPrevReviewButton.setDisable(true);

        removePrevReviewButton.setVisible(false);
        removePrevReviewButton.setDisable(true);

        reviewTextArea.clear();

        reloadReviewList();

    }

    public void onDeleteButtonClicked(ActionEvent actionEvent) throws SQLException, IOException {
        if (PopUpWindowUtils.showConfirmation("Warning!", "Are you sure to delete this book?")) {
            String sql = """
                    DELETE FROM book
                    WHERE id = ?
                    """;
            PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
            preparedStatement.setInt(1, bookId);
            preparedStatement.execute();
            PopUpWindowUtils.showNotification("Done!", "This book has been deleted.", Alert.AlertType.INFORMATION);
            onBackButtonClick(actionEvent);
        }
    }


}
