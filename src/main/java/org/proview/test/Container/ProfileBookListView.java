package org.proview.test.Container;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.proview.modal.Book.BookLib;
import org.proview.modal.Book.BookManagement;
import org.proview.test.Scene.ProfileView;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public class ProfileBookListView {

    private enum Size {
        BOOK_CELL_CARD_HEIGHT(125),
        BOOK_CELL_COMPACT_HEIGHT(75),
        BOOK_LISTVIEW_CARD_WIDTH(400),
        BOOK_LISTVIEW_COMPACT_WIDTH(800),
        ACTIVITY_CELL_HEIGHT(75),
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

    public ListView<BookLib> borrowingListView;
    public ListView<BookLib> overdueListView;
    public ListView<BookLib> pastIssuesListView;
    public ListView<BookLib> favouriteListView;

    private int bookCellHeight;
    private int bookListViewWidth;

    private void loadBorrowingListView() throws SQLException {
        ObservableList<BookLib> borrowingBookList = ProfileView.borrowingBookList;
        if (borrowingBookList == null) {
            ProfileView.loadBorrowingList();
            borrowingBookList = ProfileView.borrowingBookList;
        }

        BookManagement.initBookLibList(borrowingListView, borrowingBookList, ProfileView.cardView,false);
        borrowingListView.setPrefHeight(
                bookCellHeight * borrowingBookList.size() + Size.PADDING.getValue()
        );
        borrowingListView.setPrefWidth(
                bookListViewWidth + Size.PADDING.getValue()
        );
    }

    private void loadOverdueListView() throws SQLException {
        ObservableList<BookLib> overdueBookList = ProfileView.overdueBookList;
        if (overdueBookList == null) {
            ProfileView.loadOverdueBookList();
            overdueBookList = ProfileView.overdueBookList;
        }

        BookManagement.initBookLibList(overdueListView, overdueBookList, ProfileView.cardView, false);
        overdueListView.setPrefHeight(
                bookCellHeight * overdueBookList.size() + Size.PADDING.getValue()
        );
        overdueListView.setPrefWidth(
                bookListViewWidth + Size.PADDING.getValue()
        );
    }

    private void loadPastIssuesListView() throws SQLException {
        ObservableList<BookLib> pastIssuesBookList = ProfileView.pastIssuesBookList;
        if (pastIssuesBookList == null) {
            ProfileView.loadPastIssuesBookList();
            pastIssuesBookList = ProfileView.pastIssuesBookList;
        }

        BookManagement.initBookLibList(pastIssuesListView, pastIssuesBookList, ProfileView.cardView, false);
        pastIssuesListView.setPrefHeight(
                bookCellHeight * pastIssuesBookList.size() + Size.PADDING.getValue()
        );
        pastIssuesListView.setPrefWidth(
                bookListViewWidth + Size.PADDING.getValue()
        );

    }

    private void loadFavouriteListView() throws SQLException {
        ObservableList<BookLib> favouriteBookList = ProfileView.favouriteBookList;
        if (favouriteBookList == null) {
            ProfileView.loadFavouriteBookList();
            favouriteBookList = ProfileView.favouriteBookList;
        }

        BookManagement.initBookLibList(favouriteListView, favouriteBookList, ProfileView.cardView, false);
        pastIssuesListView.setPrefHeight(
                bookCellHeight * favouriteBookList.size() + Size.PADDING.getValue()
        );
        pastIssuesListView.setPrefWidth(
                bookListViewWidth + Size.PADDING.getValue()
        );
    }

    public void initialize() throws SQLException, FileNotFoundException {
        if (ProfileView.cardView) {
            bookCellHeight = Size.BOOK_CELL_CARD_HEIGHT.getValue();
            bookListViewWidth = Size.BOOK_LISTVIEW_CARD_WIDTH.getValue();
        }
        else {
            bookCellHeight = Size.BOOK_CELL_COMPACT_HEIGHT.getValue();
            bookListViewWidth = Size.BOOK_LISTVIEW_COMPACT_WIDTH.getValue();
        }
        System.out.println(bookListViewWidth);
        loadBorrowingListView();
        loadOverdueListView();
        loadPastIssuesListView();
        loadFavouriteListView();
        System.out.println("Inside");
    }
}
