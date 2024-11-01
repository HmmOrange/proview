package org.proview.test;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.proview.model.SearchHandler;
import org.proview.model.UserManagement;

import java.io.IOException;

public class NavBarView {
    public TextField bookSearchBar;

    public void initialize() {
        String curQuery = SearchHandler.getCurQuery();
        if (curQuery != null) {
            bookSearchBar.setText(curQuery);
        }
    }

    public void onEditBookButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("EditBookView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }

    public void onLogoutButtonClick(ActionEvent actionEvent) throws IOException {
        UserManagement.setCurrentUser(null);

        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }

    public void onSearchButtonClick(ActionEvent actionEvent) throws IOException {
        String curQuery = bookSearchBar.getText();
        if (curQuery != null) {
            SearchHandler.setCurQuery(curQuery);

            FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("SearchResultView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
            AppMain.window.setScene(scene);
            AppMain.window.centerOnScreen();
        }
    }

    public void setBookSearchBar(String query) {
        bookSearchBar.setText(query);
    }

    public void onKeyReleased(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String curQuery = bookSearchBar.getText();
            if (curQuery != null) {
                SearchHandler.setCurQuery(curQuery);

                FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("SearchResultView.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
                AppMain.window.setScene(scene);
                AppMain.window.centerOnScreen();
            }
        }
    }
}
