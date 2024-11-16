package org.proview.test.Scene;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import org.proview.modal.Activity.Activity;
import org.proview.modal.Activity.ActivityManagement;
import org.proview.modal.Book.BookLib;
import org.proview.modal.Book.BookManagement;
import java.sql.SQLException;

public class HomeView {
    private enum Size {
        BOOK_CELL_HEIGHT(125),
        ACTIVITY_CELL_HEIGHT(75),
        BOOK_LISTVIEW_WIDTH(400),
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

    public ListView<BookLib> topRatedBookListView;
    public ListView<BookLib> trendingBookListView;
    public ListView<Activity> recentReviewListView;

    public void initialize() throws SQLException {
        ObservableList<BookLib> topRatedList = BookManagement.getTopRatedBookList();
        ObservableList<BookLib> trendingList = BookManagement.getTrendingBookList();
        ObservableList<Activity> activityList = ActivityManagement.getReviewActivityList();

        BookManagement.initLibBookList(topRatedBookListView, topRatedList);
        BookManagement.initLibBookList(trendingBookListView, trendingList);
        ActivityManagement.initActivityList(recentReviewListView, activityList);

        // Make the list view non-scrollable (there is probably a better way to do this)
        topRatedBookListView.setPrefHeight(Size.BOOK_CELL_HEIGHT.getValue() * topRatedList.size() + Size.PADDING.getValue());
        trendingBookListView.setPrefHeight(Size.BOOK_CELL_HEIGHT.getValue() * trendingList.size() + Size.PADDING.getValue());
        recentReviewListView.setPrefHeight(Size.ACTIVITY_CELL_HEIGHT.getValue() * activityList.size() + Size.PADDING.getValue());

        topRatedBookListView.setMinWidth(Size.BOOK_LISTVIEW_WIDTH.getValue() + 10);
        trendingBookListView.setMinWidth(Size.BOOK_LISTVIEW_WIDTH.getValue() + 10);
        recentReviewListView.setMinWidth(Size.RECENT_ACTIVITY_LISTVIEW_WIDTH.getValue() + 10);
    }
}
