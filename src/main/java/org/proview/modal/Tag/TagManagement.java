package org.proview.modal.Tag;

import org.proview.utils.SQLUtils;

import java.sql.SQLException;
import java.util.Map;

public class TagManagement {
    public static Map<String, TagStyle> tagList;

    public static Map<String, TagStyle> getTagList() throws SQLException {
        if (tagList == null)
            tagList = SQLUtils.getTagMap();

        return tagList;
    }
}
