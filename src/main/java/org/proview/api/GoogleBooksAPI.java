package org.proview.api;

import com.google.gson.*;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringEscapeUtils;
import org.proview.modal.Book.BookGoogle;
import org.proview.modal.Book.BookLib;
import org.proview.modal.Book.BookManagement;
import org.proview.utils.SQLUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public class GoogleBooksAPI {
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    public static String getBooksFromAPI(String query) throws IOException {
        URL url = URI.create(BASE_URL + URLEncoder.encode(query, StandardCharsets.UTF_8)).toURL();
        System.out.println(url.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int respondCode = connection.getResponseCode();
        if (respondCode == HttpURLConnection.HTTP_OK) {
            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = input.readLine()) != null)
                response.append(inputLine);

            return response.toString();
        }
        return null;
    }

    public static void loadMissingImages() throws SQLException, IOException, InterruptedException {
        ObservableList<BookLib> bookList = SQLUtils.getBookList();
        for (int i = 0; i < bookList.size(); i++) {
            System.out.println("--------\n" + bookList.get(i).getTitle());
            ObservableList<BookGoogle> bookGoogleObservableList = BookManagement.getGoogleBookList(bookList.get(i).getTitle());

            assert bookGoogleObservableList != null;
            System.out.println(bookGoogleObservableList.getFirst().getCoverImageUrl());
            String url = "";
            for (var j : bookGoogleObservableList) {
                if (j.getCoverImageUrl() != null && !j.getCoverImageUrl().isEmpty()) {
                    url = j.getCoverImageUrl();
                    break;
                }
            }

            InputStream in = new URL(url).openStream();
            String path = "./assets/covers/cover" + (i + 1) + ".png";

            if (!Files.exists(Paths.get(path)))
                Files.copy(in, Paths.get(path));

            System.out.println("Added cover #" + (i + 1));
            Thread.sleep(3000);
        }

    }

    public static void main(String[] args) throws IOException {
        /*String response = getBooksFromAPI("Girl's Last Tour");
        JsonParser parser = new JsonParser();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonElement el = parser.parse(response);
        response = gson.toJson(el); // done

        System.out.println(response);*/

        String response = getBooksFromAPI("Girl's Last Tour");
        JsonParser parser = new JsonParser();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonElement el = parser.parse(response);
        response = gson.toJson(el); // done

        response = StringEscapeUtils.unescapeJava(response);
        System.out.println(response);

        JsonObject jsonObject = el.getAsJsonObject();
        JsonArray items = jsonObject.getAsJsonArray("items");

        if (items != null && items.size() > 0) {
            JsonObject volumeInfo = items.get(0).getAsJsonObject().getAsJsonObject("volumeInfo");
            String previewLink = volumeInfo.get("previewLink").getAsString();
            System.out.println("Preview Link: " + previewLink);
        } else {
            System.out.println("Không tìm thấy previewLink trong phản hồi JSON.");
        }
    }
}
