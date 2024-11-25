package org.proview.test.Container;

import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import org.proview.modal.Book.BookLib;
import org.proview.modal.Book.BookManagement;
import org.proview.test.Scene.ProfileView;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public class ProfileBookListView {
    public VBox borrowingListVBox;
    public VBox overdueListVBox;
    public VBox pastIssuesListVBox;
    public VBox favouriteListVBox;

    private void loadBorrowingListView() throws SQLException {
        ObservableList<BookLib> borrowingBookList = ProfileView.borrowingBookList;
        if (borrowingBookList == null) {
            ProfileView.loadBorrowingList();
            borrowingBookList = ProfileView.borrowingBookList;
        }

        BookManagement.initBookList(borrowingListVBox, borrowingBookList, ProfileView.cardView, false, false);
    }

    private void loadOverdueListView() throws SQLException {
        ObservableList<BookLib> overdueBookList = ProfileView.overdueBookList;
        if (overdueBookList == null) {
            ProfileView.loadOverdueBookList();
            overdueBookList = ProfileView.overdueBookList;
        }

        BookManagement.initBookList(overdueListVBox, overdueBookList, ProfileView.cardView, false, false);
    }

    private void loadPastIssuesListView() throws SQLException {
        ObservableList<BookLib> pastIssuesBookList = ProfileView.pastIssuesBookList;
        if (pastIssuesBookList == null) {
            ProfileView.loadPastIssuesBookList();
            pastIssuesBookList = ProfileView.pastIssuesBookList;
        }

        BookManagement.initBookList(pastIssuesListVBox, pastIssuesBookList, ProfileView.cardView, false, false);
    }

    private void loadFavouriteListView() throws SQLException {
        ObservableList<BookLib> favouriteBookList = ProfileView.favouriteBookList;
        if (favouriteBookList == null) {
            ProfileView.loadFavouriteBookList();
            favouriteBookList = ProfileView.favouriteBookList;
        }

        BookManagement.initBookList(favouriteListVBox, favouriteBookList, ProfileView.cardView, false, false);
    }

    public void initialize() throws SQLException, FileNotFoundException {
        loadBorrowingListView();
        loadOverdueListView();
        loadPastIssuesListView();
        loadFavouriteListView();
    }
}
