package org.proview.modal.Tag;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import org.proview.test.AppMain;
import org.proview.test.Container.TagView;

import java.io.IOException;

public class Tag extends TagStyle {
    private Label label;
    private TagView controller;
    private String tagName;

    public Tag(String tagName, String bgColorHex, String textColorHex) {
        super(bgColorHex, textColorHex);

        this.tagName = tagName;
    }

    public Tag() {
        super("#6c757d", "#ced4da");

        this.tagName = "Tag";
    }

    public String getTagName() {
        return tagName;
    }

    public Label getLabel() {
        if (label == null) {
            try {
                FXMLLoader loader = new FXMLLoader(AppMain.class.getResource("TagView.fxml"));
                label = loader.load();
                controller = loader.getController();
                controller.setData(
                        tagName,
                        super.getBgColorHex(),
                        super.getTextColorHex()
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return label;
    }

    public void setTagName(String tagName) {
        if (tagName == null || tagName.isEmpty()) {
            controller.setName("Tag");
        }
        else {
            controller.setName(tagName);
        }
        this.tagName = tagName;
    }

    public void setStyle(String bgColorHex, String textColorHex) {
        controller.setStyle(bgColorHex, textColorHex);
    }

    @Override
    public String toString() {
        return tagName;
    }
}
