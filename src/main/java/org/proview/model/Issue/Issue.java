package org.proview.model.Issue;

import java.sql.Timestamp;

public class Issue {
    private int id;
    private int bookId;
    private int duration;
    private String username;
    private String status;
    private Timestamp start_time;
    private Timestamp end_time;

    private Issue(int id, int bookId, int duration, String username) {
        this.id = id;
        this.bookId = bookId;
        this.duration = duration;
        this.username = username;
        this.status = "Not picked up";
    }

    public Issue(int id, int bookId, int duration, String username, String status, Timestamp start_time) {
        this(id, bookId, duration, username);
        this.status = status;
        this.start_time = start_time;
    }

    public Issue(int id, int bookId, int duration, String username, String status, Timestamp start_time, Timestamp end_time) {
        this(id, bookId, duration, username);
        this.status = status;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public Timestamp getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Timestamp end_time) {
        this.end_time = end_time;
    }

    public Timestamp getStart_time() {
        return start_time;
    }

    public void setStart_time(Timestamp start_time) {
        this.start_time = start_time;
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

    public String getStatus() {
        return status;
    }

    public void setNotPickedUpStatus() {
        this.status = "Not picked up";
    }

    public void setPickedUpStatus() {
        this.status = "Picked up";
    }

    public void setReturnedStatus() {
        this.status = "Returned";
    }

    public void setMissingStatus() {
        this.status = "Missing";
    }
}
