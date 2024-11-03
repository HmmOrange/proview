package org.proview.model;

public class BookGoogle extends Book {
    private String coverImageUrl;
    public BookGoogle(String title, String author, String description, String coverImageUrl) {
        super(title, author, description);
        this.coverImageUrl = coverImageUrl;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }
}
