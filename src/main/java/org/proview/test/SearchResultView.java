package org.proview.test;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.proview.model.BookLib;
import org.proview.model.BookManagement;
import org.proview.model.SearchHandler;

import java.sql.SQLException;


public class SearchResultView {
    public ListView<BookLib> topResultListView;

    public void initialize() throws SQLException {
        ObservableList<BookLib> bookList = BookManagement.getTopRatedBookCellList();
        ObservableList<BookLib> filteredBookList = SearchHandler.filterBookList(bookList);

        BookManagement.initLibBookList(topResultListView, filteredBookList);

        topResultListView.setMinHeight(150 * filteredBookList.size() + 10);

        topResultListView.setMinWidth(500 + 10);
    }
}
