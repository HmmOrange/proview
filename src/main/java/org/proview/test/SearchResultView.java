package org.proview.test;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.proview.model.BookGoogle;
import org.proview.model.BookLib;
import org.proview.model.BookManagement;
import org.proview.model.SearchHandler;

import java.io.IOException;
import java.sql.SQLException;


public class SearchResultView {
    public ListView<BookLib> topResultListView;
    public ListView<BookGoogle> googleBookListView;

    public void initialize() throws SQLException, IOException {
        String curQuery = SearchHandler.getCurQuery().toLowerCase();

        ObservableList<BookLib> bookLibList = BookManagement.getTopRatedBookList();
        ObservableList<BookGoogle> bookGoogleList = BookManagement.getGoogleBookList(curQuery);

        ObservableList<BookLib> filteredBookList = SearchHandler.filterBookList(curQuery, bookLibList);

        BookManagement.initLibBookList(topResultListView, filteredBookList);
        BookManagement.initGoogleBookList(googleBookListView, bookGoogleList);

        topResultListView.setMinHeight(150 * filteredBookList.size() + 10);
        topResultListView.setMinWidth(500 + 10);

        if (bookGoogleList != null) {
            googleBookListView.setMinHeight(150 * bookGoogleList.size() + 10);
            googleBookListView.setMinWidth(500 + 10);
        }
    }
}
