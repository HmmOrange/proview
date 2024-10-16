package org.proview.test;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.proview.model.Document;
import org.proview.model.DocumentManagement;

import java.sql.SQLException;

public class AdminView {
    public TextField documentAddID;
    public TextField documentAddName;
    public TextField documentAddAuthor;
    public TextField documentRemoveID;
    public ListView<String> documentList;
    public Button addButton;

    @FXML
    public void initialize() throws SQLException {
        reloadDocumentListView();
    }

    private void reloadDocumentListView() throws SQLException {
        System.out.println("reload");

        documentList.setItems(DocumentManagement.getDocumentListView());
    }

    public void onAddButtonClick(ActionEvent actionEvent) throws SQLException {
        Document newDocument = new Document(
                Integer.parseInt(documentAddID.getText()),
                documentAddName.getText(),
                documentAddAuthor.getText()
        );

        DocumentManagement.addDocument(newDocument.getId(), newDocument.getName(), newDocument.getAuthor());
        String newDocumentItem = documentAddID.getText() + ". " + documentAddName.getText() + " - " + documentAddAuthor.getText();
        documentList.getItems().add(newDocumentItem);
    }

    public void onRemoveButtonClick(ActionEvent actionEvent) throws SQLException {
        DocumentManagement.removeDocument(Integer.parseInt(documentRemoveID.getText()));
        reloadDocumentListView();
    }
}