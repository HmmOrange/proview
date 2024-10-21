package org.proview.model;

public class BookCell {
    private String title;
    private String tags;
    private double rating;
    private int issueCount;
    private String imagePath;
    private int copiesAvailable;

    public BookCell(String title, String tags, double rating, int issueCount, String imagePath, int copiesAvailable) {
        this.title = title;
        this.tags = tags;
        this.rating = rating;
        this.issueCount = issueCount;
        this.imagePath = imagePath;
        this.copiesAvailable = copiesAvailable;
    }

    public String getTitle() {
        return title;
    }

    public String getTags() {
        return tags;
    }

    public double getRating() {
        return rating;
    }

    public int getIssueCount() {
        return issueCount;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getCopiesAvailable() {
        return copiesAvailable;
    }
}
