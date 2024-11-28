package org.proview.model.Book;

import java.sql.SQLException;

public abstract class Book {
    private String title;
    private String author;
    private String description;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public Book(String title, String author, String description) {
        this.title = title;
        this.author = author;
        this.description = description;
    }

    public Book() {}

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public abstract int getId();

    public abstract double getRating() throws SQLException;

    public abstract int getIssueCount() throws SQLException;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public abstract String getTags() throws SQLException;

    public abstract int getCopiesAvailable() throws SQLException;

    public abstract String getCoverImageUrl();
}
