package org.proview.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.proview.modal.Book.BookLib;

import java.util.*;

public class SearchUtils {
    private static String curQuery = null;

    public static void setCurQuery(String curQuery) {
        SearchUtils.curQuery = curQuery;
    }

    public static String getCurQuery() {
        return curQuery;
    }

    public static ObservableList<BookLib> filterBookList(String curQuery, ObservableList<BookLib> bookList) {
        Set<BookLib> bookSet = new LinkedHashSet<>();

        // Search by whole query first
        for (var i : bookList) {
            if (i.getTitle().toLowerCase().contains(curQuery) || i.getAuthor().toLowerCase().contains(curQuery))
                bookSet.add(i);
        }

        // Search titles & authors with partial token sorting ratio
        Map<BookLib, Integer> ratioList = new HashMap<>();
        for (var i : bookList) {
            System.out.println(i.getTitle() + " " + FuzzySearch.tokenSetRatio(curQuery, i.getTitle() + " " + i.getAuthor()));
            ratioList.put(i, FuzzySearch.tokenSetRatio(curQuery, i.getTitle()));
        }

        // Sort by descending score
        ArrayList<Map.Entry<BookLib, Integer>> sortedList = new ArrayList<>(ratioList.entrySet());
        sortedList.sort(Map.Entry.<BookLib, Integer>comparingByValue().reversed());

        for (var i : sortedList) {
            if (i.getValue() > 50) bookSet.add(i.getKey());
        }

        return FXCollections.observableArrayList(bookSet);
    }

}
