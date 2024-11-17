package org.proview.test.Container;

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
import org.proview.test.Scene.ProfileView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class ProfileBookListCompactView {

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

    public ListView<BookLib> borrowingCompactListView;
    public ListView<BookLib> overdueCompactListView;
    public ListView<BookLib> pastIssuesCompactListView;

    private void loadBorrowingListView() throws SQLException {
        ObservableList<BookLib> borrowingBookList = ProfileView.borrowingBookList;
        if (borrowingBookList == null) {
            ProfileView.loadBorrowingList();
            borrowingBookList = ProfileView.borrowingBookList;
        }

        if (borrowingBookList != null) {
            BookManagement.initBookLibCompactList(borrowingCompactListView, borrowingBookList, false);
            borrowingCompactListView.setPrefHeight(
                    Size.BOOK_CELL_COMPACT_HEIGHT.getValue() * borrowingBookList.size()
                            + Size.PADDING.getValue()
            );
            borrowingCompactListView.setMinWidth(
                    Size.BOOK_LISTVIEW_COMPACT_WIDTH.getValue()
                            + Size.PADDING.getValue());
        }
    }

    private void loadOverdueListView() throws SQLException {
        ObservableList<BookLib> overdueBookList = ProfileView.overdueBookList;
        if (overdueBookList == null) {
            ProfileView.loadOverdueBookList();
            overdueBookList = ProfileView.overdueBookList;
        }
        if (overdueBookList != null) {
            BookManagement.initBookLibCompactList(overdueCompactListView, overdueBookList, false);
            overdueCompactListView.setPrefHeight(
                    Size.BOOK_CELL_COMPACT_HEIGHT.getValue() * overdueBookList.size()
                            + Size.PADDING.getValue()
            );
            overdueCompactListView.setMinWidth(
                    Size.BOOK_LISTVIEW_COMPACT_WIDTH.getValue()
                            + Size.PADDING.getValue());
        }
    }

    private void loadPastIssuesListView() throws SQLException {
        ObservableList<BookLib> pastIssuesBookList = ProfileView.pastIssuesBookList;
        if (pastIssuesBookList == null) {
            ProfileView.loadPastIssuesBookList();
            pastIssuesBookList = ProfileView.pastIssuesBookList;
        }
        if (pastIssuesBookList != null) {
            BookManagement.initBookLibCompactList(pastIssuesCompactListView, pastIssuesBookList, false);
            pastIssuesCompactListView.setPrefHeight(
                    Size.BOOK_CELL_COMPACT_HEIGHT.getValue() * pastIssuesBookList.size()
                            + Size.PADDING.getValue()
            );
            pastIssuesCompactListView.setMinWidth(
                    Size.BOOK_LISTVIEW_COMPACT_WIDTH.getValue()
                            + Size.PADDING.getValue());
        }
    }

    public void initialize() throws SQLException, FileNotFoundException {
        loadBorrowingListView();
        loadOverdueListView();
        loadPastIssuesListView();
        System.out.println("Inside");
    }
}
