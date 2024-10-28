package org.proview.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Locale;

public class SearchHandler {
    private static String curQuery = null;

    public static void setCurQuery(String curQuery) {
        SearchHandler.curQuery = curQuery;
    }

    public static String getCurQuery() {
        return curQuery;
    }

    public static ObservableList<BookCell> filterBookList(ObservableList<BookCell> bookList) {
        ObservableList<BookCell> newList = FXCollections.observableArrayList();
        String curQuery = SearchHandler.getCurQuery().toLowerCase();

        // Search by whole query first
        for (var i : bookList) {
            if (i.getTitle().toLowerCase().contains(curQuery) || i.getAuthor().toLowerCase().contains(curQuery))
                newList.add(i);
        }

        return newList;
    }
}
