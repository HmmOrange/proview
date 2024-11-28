package org.proview.model.Game;

import org.proview.model.User.UserManagement;
import org.proview.test.AppMain;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class GameActivity {
    private static int numOfQuestion = 100;
    private static int currentQuestionID = 1;
    private static List<Integer> questionsChosen = new ArrayList<>();
    private static int numberOfQuestionsAnswered = 0;
    private static int score = 0;
    private static int lifeRemains = 3;
    private static Timestamp start_time = new Timestamp(System.currentTimeMillis());
    private static Timestamp end_time = new Timestamp(System.currentTimeMillis());
    private static boolean ifFiftyFiftyUsed = false;
    private static boolean ifShieldUsed = false;
    private static boolean ifDoubleUsed = false;


    public static int getNumberOfQuestionsAnswered() {
        return numberOfQuestionsAnswered;
    }

    public static void setNumberOfQuestionsAnswered(int numberOfQuestionsAnswered) {
        GameActivity.numberOfQuestionsAnswered = numberOfQuestionsAnswered;
    }

    public static void setCurrentQuestionID(int q) {
        currentQuestionID = q;
    }

    public static int getCurrentQuestionID() {
        return currentQuestionID;
    }


    public static int getNumOfQuestion() {
        return numOfQuestion;
    }

    public static void setNumOfQuestion(int numOfQuestion) {
        GameActivity.numOfQuestion = numOfQuestion;
    }

    public static void setNewQuestionsList() {
        questionsChosen = new ArrayList<>();
        for (int i = 1; i <= numOfQuestion; i++) {
            questionsChosen.add(i);
        }
        Collections.shuffle(questionsChosen);
    }

    public static List<Integer> getQuestionsChosen() {
        return questionsChosen;
    }

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        GameActivity.score = score;
    }

    public static int getLifeRemains() {
        return lifeRemains;
    }

    public static void setLifeRemains(int lifeRemains) {
        GameActivity.lifeRemains = lifeRemains;
    }

    public static void restartGame() throws SQLException {
        String sql = """
                SELECT COUNT(*) AS count FROM questions;
                """;
        PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            GameActivity.setNumOfQuestion(resultSet.getInt("count"));
        }
        GameActivity.setNewQuestionsList();
        GameActivity.setCurrentQuestionID(GameActivity.getQuestionsChosen().getFirst());
        GameActivity.setNumberOfQuestionsAnswered(0);
        GameActivity.setScore(0);
        GameActivity.setLifeRemains(3);
        GameActivity.setStart_time(new Timestamp(System.currentTimeMillis()));
        GameActivity.setIfFiftyFiftyUsed(false);
        GameActivity.setIfShieldUsed(false);
        GameActivity.setIfDoubleUsed(false);
    }

    public static void endGame() throws SQLException {
        GameActivity.setEnd_time(new Timestamp(System.currentTimeMillis()));
        System.out.println("End at:" + GameActivity.getEnd_time());
        String sql = """
                INSERT INTO game_history (user_id, score, question_answered, start_time, end_time)
                VALUE (?, ?, ?, ?, ?)
                """;
        PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
        preparedStatement.setInt(1, UserManagement.getCurrentUser().getId());
        preparedStatement.setInt(2, getScore());
        preparedStatement.setInt(3, getNumberOfQuestionsAnswered());
        preparedStatement.setTimestamp(4, getStart_time());
        preparedStatement.setTimestamp(5, getEnd_time());
        preparedStatement.execute();
    }

    public static int getScoreAdded(String difficulty) {
        if (difficulty.equals("easy")) {
            return 1;
        } else if (difficulty.equals("medium")) {
            return 2;
        }
        return 3;
    }

    public static Timestamp getStart_time() {
        return start_time;
    }

    public static void setStart_time(Timestamp start_time) {
        GameActivity.start_time = start_time;
    }

    public static Timestamp getEnd_time() {
        return end_time;
    }

    public static void setEnd_time(Timestamp end_time) {
        GameActivity.end_time = end_time;
    }

    public static int getHighScore() throws SQLException {
        int high_score = 0;
        String sql = """
                SELECT COALESCE(MAX(score), 0) AS high_score FROM game_history;
                """;
        PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            high_score = resultSet.getInt("high_score");
        }
        return high_score;
    }

    public static boolean isFiftyFiftyUsed() {
        return ifFiftyFiftyUsed;
    }

    public static void setIfFiftyFiftyUsed(boolean i) {
        ifFiftyFiftyUsed = i;
    }

    public static boolean isShieldUsed() {
        return ifShieldUsed;
    }

    public static void setIfShieldUsed(boolean i) {
        ifShieldUsed = i;
    }

    public static boolean isDoubleUsed() {
        return ifDoubleUsed;
    }

    public static void setIfDoubleUsed(boolean i) {
        ifDoubleUsed = i;
    }
}
