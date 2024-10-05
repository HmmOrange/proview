package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {
    public TextField documentID;
    public TextField documentName;
    public TextField documentAuthor;

    @FXML
    private Label welcomeText;

    public void onAddButtonClick(ActionEvent actionEvent) {
        DocumentAPI.addDocument(
                Integer.parseInt(documentID.getText()),
                documentName.getText(),
                documentAuthor.getText()
        );
    }
}