package org.proview.modal.Issue;

public class Issue {
    private int id;
    private int bookId;
    private int duration;
    private String username;

    public Issue(int id, int bookId, int duration, String username) {
        this.id = id;
        this.bookId = bookId;
        this.duration = duration;
        this.username = username;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
