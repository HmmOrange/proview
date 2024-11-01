package org.proview.test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.proview.model.BookCell;
import org.proview.model.BookManagement;
import org.proview.model.UserManagement;

import java.io.IOException;
import java.sql.SQLException;

public class HomeView {
    public ListView<BookCell> topRatedBookListView;
    public ListView<BookCell> trendingBookListView;
    public TextField bookSearchBar;

    public void initList(ListView<BookCell> bookListView, ObservableList<BookCell> bookList) {
        bookListView.setItems(bookList);
        bookListView.setCellFactory(param -> new ListCell<>() {
            {
                setStyle("-fx-padding: 0px; -fx-margin: 0px; -fx-background-insets: 0px; -fx-border-insets: 0px;");
            }
            @Override
            protected void updateItem(BookCell item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(AppMain.class.getResource("BookCellView.fxml"));
                        HBox hbox = loader.load();

                        // Get the controller of the cell
                        BookCellView cellView = loader.getController();

                        cellView.setData(
                                item.getId(),
                                "#" + (getIndex() + 1) + ". " + item.getTitle(),
                                item.getAuthor(),
                                item.getTags(),
                                item.getRating(),
                                item.getIssueCount(),
                                item.getImagePath(),
                                item.getCopiesAvailable()
                        );

                        setGraphic(hbox);
                    } catch (Exception e) {
                        System.out.println(e);
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
    public void initialize() throws SQLException {
        ObservableList<BookCell> topRatedList = BookManagement.getTopRatedBookCellList();
        ObservableList<BookCell> trendingList = BookManagement.getTrendingBookCellList();

        initList(topRatedBookListView, topRatedList);
        initList(trendingBookListView, trendingList);

        // Make the list view non-scrollable (there is probably a better way to do this)
        topRatedBookListView.setMinHeight(150 * topRatedList.size() + 10);
        trendingBookListView.setMinHeight(150 * trendingList.size() + 10);

        topRatedBookListView.setMinWidth(500 + 10);
        trendingBookListView.setMinWidth(500 + 10);
    }
}
