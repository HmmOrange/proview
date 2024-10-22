package org.proview.test;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.proview.model.DocumentManagement;
import org.proview.model.UserManagement;
import java.nio.file.StandardCopyOption;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

public class AdminView {
    public TextField documentAddName;
    public TextField documentAddAuthor;
    public TextField documentRemoveID;
    public ListView<String> documentList;
    public Button addDocumentButton;
    public Button logoutButton;
    public Button addCoverButton;
    public Label fileAddedLabel;
    public ImageView coverImageView;

    private File coverFile;

    @FXML
    public void initialize() throws SQLException {
        reloadDocumentListView();
    }

    private void reloadDocumentListView() throws SQLException {
        System.out.println("reload");

        documentList.setItems(DocumentManagement.getDocumentListView());
    }

    public void onAddDocumentButtonClick(ActionEvent actionEvent) throws SQLException, IOException {
        String coverFilePath = coverFile.toURI().toString().substring(6); // This has prefix 'file:/'
        String extension = "";

        int i = coverFilePath.lastIndexOf('.');
        if (i > 0) {
            extension = coverFilePath.substring(i);
        }
        String dstFilePath = "./assets/covers/cover" + (DocumentManagement.getDocumentCount() + 1) + extension;

        // Add document to DB
        DocumentManagement.addDocument(
                documentAddName.getText(),
                documentAddAuthor.getText(),
                dstFilePath
        );

        // Store cover images in a folder (in practice this is stored in a CDN)
        Files.copy(coverFile.toPath(), (new File(dstFilePath)).toPath(), StandardCopyOption.REPLACE_EXISTING);

        // Reload the list view
        reloadDocumentListView();
    }

    public void onRemoveButtonClick(ActionEvent actionEvent) throws SQLException {
        DocumentManagement.removeDocument(Integer.parseInt(documentRemoveID.getText()));
        reloadDocumentListView();
    }

    public void onLogoutButtonClick(ActionEvent actionEvent) throws IOException {
        UserManagement.setCurrentUser(null);

        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        AppMain.window.setTitle("Login!");
        AppMain.window.setScene(scene);
    }

    public void onAddCoverButtonClick(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose document cover");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.gif")
                );
        coverFile = fileChooser.showOpenDialog(AppMain.window);
        if (coverFile != null) {
            fileAddedLabel.setText(coverFile.getName());

            Image image = new Image(coverFile.toURI().toString());
            if (image.isError()) {
                System.out.println("Error loading image: " + image.getException().getMessage());
            }
            coverImageView.setImage(image);
            coverImageView.setFitWidth(100);
            coverImageView.setPreserveRatio(true);
            coverImageView.setSmooth(true);
            coverImageView.setCache(true);
        }
    }
}