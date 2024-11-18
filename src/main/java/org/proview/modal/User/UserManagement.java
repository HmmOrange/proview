package org.proview.modal.User;
import org.proview.test.AppMain;

import java.sql.*;

public class UserManagement {
    Connection connection = AppMain.connection;

    public static User currentUser = null;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        UserManagement.currentUser = currentUser;
    }

    public static void addNormalUser(User user) throws SQLException {
        Connection connection = AppMain.connection;
        /*Statement statement = connection.createStatement();
        statement.executeUpdate(
        """
                CREATE TABLE IF NOT EXISTS user (
                    username VARCHAR(20) PRIMARY KEY,
                    password VARCHAR(20),
                    type INT
                );
            """
        );
        statement.executeUpdate(
        """
                INSERT IGNORE INTO user (username, password, type)
                VALUES ('admin', 'admin', 0);
            """
        );*/
        String sql = "INSERT IGNORE INTO user (username, password, type, firstname, lastname, email) VALUES (?, ?, 1, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getFirstName());
        preparedStatement.setString(4, user.getLastName());
        preparedStatement.setString(5, user.getEmail());
        preparedStatement.executeUpdate();
    }
}
