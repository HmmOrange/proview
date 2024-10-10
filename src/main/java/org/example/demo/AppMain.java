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

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setMinWidth(500);
        stage.setMinHeight(500);
        stage.show();
    }

    public static void main(String[] args) {


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

//            Statement statement = connection.createStatement();
//            statement.executeUpdate("""
//                    CREATE TABLE IF NOT EXISTS document (
//                        id INT PRIMARY KEY,
//                        name VARCHAR(100),
//                        author VARCHAR(100)
//                    );
//            """);
//            statement.executeUpdate("""
//                    DROP TABLE IF EXISTS document;
//            """);
//            Scanner sc = new Scanner(System.in);
//            System.out.print("Number of queries: ");
//            int queries = sc.nextInt();
//            String sql = "INSERT INTO document (id, name, author) VALUES (?, ?, ?)";
//            PreparedStatement st = connection.prepareStatement(sql);
//            for (int q = 0; q < queries; q++) {
//                int id = sc.nextInt();
//                String name = sc.next();
//                String author = sc.next();
//
//                st.setInt(1, id);       // Đặt id vào placeholder đầu tiên (?)
//                st.setString(2, name);  // Đặt name vào placeholder thứ hai (?)
//                st.setString(3, author); // Đặt author vào placeholder thứ ba (?)
//            }
//            /*statement.executeUpdate("""
//                    INSERT INTO document (id, name, author) VALUES (id, name, author);
//            """);*/
//            st.executeUpdate();
//            ResultSet resultSet = statement.executeQuery("SELECT * FROM document");
//            while (resultSet.next()) {
//                System.out.println(resultSet.getString("id"));
//                System.out.println(resultSet.getString("name"));
//                System.out.println(resultSet.getString("author"));
//
//            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        launch();
    }
}