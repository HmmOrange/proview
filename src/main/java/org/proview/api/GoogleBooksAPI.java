package org.proview.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.proview.model.BookGoogle;
import org.proview.model.BookLib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;

public class GoogleBooksAPI {
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    public static String getBooksFromAPI(String query) throws IOException {
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

            return response.toString();
        }
        return null;
    }


}
