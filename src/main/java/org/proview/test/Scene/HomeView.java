package org.proview.test.Scene;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import org.proview.modal.Activity.Activity;
import org.proview.modal.Activity.ActivityManagement;
import org.proview.modal.Book.BookLib;
import org.proview.modal.Book.BookManagement;
import org.proview.modal.Review.Review;
import org.proview.modal.Review.ReviewManagement;
import org.proview.modal.Utils.SQLUtils;

import java.sql.SQLException;

public class HomeView {
    public ListView<BookLib> topRatedBookListView;
    public ListView<BookLib> trendingBookListView;
    public ListView<Activity> recentReviewListView;
    public TextField bookSearchBar;
    public Label usernameField;
    public Label emailField;

    public void initialize() throws SQLException {
        ObservableList<BookLib> topRatedList = BookManagement.getTopRatedBookList();
        ObservableList<BookLib> trendingList = BookManagement.getTrendingBookList();
        ObservableList<Activity> activityList = ActivityManagement.getReviewActivityList();

        BookManagement.initLibBookList(topRatedBookListView, topRatedList);
        BookManagement.initLibBookList(trendingBookListView, trendingList);
        ActivityManagement.initActivityList(recentReviewListView, activityList);

        // Make the list view non-scrollable (there is probably a better way to do this)
        topRatedBookListView.setMinHeight(125 * topRatedList.size() + 10);
        trendingBookListView.setMinHeight(125 * trendingList.size() + 10);
        recentReviewListView.setMinHeight(100 * activityList.size() + 10);

        topRatedBookListView.setMinWidth(450 + 10);
        trendingBookListView.setMinWidth(450 + 10);
        recentReviewListView.setMinWidth(300 + 10);
    }
}
