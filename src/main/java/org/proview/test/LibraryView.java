package org.proview.test;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.proview.model.BookLib;
import org.proview.model.BookManagement;

import java.sql.SQLException;

public class LibraryView {
    public ListView allBooksListView;

    public void initialize() throws SQLException {
        ObservableList<BookLib> bookList = BookManagement.getBookList();

        BookManagement.initLibBookList(allBooksListView, bookList);


        allBooksListView.setMinWidth(500 + 10);
    }
}
