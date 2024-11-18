package org.proview.test.Scene;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import org.proview.modal.User.NormalUser;
import org.proview.utils.SearchUtils;
import org.proview.modal.User.UserManagement;
import org.proview.test.AppMain;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class NavBarView {
    public TextField bookSearchBar;
    public Button libraryButton;
    public Button logoutButton;
    public Button editBookButton;
    public Button issueButton;
    public Button profileButton;
    public HBox navBarHBox;

        public void loadProfileButton() throws FileNotFoundException {
            // Load CSS
            String cssPath = Objects.requireNonNull(AppMain.class.getResource("styles/NavBarView.css")).toExternalForm();
            navBarHBox.getStylesheets().add(cssPath);

            InputStream stream = new FileInputStream(UserManagement.getCurrentUser().getAvatarUrl());
            Image image = new Image(stream);
            ImageView avatarImage = new ImageView(image);

            double radius = 25;
            avatarImage.setFitWidth(2 * radius);
            avatarImage.setFitHeight(2 * radius);
            avatarImage.setClip(new Circle(radius, radius, radius));
            avatarImage.setCache(true);

            Circle circle = new Circle(radius);
            profileButton.setGraphic(avatarImage);
            profileButton.setShape(circle);
            profileButton.setMinSize(2 * radius, 2 * radius);
            profileButton.setMaxSize(2 * radius, 2 * radius);
            profileButton.setId("profile-button");
        }
    public void initialize() throws FileNotFoundException {
        if (UserManagement.getCurrentUser() instanceof NormalUser) {
            editBookButton.setVisible(false);
            editBookButton.setDisable(true);
        }
        String curQuery = SearchUtils.getCurQuery();
        if (curQuery != null) {
            bookSearchBar.setText(curQuery);
        }

        loadProfileButton();
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
        ProfileView.resetBookList();
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

