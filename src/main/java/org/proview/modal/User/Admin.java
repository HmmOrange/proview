package org.proview.modal.User;

public class Admin extends User {
    public Admin(int id, String username, String password, String firstName, String lastName, String email, Boolean cardView) {
        super(id, username, password, firstName, lastName, email, cardView);
    }

    public Admin(int id, String firstName, String lastName, String email) {
        super(id, firstName, lastName, email);
    }
}
