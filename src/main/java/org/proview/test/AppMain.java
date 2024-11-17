package org.proview.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.*;

import io.github.cdimascio.dotenv.Dotenv;


public class AppMain extends Application {
    static Dotenv dotenv = Dotenv.load();
    public static Connection connection = null;
    public static Stage window;
    
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

    public static Connection connectToDb() {
        // Loading driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver found!");
        } catch (Exception e) {
            System.out.println(e.toString());
            throw new RuntimeException(e);
        }

        // Establishing a connection to db
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/proview_data",
                    dotenv.get("SQL_USER"),
                    dotenv.get("SQL_PASSWORD")
            );
            System.out.println("Connected to 'proview_data'");
        } catch (SQLException e) {
            System.out.println(e.toString());
            throw new RuntimeException(e);
        }

        return connection;
    }

    public static String getSQLReadableFolder() throws SQLException {
        String targetFolderPath = "";
        PreparedStatement preparedStatement = connection.prepareStatement("SHOW VARIABLES LIKE 'secure_file_priv'");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) targetFolderPath = resultSet.getString("Value");
        targetFolderPath = targetFolderPath.replace('\\','/');
        return targetFolderPath;
    }

    public static void copyAvatarsToSQLReadableFolder() throws SQLException, IOException {
        Path sourceFolderPath = Paths.get("./assets/avatars");
        Path targetFolderPath = Paths.get(getSQLReadableFolder() + "/proview/avatars");
        try {
            // Ensure target folder exists
            if (!Files.exists(targetFolderPath)) {
                Files.createDirectories(targetFolderPath);
            }

            // Walk through the source directory and copy each file to the target
            Files.walkFileTree(sourceFolderPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path targetFilePath = targetFolderPath.resolve(sourceFolderPath.relativize(file));
                    Files.copy(file, targetFilePath, StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    System.out.println("Failed to visit file: " + file);
                    return FileVisitResult.CONTINUE;
                }
            });

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println(targetFolderPath);
    }

    public static void runSQLScript(Connection connection) throws IOException, SQLException {
        copyAvatarsToSQLReadableFolder();
        Statement statement = connection.createStatement();

        String filePath = "./src/main/sql/Initialize.sql";
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        StringBuilder query = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            if (line.trim().startsWith("-- ")) {
                continue;
            }

            // Append the line into the query string and add a space after that
            query.append(line).append(" ");

            if (line.trim().endsWith(";")) {
                // Execute the Query
                statement.execute(query.toString().trim());

                // Empty the Query string to add new query from the file
                query = new StringBuilder();
            }
        }

        ResultSet resultSet = statement.getResultSet();

        while (resultSet.next()) {
            // Getting the data inside the columns
            String username = resultSet.getString("username");
            String password = resultSet.getString("username");
            int type = resultSet.getInt("type");

            System.out.println(username + "\t" + password + "\t" + type);
        }

        System.out.println("Script file executed successfully");
    }

    public static void main(String[] args) throws SQLException, IOException {
        Connection connection = connectToDb();

        if (connection == null) {
            return;
        }

        // Uncomment this if in need of creating new fresh tables in DB
        runSQLScript(connection);
        launch();
    }
}