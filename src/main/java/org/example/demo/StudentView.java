package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.sql.SQLException;

public class StudentView {
    public ListView<String> documentList;

    @FXML
    public void initialize() throws SQLException {
        reloadDocumentListView();
    }

    private void reloadDocumentListView() throws SQLException {
        System.out.println("reload");

        documentList.setItems(DocumentManagement.getListView());
    }
}
