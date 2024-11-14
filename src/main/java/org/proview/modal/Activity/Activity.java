package org.proview.modal.Activity;

import java.sql.Timestamp;

public class Activity {
    private int bookId;
    private int userId;
    private String description;
    private Timestamp timestampAdded;

    public Activity(int bookId, int userId, String description, Timestamp timestampAdded) {
        this.bookId = bookId;
        this.userId = userId;
        this.description = description;
        this.timestampAdded = timestampAdded;
    }

    public int getBookId() {
        return bookId;
    }

    public int getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getTimestampAdded() {
        return timestampAdded;
    }

    public long getTimestampAddedLong() {
        return timestampAdded.getTime();
    }
}
