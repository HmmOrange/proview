package org.proview.test.Container;

import javafx.scene.control.Label;

public class TagView {
    public Label tagLabel;

    public void setData(String tagName, String bgColorHex, String textColorHex) {
        tagLabel.setText(tagName);

        StringBuilder style = new StringBuilder();

        if (bgColorHex != null) {
            style.append("-fx-background-color: #").append(bgColorHex.replace("#", "")).append("; ");
        }

        if (textColorHex != null) {
            style.append("-fx-text-fill: #").append(textColorHex.replace("#", "")).append("; ");
        }

        if (!style.isEmpty()) {
            tagLabel.setStyle(style.toString());
        } else {
            tagLabel.setStyle(""); // Clear any previous inline styles to fallback to default CSS in 'TagView.css'
        }
    }
}
