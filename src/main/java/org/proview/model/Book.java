package org.proview.model;

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

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public abstract String getTags() throws SQLException;
}
