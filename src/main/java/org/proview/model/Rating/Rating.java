package org.proview.model.Rating;

import java.sql.Timestamp;

public class Rating {
    private int userId;
    private int bookId;
    private int star;
    private Timestamp timeAdded;

    public Rating(int userId, int bookId, int star, Timestamp timeAdded) {
        this.userId = userId;
        this.bookId = bookId;
        this.star = star;
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

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }
}
