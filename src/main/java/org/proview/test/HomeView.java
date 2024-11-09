package org.proview.test;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import org.proview.model.BookLib;
import org.proview.model.BookManagement;

import java.sql.SQLException;

public class HomeView {
    public ListView<BookLib> topRatedBookListView;
    public ListView<BookLib> trendingBookListView;
    public TextField bookSearchBar;
    public Label usernameField;
    public Label emailField;

    public void initialize() throws SQLException {
        ObservableList<BookLib> topRatedList = BookManagement.getTopRatedBookList();
        ObservableList<BookLib> trendingList = BookManagement.getTrendingBookList();

        BookManagement.initLibBookList(topRatedBookListView, topRatedList);
        BookManagement.initLibBookList(trendingBookListView, trendingList);

        // Make the list view non-scrollable (there is probably a better way to do this)
        topRatedBookListView.setMinHeight(150 * topRatedList.size() + 10);
        trendingBookListView.setMinHeight(150 * trendingList.size() + 10);

        topRatedBookListView.setMinWidth(500 + 10);
        trendingBookListView.setMinWidth(500 + 10);
    }
}
