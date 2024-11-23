package org.proview.test.Scene;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import org.proview.modal.Game.GameActivity;
import org.proview.test.AppMain;

import java.io.IOException;
import java.sql.SQLException;

public class StartGameView {
    public void onStartButtonClicked(ActionEvent actionEvent) throws SQLException, IOException {
        GameActivity.restartGame();
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("GameView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }
}
