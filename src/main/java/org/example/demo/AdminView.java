package org.example.demo;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class AdminView {
    public ListView<String> documentList;
    public ListView<String> documentIssuedList;
    public TextField documentAddID;
    public Button borrowButton;

    @FXML
    public void initialize() throws SQLException {
        reloadDocumentListView();
    }

    private void reloadDocumentListView() throws SQLException {
        System.out.println("reload");

        documentList.setItems(DocumentManagement.getListView());
    }

}