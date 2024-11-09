package org.proview.model;

public class User {
    private String username;
    private String password;
    private int type; ///0 is admin, 1 is normal user
    private String firstName;
    private String lastName;
    private String email;

    public User(String username, String password, int type) {
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public User(String username, String password, int type, String firstName, String lastName, String email) {
        this.username = username;
        this.password = password;
        this.type = type;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
