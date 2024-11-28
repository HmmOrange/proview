package org.proview.test.Scene;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.proview.modal.User.UserManagement;
import org.proview.test.AppMain;
import org.proview.utils.SQLUtils;
import org.proview.utils.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

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
        Utils.switchScene("EditBookView.fxml");
    }

    public void onUsersButtonClick(ActionEvent actionEvent) throws IOException {
        UserManagement.setCurrentAdminViewButton(usersButton);
        Utils.switchScene("UserManagementForAdminView.fxml");
    }

    public void onBooksButtonClick(ActionEvent actionEvent) throws IOException {
        UserManagement.setCurrentAdminViewButton(booksButton);
        AppMain.window.centerOnScreen();Utils.switchScene("BookManagForAdminView.fxml");
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
