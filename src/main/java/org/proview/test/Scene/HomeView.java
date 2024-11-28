package org.proview.test.Scene;

import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import org.proview.model.Activity.Activity;
import org.proview.model.Activity.ActivityManagement;
import org.proview.model.Book.BookLib;
import org.proview.model.Book.BookManagement;
import java.sql.SQLException;

public class HomeView {
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
