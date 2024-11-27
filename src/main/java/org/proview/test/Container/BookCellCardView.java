package org.proview.test.Container;

import com.google.gson.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.lang3.StringEscapeUtils;
import org.kordamp.ikonli.javafx.FontIcon;
import org.proview.api.GoogleBooksAPI;
import org.proview.modal.Tag.TagManagement;
import org.proview.modal.Tag.TagStyle;
import org.proview.test.AppMain;
import org.proview.test.Scene.BookInfoView;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.sql.SQLException;
import java.util.Map;

public class BookCellCardView extends CellView {
    public ImageView coverImageView;
    public Label titleLabel;
    public HBox tagHBox;
    public Label ratingLabel;
    public Label issuesLabel;
    public Label copiesLabel;
    public Label authorLabel;
    public FontIcon starRatingIcon;
    public FontIcon issuesIcon;
    public HBox infoHBox;
    private int id = -1;

    // For Google books
    @Override
    public void setData(String title, String authors, String imageUrl, String tags) throws IOException, SQLException {
        titleLabel.setText(title);
        authorLabel.setText(authors);
        copiesLabel.setText("View in Google Books 🔗");

        infoHBox.getChildren().clear();
        infoHBox.setPrefWidth(0);

        // System.out.println(imageUrl);
        InputStream stream = URI.create(imageUrl).toURL().openStream();
        Image image = new Image(stream);

        coverImageView.setImage(image);

        double targetWidth = 83.333;
        double targetHeight = 125;
        double scaleX = targetWidth / image.getWidth();
        double scaleY = targetHeight / image.getHeight();
        double scale = Math.min(scaleX, scaleY);

        double scaledWidth = image.getWidth() * scale;
        double scaledHeight = image.getHeight() * scale;

        double viewportX = Math.max(0, (scaledWidth - targetWidth) / 2 / scale);
        double viewportY = Math.max(0, (scaledHeight - targetHeight) / 2 / scale);
        double viewportWidth = Math.min(image.getWidth(), targetWidth / scale);
        double viewportHeight = Math.min(image.getHeight(), targetHeight / scale);

        coverImageView.setViewport(new Rectangle2D(viewportX, viewportY, viewportWidth, viewportHeight));
        coverImageView.setFitWidth(targetWidth);
        coverImageView.setFitHeight(targetHeight);
        coverImageView.setPreserveRatio(false);
        coverImageView.setSmooth(true);
        coverImageView.setCache(true);

        stream.close();

        // Tags
        String[] tagList = tags.replaceAll(" {2}", " ").replaceAll(", ", ",").split(",");
        Map<String, TagStyle> tagMap = TagManagement.getTagList();

        for (String tag : tagList) {
            FXMLLoader loader = new FXMLLoader(AppMain.class.getResource("TagView.fxml"));
            Label tagLabel = loader.load();
            TagView tagView = loader.getController();

            String bgColorHex = null;
            String textColorHex = null;
            if (tagMap.containsKey(tag)) {
                bgColorHex = tagMap.get(tag).getBgColorHex();
                textColorHex = tagMap.get(tag).getTextColorHex();
            }

            tagView.setData(
                    tag,
                    bgColorHex,
                    textColorHex
            );
            tagHBox.getChildren().add(tagLabel);
        }
    }

    // For library books
    public void setData(int id, String title, String author, String tags, double rating, int issueCount, int copiesAvailable) throws IOException, SQLException {
        this.id = id;
        titleLabel.setText(title);
        authorLabel.setText(author);
        ratingLabel.setText(String.format("%.2f", rating));
        issuesLabel.setText(String.valueOf(issueCount));

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

        double targetWidth = 83.333;
        double targetHeight = 125;
        double scaleX = targetWidth / image.getWidth();
        double scaleY = targetHeight / image.getHeight();
        double scale = Math.min(scaleX, scaleY);

        double scaledWidth = image.getWidth() * scale;
        double scaledHeight = image.getHeight() * scale;

        double viewportX = Math.max(0, (scaledWidth - targetWidth) / 2 / scale);
        double viewportY = Math.max(0, (scaledHeight - targetHeight) / 2 / scale);
        double viewportWidth = Math.min(image.getWidth(), targetWidth / scale);
        double viewportHeight = Math.min(image.getHeight(), targetHeight / scale);

        coverImageView.setViewport(new Rectangle2D(viewportX, viewportY, viewportWidth, viewportHeight));
        coverImageView.setFitWidth(targetWidth);
        coverImageView.setFitHeight(targetHeight);
        coverImageView.setPreserveRatio(false);
        coverImageView.setSmooth(true);
        coverImageView.setCache(true);

        stream.close();

        // Tags
        String[] tagList = tags.replaceAll(" {2}", " ").replaceAll(", ", ",").split(",");
        Map<String, TagStyle> tagMap = TagManagement.getTagList();

        for (String tag : tagList) {
            FXMLLoader loader = new FXMLLoader(AppMain.class.getResource("TagView.fxml"));
            Label tagLabel = loader.load();
            TagView tagView = loader.getController();

            String bgColorHex = null;
            String textColorHex = null;
            if (tagMap.containsKey(tag)) {
                bgColorHex = tagMap.get(tag).getBgColorHex();
                textColorHex = tagMap.get(tag).getTextColorHex();
            }

            tagView.setData(
                    tag,
                    bgColorHex,
                    textColorHex
            );
            tagHBox.getChildren().add(tagLabel);
        }
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
