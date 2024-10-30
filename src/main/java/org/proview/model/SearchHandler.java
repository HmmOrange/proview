package org.proview.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import javax.print.Doc;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SearchHandler {
    private static String curQuery = null;

    public static void setCurQuery(String curQuery) {
        SearchHandler.curQuery = curQuery;
    }

    public static String getCurQuery() {
        return curQuery;
    }

    public static ObservableList<BookCell> filterBookList(ObservableList<BookCell> bookList) {
        Set<BookCell> bookSet = new LinkedHashSet<>();

        String curQuery = SearchHandler.getCurQuery().toLowerCase();

        // Search by whole query first
        for (var i : bookList) {
            if (i.getTitle().toLowerCase().contains(curQuery) || i.getAuthor().toLowerCase().contains(curQuery))
                bookSet.add(i);
        }

        // Search by partial token sorting ratio
        Map<BookCell, Integer> ratioList = new HashMap<>();
        for (var i : bookList) {
            System.out.println(i.getTitle() + " " + FuzzySearch.tokenSetRatio(curQuery, i.getTitle()));
            ratioList.put(i, FuzzySearch.tokenSetRatio(curQuery, i.getTitle()));
        }

        ArrayList<Map.Entry<BookCell, Integer>> sortedList = new ArrayList<>(ratioList.entrySet());
        sortedList.sort(Map.Entry.<BookCell, Integer>comparingByValue().reversed());

        for (var i : sortedList) {
            bookSet.add(i.getKey());
        }

        return FXCollections.observableArrayList(bookSet);
    }
}
