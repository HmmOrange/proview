package org.proview.test.Container;

import com.google.gson.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.apache.commons.lang3.StringEscapeUtils;
import org.proview.api.GoogleBooksAPI;
import org.proview.test.AppMain;
import org.proview.test.Scene.BookInfoView;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.sql.SQLException;

public class BookCellCardView {
    public ImageView coverImageView;
    public Label titleLabel;
    public Label tagLabel;
    public Label infoLabel;
    public Label copiesLabel;
    public Label authorLabel;
    private int id = -1;

    public void setData(String title, String authors, String imageUrl, String tags) throws IOException {
        titleLabel.setText(title);
        authorLabel.setText(authors);
        tagLabel.setText("Tags: " + tags);
        infoLabel.setText(0 + " 🌟 " + 0 + " 👀 ");
        copiesLabel.setText("View in Google Books 🔗");

        // System.out.println(imageUrl);
        InputStream stream = URI.create(imageUrl).toURL().openStream();
        Image image = new Image(stream);

        coverImageView.setImage(image);
        coverImageView.setFitWidth(112.5);
        coverImageView.setPreserveRatio(true);
        coverImageView.setSmooth(true);
        coverImageView.setCache(true);

        stream.close();
    }

    public void setData(int id, String title, String author, String tags, double rating, int issueCount, int copiesAvailable) throws IOException {
        this.id = id;
        titleLabel.setText(title);
        authorLabel.setText(author);
        tagLabel.setText("Tags: " + tags);
        infoLabel.setText(rating + " 🌟 " + issueCount + " 👀 ");

        if (copiesAvailable >= 0)
            copiesLabel.setText(copiesAvailable + (copiesAvailable == 1 ? " copy" : " copies") + " available");
        else {
            copiesLabel.setDisable(true);
            copiesLabel.setVisible(false);
        }

        String imageUrl = "./assets/covers/cover" + id + ".png";
        InputStream stream = new FileInputStream(imageUrl);
        Image image = new Image(stream);

        coverImageView.setImage(image);

        double targetWidth = 100;
        double targetHeight = 125;
        double scaleX = targetWidth / image.getWidth();
        double scaleY = targetHeight / image.getHeight();
        double scale = Math.max(scaleX, scaleY);

        coverImageView.setImage(image);
        coverImageView.setFitWidth(image.getWidth() * scale);
        coverImageView.setFitHeight(image.getHeight() * scale);
        coverImageView.setPreserveRatio(true);
        Rectangle clip = new Rectangle(targetWidth, targetHeight);
        coverImageView.setClip(clip);
        coverImageView.setSmooth(true);
        coverImageView.setCache(true);

        stream.close();
    }

    public void onMouseClick(ActionEvent actionEvent) throws IOException, SQLException {
        if (id >= 0) { // this seems tricky, maybe there is a better way to handle this
            FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("BookInfoView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
            AppMain.window.setTitle("Hello!");
            AppMain.window.setScene(scene);
            AppMain.window.centerOnScreen();

            BookInfoView tempBookInfoView = fxmlLoader.getController();
            tempBookInfoView.setData(this.id);
        } else {
            String previewLink = "";
            String response = GoogleBooksAPI.getBooksFromAPI(titleLabel.getText());
            JsonParser parser = new JsonParser();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            JsonElement el = parser.parse(response);
            response = gson.toJson(el); // done

            response = StringEscapeUtils.unescapeJava(response);

            JsonObject jsonObject = el.getAsJsonObject();
            JsonArray items = jsonObject.getAsJsonArray("items");

            if (items != null && items.size() > 0) {
                JsonObject volumeInfo = items.get(0).getAsJsonObject().getAsJsonObject("volumeInfo");
                previewLink = volumeInfo.get("previewLink").getAsString();
                System.out.println("Preview Link: " + previewLink);
            } else {
                System.out.println("Không tìm thấy previewLink trong phản hồi JSON.");
            }

            URL url = URI.create(previewLink).toURL();
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(url.toString()));
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Desktop không được hỗ trợ.");
            }
        }
    }

}
