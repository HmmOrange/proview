package org.proview.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.util.*;

public class SearchHandler {
    private static String curQuery = null;

    public static void setCurQuery(String curQuery) {
        SearchHandler.curQuery = curQuery;
    }

    public static String getCurQuery() {
        return curQuery;
    }

    public static ObservableList<BookLib> filterBookList(ObservableList<BookLib> bookList) {
        Set<BookLib> bookSet = new LinkedHashSet<>();

        String curQuery = SearchHandler.getCurQuery().toLowerCase();

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
            bookSet.add(i.getKey());
        }

        return FXCollections.observableArrayList(bookSet);
    }
}
