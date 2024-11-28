package org.proview.api;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringEscapeUtils;
import org.proview.test.AppMain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GamesAPI {
    private static final String API_URL = "https://opentdb.com/api.php?amount=50&category=10";

    private static String getQandAFromAPI() throws IOException {
        URL url = URI.create(API_URL).toURL();
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

    public static void insertQandAToDb() throws IOException, SQLException, InterruptedException {
        Connection connection = AppMain.connection;
        for (int times = 0; times < 10; times++) {
            System.out.println("Requesting data, attempt: " + (times + 1));
            String response = getQandAFromAPI();
            if (response == null) {
                System.out.println("API response is null at attempt: " + (times + 1));
                continue;
            }
            Thread.sleep(5000);
            JsonParser parser = new JsonParser();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            JsonElement el = parser.parse(response);
            response = gson.toJson(el); // done
            response = StringEscapeUtils.unescapeJava(response);
            response = StringEscapeUtils.unescapeHtml4(response);

            JsonObject jsonObject = el.getAsJsonObject();
            JsonArray items = jsonObject.getAsJsonArray("results");


            String addQandASql = """
                    INSERT IGNORE INTO questions(type, difficulty, question, correct_answer, incr_ans1, incr_ans2, incr_ans3)
                    VALUE (?, ?, ?, ?, ?, ?, ?)
                    """;
            if (items != null && items.size() > 0) {
                for (int i = 0; i < items.size(); i++) {

                    String[] questionDetails = new String[7];
                    JsonObject questionNo = items.get(i).getAsJsonObject();
                    String question = questionNo.get("question").getAsString();
                    question = StringEscapeUtils.unescapeJava(question);
                    question = StringEscapeUtils.unescapeHtml4(question);
                    String checkDupSql = """
                            SELECT * FROM questions WHERE question = ?
                            """;
                    PreparedStatement checkDupPS = AppMain.connection.prepareStatement(checkDupSql);
                    checkDupPS.setString(1, question);
                    ResultSet checkDupResultSet = checkDupPS.executeQuery();
                    if (checkDupResultSet.next()) {
                        continue;
                    }
                    String type = questionNo.get("type").getAsString();
                    if (type.equals("boolean")) {
                        continue;
                    }
                    String difficulty = questionNo.get("difficulty").getAsString();
                    String correctAnswer = questionNo.get("correct_answer").getAsString();
                    JsonArray incorrectAnswersArray = questionNo.getAsJsonArray("incorrect_answers");
                    List<String> incorrectAnswers = gson.fromJson(incorrectAnswersArray,
                            new TypeToken<List<String>>() {
                            }.getType());

                    questionDetails[0] = type;
                    questionDetails[1] = difficulty;
                    questionDetails[2] = question;
                    questionDetails[3] = correctAnswer;
                    for (int j = 0; j < incorrectAnswers.size(); j++) {
                        questionDetails[4 + j] = incorrectAnswers.get(j);
                    }
                    PreparedStatement addQandAPS = connection.prepareStatement(addQandASql);
                    for (int j = 0; j < questionDetails.length; j++) {
                        questionDetails[j] = StringEscapeUtils.unescapeJava(questionDetails[j]);
                        questionDetails[j] = StringEscapeUtils.unescapeHtml4(questionDetails[j]);
                        addQandAPS.setString(j + 1, questionDetails[j]);
                    }
                    addQandAPS.execute();

                }
            } else {
                System.out.println("Không tìm thấy q&a trong phản hồi JSON.");
            }
        }
    }


    public static void printInsertSqls() throws SQLException {
        String unformattedSql = "INSERT INTO questions(type, difficulty, question, correct_answer, incr_ans1, incr_ans2, incr_ans3) VALUE ('%s', '%s', '%s', '%s', '%s', '%s', '%s');\n";

        String getData = "SELECT type, difficulty, question, correct_answer, incr_ans1, incr_ans2, incr_ans3 FROM questions WHERE type = 'multiple'";
        ResultSet resultSet = AppMain.connection.prepareStatement(getData).executeQuery();
        while (resultSet.next()) {
            String type = "multiple";
            String difficulty = resultSet.getString("difficulty");
            String question = resultSet.getString("question");
            String correct_answer = resultSet.getString("correct_answer");
            String incr_ans1 = resultSet.getString("incr_ans1");
            String incr_ans2 = resultSet.getString("incr_ans2");
            String incr_ans3 = resultSet.getString("incr_ans3");
            String formattedSql = String.format(unformattedSql, type, difficulty, question, correct_answer, incr_ans1, incr_ans2, incr_ans3);
            System.out.println(formattedSql);
        }
    }
}
