package org.proview.modal.Review;

import java.sql.Timestamp;

public class Review {
    private int bookId;
    private int userId;
    private int rating;
    private String review;
    private Timestamp timestampAdded;

    public Review(int bookId, int userId, String review, int rating, Timestamp timestampAdded) {
        this.bookId = bookId;
        this.userId = userId;
        this.review = review;
        this.rating = rating;
        this.timestampAdded = timestampAdded;
    }

    public int getBookId() {
        return bookId;
    }

    public int getUserId() {
        return userId;
    }

    public String getReview() {
        return review;
    }

    public Timestamp getTimestampAdded() {
        return timestampAdded;
    }

    public long getTimestampAddedLong() {
        return timestampAdded.getTime();
    }

    public int getRating() {
        return rating;
    }
}
