package org.example.demo;

import java.sql.*;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

public class dbtest {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        Connection connection = null;

        // Loading driver
        try {
            System.out.println(":3");
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Hi!");
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        // Establishing a connection to db
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/proview_data",
                    dotenv.get("SQL_USER"),
                    dotenv.get("SQL_PASSWORD")
            );

            Statement statement = connection.createStatement();
            statement.executeUpdate("""
                    INSERT INTO document (id, name, author)
                    VALUES (1, 'nein', 'hitler');
            """);
            ResultSet resultSet = statement.executeQuery("SELECT * FROM document");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("id"));
                System.out.println(resultSet.getString("name"));
                System.out.println(resultSet.getString("author"));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
