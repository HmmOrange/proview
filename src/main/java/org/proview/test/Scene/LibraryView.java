package org.proview.test.Scene;

import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import org.proview.modal.Book.BookLib;
import org.proview.modal.Book.BookManagement;

import java.sql.SQLException;

public class LibraryView {
    public VBox allBooksListVBox;

    public void initialize() throws SQLException {
        ObservableList<BookLib> bookList = BookManagement.getBookList();
        BookManagement.initBookList(allBooksListVBox, bookList, true, true, false);
    }
}
