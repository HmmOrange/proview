package org.proview.test.Scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import org.proview.model.Tag.Tag;
import org.proview.test.AppMain;
import org.proview.utils.MaxSizedContextMenu;
import org.proview.utils.PopUpWindowUtils;
import org.proview.utils.SQLUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditBookInfoView {
    public TextField titleField;
    public TextField authorField;
    public TextArea descriptionField;
    public TextField copiesField;
    public ImageView coverImage;
    public Button backButton;
    public Button confirmButton;
    public Button changeCoverButton;
    public MenuButton tagSelectDropdown;
    public FlowPane tagSelectedFlowPane;
    private final ObservableList<Tag> tagSelectedList = FXCollections.observableArrayList();
    private int id;
    private File coverFile;

    private void addSelectedTag(Tag tag) {
        // Making a cloned tag because a label can only have 1 parent.
        Tag newTag = new Tag(tag.getTagName(), tag.getBgColorHex(), tag.getTextColorHex());
        tagSelectedList.add(newTag);
        tagSelectedFlowPane.getChildren().add(newTag.getLabel());
    }

    private void removeSelectedTag(Tag tag) {
        tagSelectedList.removeIf(tagInList ->
                tag.getTagName().equals((tagInList.getTagName()))
        );
        tagSelectedFlowPane.getChildren().removeIf(node ->
                tag.getTagName().equals(((Label) node).getText())
        );
    }
    public void initialize(int id) throws SQLException, IOException {
        this.id = id;
        Connection connection = AppMain.connection;
        String sql = "SELECT name, author, description, copies FROM book WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            titleField.setText(resultSet.getString("name"));
            authorField.setText(resultSet.getString("author"));
            descriptionField.setText(resultSet.getString("description"));
            copiesField.setText(resultSet.getString("copies"));
        }

        InputStream stream = new FileInputStream(String.format("./assets/covers/cover%d.png", id));
        Image image = new Image(stream);
        coverImage.setImage(image);
        coverImage.setFitWidth(300);
        coverImage.setPreserveRatio(true);
        coverImage.setSmooth(true);
        coverImage.setCache(true);
        stream.close();

        coverFile = new File(String.format("./assets/covers/cover%d.png", id));

        // Tag manu dropdown
        ObservableList<Tag> oldBookTags = SQLUtils.getBookTags(id);
        ObservableList<Tag> tagList = SQLUtils.getTagList();
        MaxSizedContextMenu maxSizedContextMenu = new MaxSizedContextMenu();
        maxSizedContextMenu.setMaxHeight(200);
        for (Tag tag : tagList) {
            CheckBox checkBox = new CheckBox();

            boolean ifOldTag = oldBookTags.contains(tag);

            checkBox.setSelected(ifOldTag);
            if (ifOldTag) {
                addSelectedTag(tag);
            }

            checkBox.setOnAction(event -> {
                if (checkBox.isSelected()) {
                    addSelectedTag(tag);
                }
                else {
                    removeSelectedTag(tag);
                }
            });

            HBox hBox = new HBox();
            hBox.setSpacing(10);
            hBox.setPrefWidth(150);
            hBox.getChildren().add(checkBox);
            hBox.getChildren().add(tag.getLabel());

            hBox.setOnMouseClicked(event -> {
                if (!checkBox.isSelected()) {
                    addSelectedTag(tag);
                    checkBox.setSelected(true);
                }
                else {
                    removeSelectedTag(tag);
                    checkBox.setSelected(false);
                }
            });

            CustomMenuItem customMenuItem = new CustomMenuItem(hBox);
            customMenuItem.setHideOnClick(false);
            maxSizedContextMenu.getItems().add(customMenuItem);
        }
        tagSelectDropdown.setOnMousePressed(event -> {
            if (!maxSizedContextMenu.isShowing()) {
                Bounds bounds = tagSelectDropdown.localToScreen(tagSelectDropdown.getBoundsInLocal());
                double x = bounds.getMinX();
                double y = bounds.getMaxY() + 5;
                maxSizedContextMenu.show(tagSelectDropdown, x, y);
            } else {
                maxSizedContextMenu.hide();
            }
        });
    }

    public void onChangeCoverButtonClick(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose book cover");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.gif")
        );
        coverFile = fileChooser.showOpenDialog(AppMain.window);
        if (coverFile != null) {
            Image image = new Image(coverFile.toURI().toString());
            if (image.isError()) {
                System.out.println("Error loading image: " + image.getException().getMessage());
            }
            coverImage.setImage(image);
            coverImage.setFitWidth(300);
            coverImage.setPreserveRatio(true);
            coverImage.setSmooth(true);
            coverImage.setCache(true);
        }
    }

    public void onConfirmButtonClick(ActionEvent actionEvent) throws SQLException, IOException {
        if (PopUpWindowUtils.showConfirmation("Warning!", "Are you sure to confirm those changes?")) {
            Connection connection = AppMain.connection;
            String sql = "UPDATE book SET name = ?, author = ?, description = ?, copies = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, titleField.getText());
            preparedStatement.setString(2, authorField.getText());
            preparedStatement.setString(3, descriptionField.getText());
            preparedStatement.setString(4, copiesField.getText());
            preparedStatement.setInt(5, id);
            preparedStatement.executeUpdate();

            String dstFilePath = String.format("./assets/covers/cover%d.png", id);
            // Store cover images in a folder (in practice this is stored in a CDN)
            Files.copy(coverFile.toPath(), (new File(dstFilePath)).toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Tag
            SQLUtils.upsertBookTags(id, tagSelectedList);
            PopUpWindowUtils.showNotification("Done!", "Changes confirmed!", Alert.AlertType.INFORMATION);
            this.onBackButtonClick(actionEvent);
        }
    }

    public void onBackButtonClick(ActionEvent actionEvent) throws SQLException, IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("BookInfoView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();

        BookInfoView tempBookInfoView = fxmlLoader.getController();
        tempBookInfoView.setData(this.id);
    }
}
