package org.proview.test.Scene;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.proview.modal.Book.BookLib;
import org.proview.modal.Book.BookManagement;

import java.sql.SQLException;

public class LibraryView {
    public ListView<BookLib> allBooksListView;

    public void initialize() throws SQLException {
        ObservableList<BookLib> bookList = BookManagement.getBookList();

        BookManagement.initLibBookList(allBooksListView, bookList);


        allBooksListView.setMinWidth(500 + 10);
    }
}
