package org.proview.model.Tag;

public class TagStyle {
    private String bgColorHex;
    private String textColorHex;

    public TagStyle(String bgColorHex, String textColorHex) {
        this.bgColorHex = bgColorHex;
        this.textColorHex = textColorHex;
    }

    public String getBgColorHex() {
        return bgColorHex;
    }

    public String getTextColorHex() {
        return textColorHex;
    }
}