package org.proview.model;

public class Issue {
    private int id;
    private int documentId;
    private int duration;
    private String username;

    public Issue(int id, int documentId, int duration, String username) {
        this.id = id;
        this.documentId = documentId;
        this.duration = duration;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
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
