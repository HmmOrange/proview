package org.proview.test.Scene;

import javafx.collections.ObservableList;
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
    public VBox recentReviewListVBox;

    public void initialize() throws SQLException {
        ObservableList<BookLib> topRatedList = BookManagement.getTopRatedBookList();
        ObservableList<BookLib> trendingList = BookManagement.getTrendingBookList();
        ObservableList<Activity> activityList = ActivityManagement.getReviewActivityList();

        if (topRatedList.size() > 20) {
            topRatedList.remove(20, topRatedList.size());
        }

        if (trendingList.size() > 20) {
            trendingList.remove(20, trendingList.size());
        }

        if (activityList.size() > 20) {
            activityList.remove(20, activityList.size());
        }

        BookManagement.initBookList(topRatedBookListVBox, topRatedList, true, true, true);
        BookManagement.initBookList(trendingBookListVBox, trendingList, true, true, true);
        ActivityManagement.initActivityList(recentReviewListVBox, activityList);
    }
}
