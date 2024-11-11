package org.proview.test.Scene;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.proview.modal.Book.BookManagement;
import org.proview.modal.Issue.IssueManagement;
import org.proview.modal.User.UserManagement;
import org.proview.test.AppMain;

import java.io.IOException;
import java.sql.SQLException;

public class CreateIssueView {
    public ListView<String> bookList;
    public ListView<String> bookIssuedList;
    public Button borrowButton;
    public TextField bookBorrowID;
    public TextField bookBorrowDuration;
    public Button logoutButton;

    @FXML
    public void initialize() throws SQLException {
        reloadBookListView();
        reloadIssueListView();
    }

    private void reloadBookListView() throws SQLException {
        bookList.setItems(BookManagement.getBookListView());
    }

    private void reloadIssueListView() throws SQLException {
        bookIssuedList.setItems(IssueManagement.getIssueListViewFrom(UserManagement.getCurrentUser().getUsername()));
    }

    public void onBorrowButtonClick(ActionEvent actionEvent) throws SQLException {
        String username = UserManagement.getCurrentUser().getUsername();
        int bookID = Integer.parseInt(bookBorrowID.getText());
        int duration = Integer.parseInt(bookBorrowDuration.getText());

        IssueManagement.addIssue(username, bookID, duration);
        reloadIssueListView();
    }

    public void onLogoutButtonClick(ActionEvent actionEvent) throws IOException {
        UserManagement.setCurrentUser(null);

        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }
}
