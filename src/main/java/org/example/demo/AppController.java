package org.example.demo;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class AppController {
    public TextField documentAddID;
    public TextField documentAddName;
    public TextField documentAddAuthor;
    public TextField documentRemoveID;
    public ListView<String> documentList;
    public Button addButton;

    @FXML
    public void initialize() {
        reloadDocumentListView();
    }

    private void reloadDocumentListView() {
        System.out.println("reload");
        try {
            assert documentList != null;
            documentList.getItems().clear();
        } catch (NullPointerException ignored) {

        }
        ObservableList<Document> currentDocumentList = null;
        try {
            currentDocumentList = DocumentManagement.getDocumentList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Document d : currentDocumentList) {
            String newDocumentItem = d.getId() + ". " + d.getName() + " - " + d.getAuthor();
            documentList.getItems().add(newDocumentItem);
        }
    }

    public void onAddButtonClick(ActionEvent actionEvent) throws SQLException {
        Document newDocument = new Document(
                Integer.parseInt(documentAddID.getText()),
                documentAddName.getText(),
                documentAddAuthor.getText()
        );

        DocumentManagement.addDocument (newDocument.getId(), newDocument.getName(), newDocument.getAuthor());
        String newDocumentItem = documentAddID.getText() + ". " + documentAddName.getText() + " - " + documentAddAuthor.getText();
        documentList.getItems().add(newDocumentItem);
    }

    public void onRemoveButtonClick(ActionEvent actionEvent) throws SQLException {
        DocumentManagement.removeDocument(Integer.parseInt(documentRemoveID.getText()));
        reloadDocumentListView();
    }
}