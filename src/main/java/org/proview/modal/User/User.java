package org.proview.modal.User;

import org.proview.utils.SQLUtils;

import java.sql.SQLException;

public abstract class User {
    protected int id;
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected Boolean cardView;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.cardView = true;
    }

    public User(int id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.cardView = true;
    }

    public User(int id, String username, String password, String firstName, String lastName, String email, Boolean cardView) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.cardView = cardView;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getCardView() {
        return cardView;
    }

    public void setCardView(Boolean cardView) {
        this.cardView = cardView;
    }

    public void addComment(int book_id, String review) throws SQLException {
        SQLUtils.addComment(book_id, id, review);
    }
}
