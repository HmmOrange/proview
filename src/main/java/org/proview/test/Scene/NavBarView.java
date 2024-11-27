package org.proview.test.Scene;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.proview.modal.Game.GameActivity;
import org.proview.modal.User.Admin;
import org.proview.modal.User.NormalUser;
import org.proview.utils.SearchUtils;
import org.proview.modal.User.UserManagement;
import org.proview.test.AppMain;
import org.proview.utils.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class NavBarView {
    public TextField bookSearchBar;
    public Button libraryButton;
    public Button logoutButton;
    public Button issueButton;
    public HBox navBarHBox;
    public Circle avatarImageCircle;

    public void loadProfileButton() throws IOException {
            // Load CSS
            InputStream stream = new FileInputStream(UserManagement.getCurrentUser().getAvatarUrl());
            Image image = new Image(stream);
            stream.close();

            avatarImageCircle.setFill(new ImagePattern(image));
            avatarImageCircle.setOnMouseClicked(event -> {
                try {
                    onProfileButtonClick(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    public void initialize() throws IOException {
        if (UserManagement.getCurrentUser() instanceof Admin) {
            issueButton.setText("Manage");
        }
        String curQuery = SearchUtils.getCurQuery();
        if (curQuery != null) {
            bookSearchBar.setText(curQuery);
        }

        loadProfileButton();
    }

    public void onLogoutButtonClick(ActionEvent actionEvent) throws IOException {
        UserManagement.setCurrentUser(null);
        ProfileView.resetBookList();
        Utils.switchScene("LoginView.fxml");
    }

    public void onSearchButtonClick(ActionEvent actionEvent) throws IOException {
        String curQuery = bookSearchBar.getText();
        if (curQuery != null) {
            SearchUtils.setCurQuery(curQuery);
            Utils.switchScene("SearchResultView.fxml");
        }
    }

    public void onLibraryButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("LibraryView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }

    public void onIssueButtonClick(ActionEvent actionEvent) throws IOException {
        Utils.switchScene("IssueListView.fxml");
    }

    public void onHomeButtonClick(ActionEvent actionEvent) throws IOException {
        Utils.switchScene("HomeView.fxml");
    }

    public void onProfileButtonClick(MouseEvent mouseClick) throws IOException {
        ProfileView.setUser(UserManagement.getCurrentUser());
        Utils.switchScene("ProfileView.fxml");
    }

    public void onGameButtonClicked(ActionEvent actionEvent) throws IOException, SQLException {
        Utils.switchScene("StartGameView.fxml");
    }

    public void onDashboardButtonClicked(ActionEvent actionEvent) throws IOException {
        if (UserManagement.getCurrentUser() instanceof NormalUser) {
            Utils.switchScene("NormalUserDashboardView.fxml");
        }
        else {
            Utils.switchScene("AdminDashboardView.fxml");
        }
    }

    public void setButtonActive(Button button) {


        button.getStylesheets().add("nav-button-active");
    }
}

