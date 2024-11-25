package org.proview.test.Scene;

import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import org.proview.modal.Book.BookGoogle;
import org.proview.modal.Book.BookLib;
import org.proview.modal.Book.BookManagement;
import org.proview.utils.SearchUtils;

import java.io.IOException;
import java.sql.SQLException;


public class SearchResultView {
    public VBox topResultListVBox;
    public VBox googleBookListVBox;

    public void initialize() throws SQLException, IOException {
        String curQuery = SearchUtils.getCurQuery().toLowerCase();

        ObservableList<BookLib> bookLibList = BookManagement.getTopRatedBookList();
        ObservableList<BookGoogle> bookGoogleList = BookManagement.getGoogleBookList(curQuery);

        ObservableList<BookLib> filteredBookList = SearchUtils.filterBookList(curQuery, bookLibList);

        BookManagement.initBookList(topResultListVBox, filteredBookList, false, true, false);
        if (bookGoogleList != null)
            BookManagement.initBookList(googleBookListVBox, bookGoogleList, false, false, false);
    }
}
