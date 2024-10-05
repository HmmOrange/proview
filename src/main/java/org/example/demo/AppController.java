package org.example.demo;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class AppController {
    public TextField documentAddID;
    public TextField documentAddName;
    public TextField documentAddAuthor;
    public TextField documentRemoveID;
    public ListView<String> documentList;
    public Button addButton;

    private void refreshDocumentListView() {
        documentList.getItems().clear();

        ObservableList<Document> currentDocumentList = DocumentAPI.getDocumentList();
        for (Document d : currentDocumentList) {
            String newDocumentItem = d.getId() + ". " + d.getName() + " - " + d.getAuthor();
            documentList.getItems().add(newDocumentItem);
        }
    }
    @FXML
    public void onAddButtonClick(ActionEvent actionEvent) {
        Document newDocument = DocumentAPI.addDocument(
                Integer.parseInt(documentAddID.getText()),
                documentAddName.getText(),
                documentAddAuthor.getText()
        );
        String newDocumentItem = documentAddID.getText() + ". " + documentAddName.getText() + " - " + documentAddAuthor.getText();
        documentList.getItems().add(newDocumentItem);
    }

    public void onRemoveButtonClick(ActionEvent actionEvent) {
        DocumentAPI.removeDocument(Integer.parseInt(documentRemoveID.getText()));
        refreshDocumentListView();
    }
}