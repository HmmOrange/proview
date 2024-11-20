package org.proview.test.Scene;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
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

    public VBox trendingBookListVBox;
    public VBox topRatedBookListVBox;
    public ListView<Activity> recentReviewListView;

    public void initialize() throws SQLException {
        ObservableList<BookLib> topRatedList = BookManagement.getTopRatedBookList();
        ObservableList<BookLib> trendingList = BookManagement.getTrendingBookList();
        ObservableList<Activity> activityList = ActivityManagement.getReviewActivityList();

        BookManagement.initBookLibList(topRatedBookListVBox, topRatedList, true, true);
        BookManagement.initBookLibList(trendingBookListVBox, trendingList, true, true);
        ActivityManagement.initActivityList(recentReviewListView, activityList);

        recentReviewListView.setPrefHeight(Size.ACTIVITY_CELL_HEIGHT.getValue() * activityList.size() + Size.PADDING.getValue());

        recentReviewListView.setMinWidth(Size.RECENT_ACTIVITY_LISTVIEW_WIDTH.getValue() + 10);
    }
}
