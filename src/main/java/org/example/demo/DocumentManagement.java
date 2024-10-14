package org.example.demo;

import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DocumentManagement {
    public static void addDocument (int id, String name, String author) throws SQLException {
        Connection connection = AppMain.connection;
        Statement statement = connection.createStatement();
        statement.executeUpdate("""
            CREATE TABLE IF NOT EXISTS document (
                id INT PRIMARY KEY,
                name VARCHAR(100),
                author VARCHAR(100)
            );
        """);
        String sql = "INSERT INTO document(id, name, author) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, author);
        preparedStatement.executeUpdate();
    }

    public static void removeDocument (int id) throws SQLException {
        Connection connection = AppMain.connection;
        String sql = "DELETE FROM document WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    public static ObservableList<Document> getDocumentList() throws SQLException {
        ObservableList<Document> documents = FXCollections.observableArrayList();
        Connection connection = AppMain.connection;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM document");
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String author = resultSet.getString("author");
            Document curDocument = new Document(id, name, author);
            documents.add(curDocument);
        }
        return documents;
    }

    public static ObservableList<String> getListView() throws SQLException {
        ObservableList<Document> currentDocumentList = null;
        ObservableList<String> documentStringList = FXCollections.observableArrayList();
        try {
            currentDocumentList = DocumentManagement.getDocumentList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Document d : currentDocumentList) {
            String newDocumentItem = d.getId() + ". " + d.getName() + " - " + d.getAuthor();
            documentStringList.add(newDocumentItem);
        }

        return documentStringList;
    }
}
