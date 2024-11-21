package org.proview.modal.Tag;

public class Tag extends TagStyle {
    private String tagName;

    public Tag(String tagName, String bgColorHex, String textColorHex) {
        super(bgColorHex, textColorHex);

        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

    @Override
    public String toString() {
        return tagName;
    }
}
