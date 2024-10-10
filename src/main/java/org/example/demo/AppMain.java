package org.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppMain extends Application {
    protected static Stage window;

    @Override
    public void start(Stage stage) throws IOException {
        window = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("LoginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        window.setTitle("Hello!");
        window.setScene(scene);
        window.setMinWidth(500);
        window.setMinHeight(500);
        window.show();
    }

    public static void main(String[] args) {
        launch();
    }
}