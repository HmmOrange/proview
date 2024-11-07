package org.proview.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.proview.model.BookLib;
import org.proview.model.IssueManagement;
import org.proview.model.UserManagement;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public void initialize() {
        durationField.setVisible(false);
        durationField.setDisable(true);
        if(UserManagement.getCurrentUser().getType() == 1) {
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
        BookLib book = new BookLib(id);

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
        coverImage.setFitWidth(300);
        coverImage.setPreserveRatio(true);
        coverImage.setSmooth(true);
        coverImage.setCache(true);
        stream.close();
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
}
