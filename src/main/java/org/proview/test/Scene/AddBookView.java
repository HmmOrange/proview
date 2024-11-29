package org.proview.test.Scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import org.proview.model.Book.Book;
import org.proview.model.Book.BookManagement;
import org.proview.model.Tag.Tag;
import org.proview.model.Tag.TagManagement;
import org.proview.test.AppMain;
import org.proview.utils.PopUpWindowUtils;
import org.proview.utils.SQLUtils;

import java.nio.file.StandardCopyOption;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

public class AddBookView {
    public TextField bookAddName;
    public TextField bookAddAuthor;
    public TextArea bookAddDescription;
    public TextField bookAddCopies;
    public TextField bookAddTag;
    public Button addBookButton;
    public Button addCoverButton;
    public Label fileAddedLabel;
    public ImageView coverImageView;
    public FlowPane bookTagsSelectedFlowPane;
    public MenuButton bookTagsDropdown;
    private final ObservableList<Tag> tagSelectedList = FXCollections.observableArrayList();

    private File coverFile;

    public void initialize() throws SQLException {
        ObservableList<Tag> allTagsList = SQLUtils.getTagList();
        ObservableList<Tag> oldTagsList = FXCollections.observableArrayList();
        TagManagement.loadTagDropdown(allTagsList, oldTagsList, bookTagsDropdown, bookTagsSelectedFlowPane, tagSelectedList);
    }

    public void onAddBookButtonClick(ActionEvent actionEvent) throws SQLException, IOException {
        if (PopUpWindowUtils.showConfirmation("Warning", "Are you sure to add this book?")) {
            String coverFilePath;
            try {
                coverFilePath = coverFile.toURI().toString().substring(6); // This has prefix 'file:/'
            } catch (NullPointerException e) {
                coverFilePath = "file://assets/samples/defaultCover.png";
            }
            String extension = "";

            int i = coverFilePath.lastIndexOf('.');
            if (i > 0) {
                extension = coverFilePath.substring(i);
            }

            // If folder does not exist yet, create one
            File coverImageFolderPath = new File("./assets/covers");
            if (!coverImageFolderPath.exists()) {
                coverImageFolderPath.mkdirs();
            }

            String dstFilePath = "./assets/covers/cover" + (BookManagement.getBookCount() + 1) + extension;

            // Add book to DB
            BookManagement.addBook(
                    bookAddName.getText(),
                    bookAddAuthor.getText(),
                    bookAddDescription.getText(),
                    Integer.parseInt(bookAddCopies.getText()),
                    tagSelectedList
            );
            // Store cover images in a folder (in practice this is stored in a CDN)
            Files.copy(coverFile.toPath(), (new File(dstFilePath)).toPath(), StandardCopyOption.REPLACE_EXISTING);
            PopUpWindowUtils.showNotification("Done!", "Book has been added!", Alert.AlertType.INFORMATION);
        }
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
            coverImageView.setFitWidth(200);
            coverImageView.setPreserveRatio(true);
            coverImageView.setSmooth(true);
            coverImageView.setCache(true);
        }
    }

}