package org.proview.test.Container;

import javafx.scene.control.Label;

public class TagView {
    public Label tagLabel;

    public void setData(String tagName, String bgColorHex, String textColorHex) {
        setName(tagName);
        setStyle(bgColorHex, textColorHex);
    }

    public void setName(String name) {
        tagLabel.setText(name);
    }

    public void setStyle(String bgColorHex, String textColorHex) {
        StringBuilder style = new StringBuilder();

        if (bgColorHex != null) {
            if (bgColorHex.startsWith("#"))
                bgColorHex = bgColorHex.substring(1);
            if (bgColorHex.isEmpty() || (bgColorHex.length() != 3 && bgColorHex.length() != 6))
                bgColorHex = "6c757d";
            style.append("-fx-background-color: #").append(bgColorHex).append("; ");
        }

        if (textColorHex != null) {
            if (textColorHex.startsWith("#"))
                textColorHex = textColorHex.substring(1);
            if (textColorHex.isEmpty() || (textColorHex.length() != 3 && textColorHex.length() != 6))
                textColorHex = "ced4da";
            style.append("-fx-text-fill: #").append(textColorHex).append("; ");
        }

        if (!style.isEmpty()) {
            tagLabel.setStyle(style.toString());
        } else {
            tagLabel.setStyle(""); // Clear any previous inline styles to fallback to default CSS in 'TagView.css'
        }
    }
}
