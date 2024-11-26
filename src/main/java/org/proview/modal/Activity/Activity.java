package org.proview.modal.Activity;

import java.lang.reflect.Type;
import java.sql.Timestamp;

public class Activity {
    public enum Type {
        REVIEW,
        RATE,
        ISSUE_START,
        ISSUE_END,
        WARNING,
        OVERDUE,
        RATING,
        FAVOURITE
    }

    private int bookId;
    private int userId;
    private String description;
    private Timestamp timestampAdded;
    private Type type;

    public Activity(int bookId, int userId, String description, Timestamp timestampAdded, Type type) {
        this.bookId = bookId;
        this.userId = userId;
        this.description = description;
        this.timestampAdded = timestampAdded;
        this.type = type;
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

    public Type getType() {
        return type;
    }
}
