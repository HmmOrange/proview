package org.proview.model.Tag;

import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import org.proview.utils.MaxSizedContextMenu;
import org.proview.utils.SQLUtils;
import org.proview.utils.SearchUtils;

import java.sql.SQLException;
import java.util.Map;

public class TagManagement {
    public static Map<String, TagStyle> tagList;

    public static Map<String, TagStyle> getTagList() throws SQLException {
        if (tagList == null)
            tagList = SQLUtils.getTagMap();

        return tagList;
    }

    private static void addSelectedTag(Tag tag, ObservableList<Tag> tagList, FlowPane tagFlowPane) {
        // Making a cloned tag because a label can only have 1 parent.
        Tag newTag = new Tag(tag.getTagName(), tag.getBgColorHex(), tag.getTextColorHex());
        tagList.add(newTag);
        tagFlowPane.getChildren().add(newTag.getLabel());
    }

    private static void removeSelectedTag(Tag tag, ObservableList<Tag> tagList, FlowPane tagFlowPane) {
        tagList.removeIf(tagInList ->
                tag.getTagName().equals((tagInList.getTagName()))
        );
        tagFlowPane.getChildren().removeIf(node ->
                tag.getTagName().equals(((Label) node).getText())
        );
    }

    public static void loadTagDropdown(
            ObservableList<Tag> tagIncludedList, ObservableList<Tag> oldIncludedList,
            MenuButton tagIncludedSelectDropdown, FlowPane tagIncludedSelectedFlowPane, ObservableList<Tag> tagIncludedSelectedList
    ) throws SQLException {
        tagIncludedList = SQLUtils.getTagList();

        // Tag included dropdown
        MaxSizedContextMenu maxSizedIncludedContextMenu = new MaxSizedContextMenu();
        maxSizedIncludedContextMenu.setMaxHeight(200);
        for (Tag tag : tagIncludedList) {
            CheckBox checkBox = new CheckBox();

            boolean ifOldTag = oldIncludedList.contains(tag);

            checkBox.setSelected(ifOldTag);
            if (ifOldTag) {
                addSelectedTag(tag, tagIncludedSelectedList, tagIncludedSelectedFlowPane);
            }

            checkBox.setOnAction(event -> {
                if (checkBox.isSelected()) {
                    addSelectedTag(tag, tagIncludedSelectedList, tagIncludedSelectedFlowPane);
                }
                else {
                    removeSelectedTag(tag, tagIncludedSelectedList, tagIncludedSelectedFlowPane);
                }
            });

            HBox hBox = new HBox();
            hBox.setSpacing(10);
            hBox.setPrefWidth(150);
            hBox.getChildren().add(checkBox);
            hBox.getChildren().add(tag.getLabel());

            hBox.setOnMouseClicked(event -> {
                if (!checkBox.isSelected()) {
                    addSelectedTag(tag, tagIncludedSelectedList, tagIncludedSelectedFlowPane);
                    checkBox.setSelected(true);
                }
                else {
                    removeSelectedTag(tag, tagIncludedSelectedList, tagIncludedSelectedFlowPane);
                    checkBox.setSelected(false);
                }
            });

            CustomMenuItem customMenuItem = new CustomMenuItem(hBox);
            customMenuItem.setHideOnClick(false);
            maxSizedIncludedContextMenu.getItems().add(customMenuItem);
        }
        tagIncludedSelectDropdown.setOnMousePressed(event -> {
            if (!maxSizedIncludedContextMenu.isShowing()) {
                Bounds bounds = tagIncludedSelectDropdown.localToScreen(tagIncludedSelectDropdown.getBoundsInLocal());
                double x = bounds.getMinX();
                double y = bounds.getMaxY() + 5;
                maxSizedIncludedContextMenu.show(tagIncludedSelectDropdown, x, y);
            } else {
                maxSizedIncludedContextMenu.hide();
            }
        });
    }
}
