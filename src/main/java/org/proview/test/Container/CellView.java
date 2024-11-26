package org.proview.test.Container;

import java.io.IOException;
import java.sql.SQLException;

public abstract class CellView {
    public abstract void setData(String title, String authors, String imageUrl, String tags) throws IOException, SQLException;
    public abstract void setData(int id, String title, String author, String tags, double rating, int issueCount, int copiesAvailable) throws IOException, SQLException;
}
