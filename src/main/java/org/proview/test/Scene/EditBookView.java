package org.proview.test.Scene;

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
import org.proview.modal.Book.BookManagement;
import org.proview.modal.User.UserManagement;
import org.proview.test.AppMain;

import java.nio.file.StandardCopyOption;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

public class EditBookView {
    public TextField bookAddName;
    public TextField bookAddAuthor;
    public TextField bookAddDescription;
    public TextField bookAddCopies;
    public TextField bookAddTag;
    public TextField bookRemoveID;
    public Button addBookButton;
    public Button addCoverButton;
    public Label fileAddedLabel;
    public ImageView coverImageView;

    private File coverFile;


    public void onAddBookButtonClick(ActionEvent actionEvent) throws SQLException, IOException {
        String coverFilePath = coverFile.toURI().toString().substring(6); // This has prefix 'file:/'
        String extension = "";

        int i = coverFilePath.lastIndexOf('.');
        if (i > 0) {
            extension = coverFilePath.substring(i);
        }

        // If folder does not exist yet, create one
        File coverImageFolderPath = new File("./assets/covers");
        if (!coverImageFolderPath.exists()){
            coverImageFolderPath.mkdirs();
        }

        String dstFilePath = "./assets/covers/cover" + (BookManagement.getBookCount() + 1) + extension;

        // Add book to DB
        BookManagement.addBook(
                bookAddName.getText(),
                bookAddAuthor.getText(),
                bookAddDescription.getText(),
                Integer.parseInt(bookAddCopies.getText()),
                bookAddTag.getText()
        );

        // Store cover images in a folder (in practice this is stored in a CDN)
        Files.copy(coverFile.toPath(), (new File(dstFilePath)).toPath(), StandardCopyOption.REPLACE_EXISTING);

    }

    public void onRemoveButtonClick(ActionEvent actionEvent) throws SQLException {
        BookManagement.removeBook(Integer.parseInt(bookRemoveID.getText()));
    }

    public void onLogoutButtonClick(ActionEvent actionEvent) throws IOException {
        UserManagement.setCurrentUser(null);

        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }

    public void onAddCoverButtonClick(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose book cover");
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

    public void onBackButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("HomeView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }
}