package org.proview.modal.Tag;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import org.proview.test.AppMain;
import org.proview.test.Container.TagView;

import java.io.IOException;

public class Tag extends TagStyle {
    private Label label;
    private String tagName;

    public Tag(String tagName, String bgColorHex, String textColorHex) {
        super(bgColorHex, textColorHex);

        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

    public Label getLabel() {
        if (label == null) {
            try {
                FXMLLoader loader = new FXMLLoader(AppMain.class.getResource("TagView.fxml"));
                label = loader.load();
                TagView tagView = loader.getController();
                tagView.setData(
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

    public void setLabel(String tagName, String bgColorHex, String textColorHex) {

    }
    @Override
    public String toString() {
        return tagName;
    }
}
