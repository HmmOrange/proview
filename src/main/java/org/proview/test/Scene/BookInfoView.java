package org.proview.test.Scene;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.proview.modal.Book.BookLib;
import org.proview.modal.Issue.IssueManagement;
import org.proview.modal.Review.Review;
import org.proview.modal.Review.ReviewManagement;
import org.proview.modal.User.NormalUser;
import org.proview.modal.User.User;
import org.proview.modal.User.UserManagement;
import org.proview.modal.Utils.SQLUtils;
import org.proview.test.AppMain;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Objects;

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

    private int id;

    public void setId(int id) {
        this.id = id;
    }

    private void reloadReviewList() throws SQLException {
        // Review list
        ObservableList<Review> reviewList = ReviewManagement.getReviewListWithBookId(id);
        ReviewManagement.initReviewList(reviewListView, reviewList);

        // Make the list view non-scrollable (there is probably a better way to do this)
        reviewListView.setMinHeight(250 * reviewList.size() + 10);
        reviewListView.setMinWidth(850 + 10);
    }

    public void initialize() throws SQLException {
        durationField.setVisible(false);
        durationField.setDisable(true);
        if (UserManagement.getCurrentUser() instanceof NormalUser) {
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
    }


    public void setData(int id) throws IOException, SQLException {
        setId(id);
        BookLib book = Objects.requireNonNull(SQLUtils.getBook(id));

        titleLabel.setText(book.getTitle());
        authorLabel.setText(authorLabel.getText() + " " + book.getAuthor());
        descriptionLabel.setText(descriptionLabel.getText() + "\n" + book.getDescription());
        copiesLabel.setText(copiesLabel.getText() + " " + book.getCopiesAvailable());
        tagLabel.setText(tagLabel.getText() + " " + book.getTags());
        ratingLabel.setText(ratingLabel.getText() + " " + book.getRating());
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
    }

    public void onBackButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("HomeView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }

    public void onBorrowButtonClick(ActionEvent actionEvent) throws SQLException, IOException {
        IssueManagement.addIssue(UserManagement.getCurrentUser().getUsername(), id, Integer.parseInt(durationField.getText()));
        this.onBackButtonClick(actionEvent);
    }

    public void onEditButtonClick(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("EditBookInfoView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        EditBookInfoView thisEditBookInfoView = fxmlLoader.getController();
        thisEditBookInfoView.initialize(id);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }

    public void onSubmitReviewButtonClick(ActionEvent actionEvent) throws SQLException {
        User currentUser = UserManagement.getCurrentUser();
        currentUser.addComment(id, reviewTextArea.getText());
        reloadReviewList();
    }
}
