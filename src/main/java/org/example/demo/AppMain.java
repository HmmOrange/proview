package org.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import java.sql.*;
import java.util.Scanner;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;


public class AppMain extends Application {
    static Dotenv dotenv = Dotenv.load();
    static Connection connection = null;

    protected static Stage window;


    @Override
    public void start(Stage stage) throws IOException {
        window = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        window.setTitle("Hello!");
        window.setScene(scene);
        window.setMinWidth(500);
        window.setMinHeight(500);
        window.show();
    }

    public static void main(String[] args) throws SQLException {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        /*test UserManagement
        Scanner sc = new Scanner(System.in);
        String username = sc.next();
        String password = sc.next();
        User user = new User(username, password, 1);
        UserManagement.addNormalUser(user);
        String usn = sc.next();
        String pw = sc.next();
        if (UserManagement.isValidLoginCredentials(usn, pw) == null) System.out.println("Wrong username or password");
        else System.out.println("Welcome, " + usn);
        */

        launch();
    }
}