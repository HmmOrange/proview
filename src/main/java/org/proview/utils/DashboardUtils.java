package org.proview.utils;

import java.sql.SQLException;

public class DashboardUtils {
    public static class CopiesBorrowedPieChart {
        public static int getCopiesBorrowed() throws SQLException {
            return SQLUtils.getCurrentIssuesCount();
        }

        public static int getCopiesAvailable() throws SQLException {
            return SQLUtils.getTotalCopiesCount() - SQLUtils.getCurrentIssuesCount();
        }
    }
}
