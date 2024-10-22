package org.proview.test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import org.proview.model.BookCell;
import org.proview.model.UserManagement;

import java.io.IOException;

public class HomeView {
    public ListView<BookCell> topRatedBookListView;

    public void initialize() {
        ObservableList<BookCell> bookList = FXCollections.observableArrayList(
                new BookCell("#1. Book #1", "Drama, Slice of Life", 4.6, 60, "./assets/covers/cover1.png", 3),
                new BookCell("#2. Book #2", "Action, Guns, Comedy", 4.49, 36, "./assets/covers/cover2.png", 5)
        );

        topRatedBookListView.setItems(bookList);
        topRatedBookListView.setCellFactory(param -> new ListCell<>() {
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
                                item.getTitle(),
                                item.getTags(),
                                item.getRating(),
                                item.getIssueCount(),
                                item.getImagePath(),
                                item.getCopiesAvailable()
                        );

                        setGraphic(hbox);
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }


    public void onEditBookButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("EditBookView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        AppMain.window.setTitle("Edit book");
        AppMain.window.setScene(scene);
    }

    public void onLogoutButtonClick(ActionEvent actionEvent) throws IOException {
        UserManagement.setCurrentUser(null);

        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        AppMain.window.setTitle("Login!");
        AppMain.window.setScene(scene);
    }
}
