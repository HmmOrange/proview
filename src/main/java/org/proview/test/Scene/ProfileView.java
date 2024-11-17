package org.proview.test.Scene;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.proview.modal.Activity.Activity;
import org.proview.modal.Activity.ActivityManagement;
import org.proview.modal.Book.BookLib;
import org.proview.modal.Book.BookManagement;
import org.proview.modal.User.Admin;
import org.proview.modal.User.NormalUser;
import org.proview.modal.User.UserManagement;
import org.proview.modal.Utils.SQLUtils;
import org.proview.test.AppMain;

import java.io.*;
import java.sql.SQLException;

public class ProfileView {
    private enum Size {
        BOOK_CELL_HEIGHT(125),
        BOOK_CELL_COMPACT_HEIGHT(75),
        ACTIVITY_CELL_HEIGHT(75),
        BOOK_LISTVIEW_WIDTH(400),
        BOOK_LISTVIEW_COMPACT_WIDTH(800),
        RECENT_ACTIVITY_LISTVIEW_WIDTH(350),
        PADDING(10);

        private final int value;

        Size(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public Label nameField;
    public Label emailField;
    public ImageView avatarImageView;
    public Button editProfileButton;
    public ListView<Activity> recentActivityListView;
    public ListView<BookLib> borrowingCompactListView;

    public static ObservableList<BookLib> borrowingBookList;
    public static ObservableList<BookLib> overdueBookList;
    public static ObservableList<BookLib> pastIssuesBookList;

    public ObservableList<BookLib> getBorrowingBookList() {
        return borrowingBookList;
    }

    private void loadProfile() throws FileNotFoundException {
        nameField.setText("Name: " + UserManagement.getCurrentUser().getFullName());
        emailField.setText("Mail: " + UserManagement.getCurrentUser().getEmail());

        InputStream stream = new FileInputStream("./assets/avatars/user" + UserManagement.getCurrentUser().getId() + ".png");
        Image image = new Image(stream);
        avatarImageView.setImage(image);
        avatarImageView.setFitHeight(150);
        avatarImageView.setPreserveRatio(true);
        avatarImageView.setSmooth(true);
        avatarImageView.setCache(true);

        if (UserManagement.getCurrentUser() instanceof Admin) {
            editProfileButton.setDisable(true);
            editProfileButton.setVisible(false);
        }
    }

    private void loadRecentActivity() throws SQLException {
        ObservableList<Activity> activityList = ActivityManagement.getAllActivityList(UserManagement.getCurrentUser().getId());
        ActivityManagement.initPersonalActivityList(recentActivityListView, activityList);
        recentActivityListView.setPrefHeight(
                Size.ACTIVITY_CELL_HEIGHT.getValue() * activityList.size()
                        + Size.PADDING.getValue());
        recentActivityListView.setMinWidth(
                Size.RECENT_ACTIVITY_LISTVIEW_WIDTH.getValue()
                        + Size.PADDING.getValue());
    }

    public static void loadBorrowingList() throws SQLException {
        borrowingBookList = SQLUtils.getBorrowingBookList(UserManagement.getCurrentUser().getId());
    }

    public static void loadOverdueBookList() throws SQLException {
        overdueBookList = SQLUtils.getOverdueBookList(UserManagement.getCurrentUser().getId());
    }

    public static void loadPastIssuesBookList() throws SQLException {
        pastIssuesBookList = SQLUtils.getPastIssuesBookList(UserManagement.getCurrentUser().getId());
    }

    public void initialize() throws SQLException, FileNotFoundException {
        loadProfile();
        loadRecentActivity();
        if (borrowingBookList == null) loadBorrowingList();
        if (overdueBookList == null) loadOverdueBookList();
        if (pastIssuesBookList == null) loadPastIssuesBookList();
    }

    public void onEditProfileButtonClick(ActionEvent actionEvent) throws IOException {
        if (UserManagement.getCurrentUser() instanceof NormalUser) {
            FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("EditProfileView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
            AppMain.window.setTitle("Hello!");
            AppMain.window.setScene(scene);
            AppMain.window.centerOnScreen();
        }
    }

    public static void resetBookList() {
        borrowingBookList = null;
        overdueBookList = null;
        pastIssuesBookList = null;
    }
}
