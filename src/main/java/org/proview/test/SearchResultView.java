package org.proview.test;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.proview.model.BookCell;
import org.proview.model.BookManagement;
import org.proview.model.SearchHandler;
import java.io.IOException;
import java.sql.SQLException;

public class SearchResultView {
    public ListView<BookCell> topResultListView;

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
        ObservableList<BookCell> bookList = BookManagement.getTopRatedBookCellList();
        ObservableList<BookCell> filteredBookList = SearchHandler.filterBookList(bookList);

        initList(topResultListView, filteredBookList);

        topResultListView.setMinHeight(150 * filteredBookList.size() + 10);

        topResultListView.setMinWidth(500 + 10);
    }
}
