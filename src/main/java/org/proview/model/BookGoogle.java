package org.proview.model;

public class BookGoogle extends Book {
    private String coverImageUrl;
    private String tags;
    public BookGoogle(String title, String author, String description, String coverImageUrl, String tags) {
        super(title, author, description);
        this.coverImageUrl = coverImageUrl;
        this.tags = tags;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    @Override
    public String getTags() {
        return tags;
    }
}
