package org.proview.model.Book;

import java.sql.SQLException;

public class BookGoogle extends Book {
    private String coverImageUrl;
    private String tags;
    private String previewLink;

    public BookGoogle(String title, String author, String description, String coverImageUrl, String tags, String previewLink) {
        super(title, author, description);
        this.coverImageUrl = coverImageUrl;
        this.tags = tags;
        this.previewLink = previewLink;
    }

    @Override
    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    @Override
    public int getId() {
        return -1;
    }

    @Override
    public double getRating() {
        return -1;
    }

    @Override
    public int getIssueCount() throws SQLException {
        return -1;
    }

    @Override
    public String getTags() {
        return tags;
    }

    @Override
    public int getCopiesAvailable() throws SQLException {
        return -1;
    }

    public String getPreviewLink() {
        return previewLink;
    }
}
