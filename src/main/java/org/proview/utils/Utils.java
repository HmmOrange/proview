package org.proview.utils;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.proview.test.AppMain;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class Utils {
    public static void insertBookImage(ImageView imageView, Image image, double targetWidth, double targetHeight) {
        imageView.setImage(image);
        double scaleX = targetWidth / image.getWidth();
        double scaleY = targetHeight / image.getHeight();
        double scale = Math.min(scaleX, scaleY);

        double scaledWidth = image.getWidth() * scale;
        double scaledHeight = image.getHeight() * scale;
        double viewportX = Math.max(0, (scaledWidth - targetWidth) / 2 / scale);
        double viewportY = Math.max(0, (scaledHeight - targetHeight) / 2 / scale);
        double viewportWidth = Math.min(image.getWidth(), targetWidth / scale);
        double viewportHeight = Math.min(image.getHeight(), targetHeight / scale);

        imageView.setViewport(new Rectangle2D(viewportX, viewportY, viewportWidth, viewportHeight));
        imageView.setFitWidth(targetWidth);
        imageView.setFitHeight(targetHeight);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);
        imageView.setCache(true);
    }

    public static void insertBookImage(ImageView imageView, String imageUrl, double targetWidth, double targetHeight) throws IOException {
        InputStream stream = URI.create(imageUrl).toURL().openStream();
        Image image = new Image(stream);

        insertBookImage(imageView, image, targetWidth, targetHeight);

        stream.close();
    }

    public static void insertBookImage(ImageView imageView, int id, double targetWidth, double targetHeight) throws FileNotFoundException {
        String imageUrl = "./assets/covers/cover" + id + ".png";
        InputStream stream = new FileInputStream(imageUrl);
        Image image = new Image(stream);

        insertBookImage(imageView, image, targetWidth, targetHeight);
    }

    public static void switchScene(String fxmlPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource(fxmlPath));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }
}
