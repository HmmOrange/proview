package org.example.demo;
import java.sql.*;
import java.util.Scanner;

public class UserManagement {
    public static void addNormalUser(User user) throws SQLException {
        Connection connection = AppMain.connection;
        Statement statement = connection.createStatement();
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
        );
        String sql = "INSERT IGNORE INTO user (username, password, type) VALUES (?, ?, 1)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.executeUpdate();
    }

    public static User isValidLoginCredentials(String username, String password) throws SQLException {
        Connection connection = AppMain.connection;
        String sql = "SELECT * FROM user WHERE username = ? AND PASSWORD = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            return null;
        } else {
            resultSet.isFirst();
            return new User(resultSet.getString("username"), resultSet.getString("password"),
                    resultSet.getInt("type"));
        }
    }


}
