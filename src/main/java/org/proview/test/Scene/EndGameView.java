package org.proview.test.Scene;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import org.proview.modal.Game.GameActivity;
import org.proview.test.AppMain;

import java.io.IOException;
import java.sql.SQLException;

public class EndGameView {
    public Label scoreLabel;

    public void initialize() throws SQLException {
        if (GameActivity.getScore() > GameActivity.getHighScore()) {
            scoreLabel.setText("End game. New High Score: " + GameActivity.getScore());
        } else {
            scoreLabel.setText("End game. Final Score: " + GameActivity.getScore());
        }
    }

    public void onRestartButtonClicked(ActionEvent actionEvent) throws SQLException, IOException {
        GameActivity.restartGame();
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("GameView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }

    public void onBackButtonClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("HomeView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }
}
