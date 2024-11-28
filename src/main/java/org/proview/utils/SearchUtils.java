package org.proview.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.proview.model.Book.BookLib;
import org.proview.model.Tag.Tag;

import java.sql.SQLException;
import java.util.*;

public class SearchUtils {
    private static String curQuery = null;
    private static ObservableList<Tag> tagIncludedList;
    private static ObservableList<Tag> tagExcludedList;
    private static double lowRating = 0.0;
    private static double highRating = 5.0;

    public static ObservableList<Tag> getTagIncludedList() {
        if (tagIncludedList == null)
            tagIncludedList = FXCollections.observableArrayList();
        return tagIncludedList;
    }

    public static ObservableList<Tag> getTagExcludedList() {
        if (tagExcludedList == null)
            tagExcludedList = FXCollections.observableArrayList();
        return tagExcludedList;
    }

    public static double getLowRating() {
        return lowRating;
    }

    public static double getHighRating() {
        return highRating;
    }

    public static void setCurQuery(String curQuery) {
        SearchUtils.curQuery = curQuery;
    }

    public static void setTagIncludedList(ObservableList<Tag> tagIncludedList) {
        SearchUtils.tagIncludedList = tagIncludedList;
    }

    public static void setTagExcludedList(ObservableList<Tag> tagExcludedList) {
        SearchUtils.tagExcludedList = tagExcludedList;
    }

    public static void setLowRating(double lowRating) {
        SearchUtils.lowRating = lowRating;
    }

    public static void setHighRating(double highRating) {
        SearchUtils.highRating = highRating;
    }

    public static String getCurQuery() {
        return curQuery;
    }

    public static ObservableList<BookLib> filterBookList(ObservableList<BookLib> bookList) throws SQLException {
        curQuery = curQuery.toLowerCase();

        if (tagIncludedList == null)
            tagIncludedList = FXCollections.observableArrayList();

        if (tagExcludedList == null)
            tagExcludedList = FXCollections.observableArrayList();

        ObservableList<BookLib> filteredList = FXCollections.observableArrayList();
        filteredList.addAll(bookList);

        for (BookLib bookLib : bookList) {
            ObservableList<Tag> tagList = bookLib.getTagList();

            boolean qualifiedTag = true;

            if (!tagIncludedList.isEmpty()) {
                for (Tag tag : tagIncludedList) {
                    if (!tagList.contains(tag)) {
                        qualifiedTag = false;
                        break;
                    }
                }
            }

            if (qualifiedTag) {
                for (Tag tag : tagList) {
                    if (tagExcludedList.contains(tag)) {
                        qualifiedTag = false;
                        break;
                    }
                }
            }

            if (qualifiedTag) {
                if (lowRating > bookLib.getRating() || highRating < bookLib.getRating()) {
                    qualifiedTag = false;
                }
            }

            if (!qualifiedTag) filteredList.remove(bookLib);
        }

        Set<BookLib> bookSet = new LinkedHashSet<>();

        // Search by whole query first
        for (var i : filteredList) {
            if (i.getTitle().toLowerCase().contains(curQuery) || i.getAuthor().toLowerCase().contains(curQuery))
                bookSet.add(i);
        }

        // Search titles & authors with partial token sorting ratio
        Map<BookLib, Integer> ratioList = new HashMap<>();
        for (var i : filteredList) {
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
