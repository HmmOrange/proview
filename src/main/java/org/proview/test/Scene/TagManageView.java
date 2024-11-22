package org.proview.test.Scene;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import org.proview.modal.Tag.Tag;
import org.proview.utils.SQLUtils;

import java.sql.SQLException;
import java.util.Comparator;

public class TagManageView {
    public StackPane tagAddPreviewStackPane;

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
    public TextField tagEditNameLabel;
    public TextField tagEditBgColorHex;
    public TextField tagEditTextColorHex;

    public Label tagAddPreviewLabel;
    public Label tagEditPreviewLabel;
    public Label errorAddTextLabel;
    public Label errorAddBgLabel;
    public Label errorAddNameLabel;

    private Tag previewAddTag;
    private Tag previewEditTag;

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
        previewAddTag = new Tag();
        previewEditTag = new Tag();

        tagAddPreviewStackPane.getChildren().clear();
        tagAddPreviewStackPane.getChildren().add(previewAddTag.getLabel());

        errorAddNameLabel.setVisible(false);
        errorAddBgLabel.setVisible(false);
        errorAddTextLabel.setVisible(false);
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

    public void onTagAddKeyTyped(KeyEvent keyEvent) {

    }
}
