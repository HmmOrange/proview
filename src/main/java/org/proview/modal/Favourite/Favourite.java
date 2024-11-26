package org.proview.modal.Favourite;

import java.sql.Timestamp;

public class Favourite {
    private int userId;
    private int bookId;
    private Timestamp timeAdded;

    public Favourite(int userId, int bookId, Timestamp timeAdded) {
        this.userId = userId;
        this.bookId = bookId;
        this.timeAdded = timeAdded;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }
}
