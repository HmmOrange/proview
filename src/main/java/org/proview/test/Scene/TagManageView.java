package org.proview.test.Scene;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import org.proview.model.Tag.Tag;
import org.proview.utils.SQLUtils;

import java.sql.SQLException;
import java.util.Comparator;

public class TagManageView {
    public static class RowData {
        private final Tag tag;

        public RowData(Tag tag) {
            this.tag = tag;
        }

        public Tag getTag() {
            return tag;
        }

        public String getCol(int idx) {
            return switch (idx) {
                case 0 -> tag.getTagName();
                case 1 -> tag.getBgColorHex();
                case 2 -> tag.getTextColorHex();
                default -> "";
            };

        }
    }

    public ObservableList<RowData> rowDataList = FXCollections.observableArrayList();
    public TableView<RowData> tagTable;

    public StackPane tagAddPreviewStackPane;
    public TextField tagAddNameLabel;
    public TextField tagAddBgColorHex;
    public TextField tagAddTextColorHex;
    public Label errorAddTextLabel;
    public Label errorAddBgLabel;
    public Label errorAddNameLabel;
    private Tag previewAddTag;
    public Button confirmAddButton;

    public StackPane tagEditPreviewStackPane;
    public TextField tagEditNameLabel;
    public TextField tagEditBgColorHex;
    public TextField tagEditTextColorHex;
    public Label errorEditNameLabel;
    public Label errorEditBgLabel;
    public Label errorEditTextLabel;
    private Tag previewEditTag;
    public Button confirmEditButton;
    private RowData currentlyEditingTag;

    public TextField tagDeleteNameLabel;
    public Button confirmDeleteButton;

    public void initTable() {
        String[] columnName = {"Tag Name", "Tag Background Color", "Tag Text Color", "Tag Preview"};

        // First three columns
        for (int i = 0; i < columnName.length - 1; i++) {
            TableColumn<RowData, String> column = new TableColumn<>(columnName[i]);
            int finalI = i;
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCol(finalI)));
            column.setComparator(Comparator.comparing(String::toString));
            tagTable.getColumns().add(column);
        }

        // For the last column, use Tag & TagView instead of String
        TableColumn<RowData, Tag> tagColumn = new TableColumn<>("Tag");
        tagColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTag()));
        tagColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Tag tag, boolean empty) {
                super.updateItem(tag, empty);
                if (empty || tag == null) {
                    setGraphic(null);
                } else {
                    setGraphic(tag. getLabel());
                }
            }
        });
        tagTable.getColumns().add(tagColumn);

    }

    public void loadNodes() {
        // Add tag tab
        previewAddTag = new Tag();

        tagAddNameLabel.setText("");
        tagAddBgColorHex.setText("");
        tagAddTextColorHex.setText("");

        tagAddPreviewStackPane.getChildren().clear();
        tagAddPreviewStackPane.getChildren().add(previewAddTag.getLabel());

        errorAddNameLabel.setVisible(false);
        errorAddBgLabel.setVisible(false);
        errorAddTextLabel.setVisible(false);

        confirmAddButton.setDisable(true);

        // Edit tag tab
        previewEditTag = new Tag();

        tagEditNameLabel.setText("");
        tagEditBgColorHex.setText("");
        tagEditTextColorHex.setText("");

        tagEditPreviewStackPane.getChildren().clear();
        tagEditPreviewStackPane.getChildren().add(previewEditTag.getLabel());

        errorEditNameLabel.setVisible(false);
        errorEditBgLabel.setVisible(false);
        errorEditTextLabel.setVisible(false);

        confirmEditButton.setDisable(true);

        // Delete tag tab
        tagDeleteNameLabel.setText("");
        confirmDeleteButton.setDisable(true);
    }

    public void reloadTable() throws SQLException {
        rowDataList.clear();
        ObservableList<Tag> tagList = SQLUtils.getTagList();
        for (Tag tag: tagList) {
            rowDataList.add(new RowData(tag));
        }

        tagTable.setItems(rowDataList);
    }
    public void initialize() throws SQLException {
        initTable();
        reloadTable();
        loadNodes();
    }

    private Boolean validAddTagName(String tagName) {
        if (tagName.isEmpty())
            return false;

        for (RowData rowData: rowDataList) {
            if (rowData.getCol(0).equals(tagName)) {
                return false;
            }
        }

        return true;
    }

    private Boolean validEditTagName(String tagName) {
        if (tagName.isEmpty())
            return false;

        for (RowData rowData: rowDataList) {
            if (rowData.getCol(0).equals(tagName)) {
                currentlyEditingTag = rowData;
                return true;
            }
        }

        return false;
    }

    private Boolean validColorHex(String tagColorHex) {
        if (tagColorHex.isEmpty()) {
            return true;
        }

        if (tagColorHex.startsWith("#")) {
            tagColorHex = tagColorHex.substring(1);
        }

        return tagColorHex.matches("^([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$");
    }

    public void onTagAddKeyTyped(KeyEvent keyEvent) {
        String tagName = tagAddNameLabel.getText();
        String bgColorHex = tagAddBgColorHex.getText();
        String textColorHex = tagAddTextColorHex.getText();

        Boolean nameCheck = validAddTagName(tagName);
        Boolean bgColorCheck = validColorHex(bgColorHex);
        Boolean textColorCheck = validColorHex(textColorHex);

        errorAddNameLabel.setVisible(!tagName.isEmpty());
        errorAddBgLabel.setVisible(!bgColorHex.isEmpty());
        errorAddTextLabel.setVisible(!textColorHex.isEmpty());

        if (!nameCheck) {
            errorAddNameLabel.setText("Tag name already exists");
        }
        else {
            errorAddNameLabel.setText("");
        }

        if (!bgColorCheck) {
            errorAddBgLabel.setText("Invalid color hex");
        }
        else {
            errorAddBgLabel.setText("");
        }

        if (!textColorCheck) {
            errorAddTextLabel.setText("Invalid color hex");
        }
        else {
            errorAddTextLabel.setText("");
        }

        if (nameCheck && bgColorCheck && textColorCheck) {
            confirmAddButton.setDisable(false);
        }
        else {
            confirmAddButton.setDisable(true);
        }

        previewAddTag.setTagName(tagName);
        previewAddTag.setStyle(
                bgColorCheck ? bgColorHex : "",
                textColorCheck ? textColorHex : ""
        );
    }

    public void onTagEditKeyTyped(KeyEvent keyEvent) {
        String tagName = tagEditNameLabel.getText();
        String bgColorHex = tagEditBgColorHex.getText();
        String textColorHex = tagEditTextColorHex.getText();

        Boolean nameCheck = validEditTagName(tagName);
        Boolean bgColorCheck = validColorHex(bgColorHex);
        Boolean textColorCheck = validColorHex(textColorHex);

        errorEditNameLabel.setVisible(!tagName.isEmpty());
        errorEditBgLabel.setVisible(!bgColorHex.isEmpty());
        errorEditTextLabel.setVisible(!textColorHex.isEmpty());

        if (!nameCheck) {
            errorEditNameLabel.setText("Tag name does not exist");
        }
        else {
            errorEditNameLabel.setText("");
        }

        if (!bgColorCheck) {
            errorEditBgLabel.setText("Invalid color hex");
        }
        else {
            errorEditBgLabel.setText("");
        }

        if (!textColorCheck) {
            errorEditTextLabel.setText("Invalid color hex");
        }
        else {
            errorEditTextLabel.setText("");
        }

        if (nameCheck && bgColorCheck && textColorCheck) {
            confirmEditButton.setDisable(false);
        }

        previewEditTag.setTagName(tagName);
        previewEditTag.setStyle(
                bgColorCheck
                    ? bgColorHex.isEmpty()
                        ? nameCheck
                            ? currentlyEditingTag.getCol(1)
                            : ""
                        : bgColorHex
                    : "",

                textColorCheck
                    ? textColorHex.isEmpty()
                        ? nameCheck
                            ? currentlyEditingTag.getCol(2)
                            : ""
                        : textColorHex
                    : ""
        );
    }

    public void onTagDeleteKeyTyped(KeyEvent keyEvent) {
        String tagName = tagDeleteNameLabel.getText();
        Boolean nameCheck = validEditTagName(tagName);
        if (nameCheck) {
            errorEditNameLabel.setText("");
            confirmDeleteButton.setDisable(false);
        }
        else {
            errorEditNameLabel.setText("Tag name does not exist");
            confirmDeleteButton.setDisable(true);
        }
    }
    
    public void onConfirmAddButtonClicked(ActionEvent actionEvent) throws SQLException {
        String tagName = tagAddNameLabel.getText();
        String bgColorHex = tagAddBgColorHex.getText();
        String textColorHex = tagAddTextColorHex.getText();

        SQLUtils.addTag(tagName, bgColorHex, textColorHex);
        reloadTable();

        loadNodes();
    }

    public void onConfirmEditButtonClicked(ActionEvent actionEvent) throws SQLException {
        String tagName = tagEditNameLabel.getText();
        String bgColorHex = tagEditBgColorHex.getText();
        String textColorHex = tagEditTextColorHex.getText();

        SQLUtils.updateTag(tagName, bgColorHex, textColorHex);
        reloadTable();

        loadNodes();
    }

    public void onConfirmDeleteButtonClicked(ActionEvent actionEvent) throws SQLException {
        String tagName = tagDeleteNameLabel.getText();

        SQLUtils.removeTag(tagName);
        reloadTable();

        loadNodes();
    }
}
