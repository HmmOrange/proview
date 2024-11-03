package org.proview.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.proview.model.BookGoogle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;

public class GoogleBooksAPI {
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    public static BookGoogle getBooksFromAPI(String query) throws IOException {
        HttpClient client = HttpClient.newHttpClient();

        URL url = URI.create(BASE_URL + URLEncoder.encode(query, StandardCharsets.UTF_8)).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int respondCode = connection.getResponseCode();
        if (respondCode == HttpURLConnection.HTTP_OK) {
            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = input.readLine()) != null)
                response.append(inputLine);

            String jsonResponse = response.toString();
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

            JsonArray items = jsonObject.getAsJsonArray("items");
            if (items != null && !items.isEmpty()) {
                JsonObject volumeInfo = items.get(0).getAsJsonObject().getAsJsonObject("volumeInfo");

                // Book details
                String title = volumeInfo.get("title").getAsString();
                String authors = volumeInfo.getAsJsonArray("authors").toString();
                String description = volumeInfo.has("description") ? volumeInfo.get("description").getAsString() : "No description available";

                // Cover image URL
                String coverImageUrl = "";
                if (volumeInfo.has("imageLinks")) {
                    JsonObject imageLinks = volumeInfo.getAsJsonObject("imageLinks");
                    coverImageUrl = imageLinks.get("thumbnail").getAsString();
                }

                return new BookGoogle(title, authors, description, coverImageUrl);
            }
        }

        return null;
    }


}
