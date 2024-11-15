package org.proview.test.Scene;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.proview.modal.User.NormalUser;
import org.proview.modal.Utils.SearchUtils;
import org.proview.modal.User.Admin;
import org.proview.modal.User.UserManagement;
import org.proview.test.AppMain;

import java.io.IOException;

public class NavBarView {
    public TextField bookSearchBar;
    public Button libraryButton;
    public Button logoutButton;
    public Button editBookButton;
    public Button issueButton;
    public Button profileButton;

    public void initialize() {
        if (UserManagement.getCurrentUser() instanceof NormalUser) {
            editBookButton.setVisible(false);
            editBookButton.setDisable(true);
        }
        String curQuery = SearchUtils.getCurQuery();
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
            SearchUtils.setCurQuery(curQuery);

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
                SearchUtils.setCurQuery(curQuery);

                FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("SearchResultView.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
                AppMain.window.setScene(scene);
                AppMain.window.centerOnScreen();
            }
        }
    }

    public void onLibraryButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("LibraryView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }

    public void onIssueButtonClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("IssueListView.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 1300, 700);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }

    public void onHomeButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("HomeView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }

    public void onProfileButtonClick(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("ProfileView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
            AppMain.window.setTitle("Hello!");
            AppMain.window.setScene(scene);
            AppMain.window.centerOnScreen();
        } catch (IOException e) {
            System.out.println("Error loading ProfileView.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
