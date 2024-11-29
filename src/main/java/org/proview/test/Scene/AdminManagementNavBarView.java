package org.proview.test.Scene;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import org.proview.model.User.UserManagement;
import org.proview.test.AppMain;
import org.proview.utils.Utils;

import java.io.IOException;
import java.sql.SQLException;

public class AdminManagementNavBarView {
    public Button addBookButton;
    public Button importExportButton;
    public Button issuesButtons;
    public Button usersButton;
    public Button booksButton;
    public Button tagsButton;

    public void initialize() {
        Button currentViewButton = UserManagement.getCurrentAdminViewButton();
        if (currentViewButton == null) {
            issuesButtons.getStyleClass().add("nav-button-active");
        }
        else {
            switch (currentViewButton.getId()) {
                case null:
                    break;
                case "addBookButton":
                    addBookButton.getStyleClass().add("nav-button-active");
                    break;
                case "importExportButton":
                    importExportButton.getStyleClass().add("nav-button-active");
                    break;
                case "issuesButtons":
                    issuesButtons.getStyleClass().add("nav-button-active");
                    break;
                case "usersButton":
                    usersButton.getStyleClass().add("nav-button-active");
                    break;
                case "booksButton":
                    booksButton.getStyleClass().add("nav-button-active");
                    break;
                case "tagsButton":
                    tagsButton.getStyleClass().add("nav-button-active");
                    break;
                default:
                    break;
            }
        }
    }
    public void onIssuesButtonClick(ActionEvent actionEvent) throws IOException {
        UserManagement.setCurrentAdminViewButton(issuesButtons);
        Utils.switchScene("IssueListView.fxml");
    }

    public void onAddDeleteButtonClick(ActionEvent actionEvent) throws IOException {
        UserManagement.setCurrentAdminViewButton(addBookButton);
        Utils.switchScene("AddBookView.fxml");
    }

    public void onUsersButtonClick(ActionEvent actionEvent) throws IOException {
        UserManagement.setCurrentAdminViewButton(usersButton);
        Utils.switchScene("UserManagementForAdminView.fxml");
    }

    public void onBooksButtonClick(ActionEvent actionEvent) throws IOException {
        UserManagement.setCurrentAdminViewButton(booksButton);
        AppMain.window.centerOnScreen();Utils.switchScene("BookManagementForAdminView.fxml");
    }

    public void onTagsButtonClick(ActionEvent actionEvent) throws IOException {
        UserManagement.setCurrentAdminViewButton(tagsButton);
        AppMain.window.centerOnScreen();Utils.switchScene("TagManageView.fxml");
    }

    public void onImportExportButtonClicked(ActionEvent actionEvent) throws SQLException, IOException {
        UserManagement.setCurrentAdminViewButton(importExportButton);
        AppMain.window.centerOnScreen();Utils.switchScene("ImportAndExportView.fxml");
    }
}
