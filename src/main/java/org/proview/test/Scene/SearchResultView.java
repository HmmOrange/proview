package org.proview.test.Scene;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.proview.modal.Book.BookGoogle;
import org.proview.modal.Book.BookLib;
import org.proview.modal.Book.BookManagement;
import org.proview.utils.SearchUtils;

import java.io.IOException;
import java.sql.SQLException;


public class SearchResultView {
    public ListView<BookLib> topResultListView;
    public ListView<BookGoogle> googleBookListView;

    public void initialize() throws SQLException, IOException {
        String curQuery = SearchUtils.getCurQuery().toLowerCase();

        ObservableList<BookLib> bookLibList = BookManagement.getTopRatedBookList();
        ObservableList<BookGoogle> bookGoogleList = BookManagement.getGoogleBookList(curQuery);

        ObservableList<BookLib> filteredBookList = SearchUtils.filterBookList(curQuery, bookLibList);

        BookManagement.initBookLibList(topResultListView, filteredBookList);
        BookManagement.initBookGoogleList(googleBookListView, bookGoogleList);

        topResultListView.setMinHeight(150 * filteredBookList.size() + 10);
        topResultListView.setMinWidth(500 + 10);

        if (bookGoogleList != null) {
            googleBookListView.setMinHeight(150 * bookGoogleList.size() + 10);
            googleBookListView.setMinWidth(500 + 10);
        }
    }
}
