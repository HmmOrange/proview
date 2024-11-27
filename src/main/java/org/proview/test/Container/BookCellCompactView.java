package org.proview.test.Container;

import com.google.gson.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;

public class BookCellCompactView extends CellView {
    public ImageView coverImageView;
    public Label titleLabel;
    public HBox tagHBox;
    public Label copiesLabel;
    public Label authorLabel;
    public Label issuesLabel;
    public Label ratingLabel;
    public FontIcon issuesIcon;
    public FontIcon starRatingIcon;
    public HBox infoHBox;
    private int id = -1;

    @Override
    public void setData(String title, String authors, String imageUrl, String tags) throws IOException, SQLException {
        super.setData(
                titleLabel, title,
                authorLabel, authors,
                copiesLabel,
                infoHBox,
                imageUrl, coverImageView,
                60, 90,
                tags, tagHBox
        );
    }

    public void setData(int id, String title, String author, double rating, int issueCount, int copiesAvailable) throws IOException, SQLException {
        this.id = id;
        super.setData(
                titleLabel, title,
                authorLabel, author,
                ratingLabel, rating,
                issuesLabel, issueCount,
                copiesLabel, copiesAvailable,
                coverImageView, id,
                60, 90,
                tagHBox
        );
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
