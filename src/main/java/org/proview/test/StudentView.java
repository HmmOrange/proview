package org.proview.test;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.proview.model.DocumentManagement;
import org.proview.model.IssueManagement;
import org.proview.model.UserManagement;

import java.sql.SQLException;

public class StudentView {
    public ListView<String> documentList;
    public ListView<String> documentIssuedList;
    public Button borrowButton;
    public TextField documentBorrowID;
    public TextField documentBorrowDuration;

    @FXML
    public void initialize() throws SQLException {
        reloadDocumentListView();
        reloadIssueListView();
    }

    private void reloadDocumentListView() throws SQLException {
        documentList.setItems(DocumentManagement.getDocumentListView());
    }

    private void reloadIssueListView() throws SQLException {
        documentIssuedList.setItems(IssueManagement.getIssueListViewFrom(UserManagement.getCurrentUser().getUsername()));
    }

    public void onBorrowButtonClick(ActionEvent actionEvent) throws SQLException {
        String username = UserManagement.getCurrentUser().getUsername();
        int documentID = Integer.parseInt(documentBorrowID.getText());
        int duration = Integer.parseInt(documentBorrowDuration.getText());

        IssueManagement.addIssue(username, documentID, duration);
        reloadIssueListView();
    }
}
