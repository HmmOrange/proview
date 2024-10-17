package org.proview.model;

import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.proview.test.AppMain;

public class DocumentManagement {
    public static void addDocument(String name, String author) throws SQLException {
        Connection connection = AppMain.connection;

        String sql = "INSERT INTO document(name, author) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, author);
        preparedStatement.executeUpdate();
    }

    public static void removeDocument(int id) throws SQLException {
        Connection connection = AppMain.connection;
        String sql = "DELETE FROM document WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    public static Document getDocument(int id) throws SQLException {
        Connection connection = AppMain.connection;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM document WHERE id = " + id);

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String author = resultSet.getString("author");
            return new Document(id, name, author);
        }

        return null;
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

    public static ObservableList<String> getDocumentListView() throws SQLException {
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
