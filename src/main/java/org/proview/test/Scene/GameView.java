package org.proview.test.Scene;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.proview.modal.Game.GameActivity;
import org.proview.test.AppMain;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameView {
    public Label questionLabel;
    public Button ans1Button;
    public Button ans2Button;
    public Button ans3Button;
    public Button ans4Button;
    public Label resultLabel;

    private boolean[] ifLabelHasCorrectAns = new boolean[4];

    public void initialize() throws SQLException {
        int qId = GameActivity.getCurrentQuestion();
        if (qId > 1) {
            if (GameActivity.getLastResult()) {
                resultLabel.setText("That's right");
            } else {
                resultLabel.setText("That's wrong");
            }
        }
        String sql = """
                SELECT type, difficulty, question, correct_answer, incr_ans1, incr_ans2, incr_ans3
                FROM questions
                WHERE id = ?
                """;
        PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
        preparedStatement.setInt(1, qId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            System.out.println("run");
            String type = resultSet.getString("type");
            if (type.equals("multiple")) {
                String question = resultSet.getString("question");
                String correct_answer = resultSet.getString("correct_answer");
                String incr_ans1 = resultSet.getString("incr_ans1");
                String incr_ans2 = resultSet.getString("incr_ans2");
                String incr_ans3 = resultSet.getString("incr_ans3");
                String[] answers = {correct_answer, incr_ans1, incr_ans2, incr_ans3};
                System.out.println(answers[0]);
                Integer[] arr = {0, 1, 2, 3};
                List<Integer> answerOrderNo = Arrays.asList(arr);
                Collections.shuffle(answerOrderNo);
                for (int i = 0; i < 4; i++) {
                    if (answerOrderNo.get(i) == 0) {
                        ifLabelHasCorrectAns[i] = true;
                    } else {
                        ifLabelHasCorrectAns[i] = false;
                    }
                    System.out.println(i + " is " + ifLabelHasCorrectAns[i]);
                }
                questionLabel.setText(question);
                ans1Button.setText(answers[answerOrderNo.get(0)]);
                ans2Button.setText(answers[answerOrderNo.get(1)]);
                ans3Button.setText(answers[answerOrderNo.get(2)]);
                ans4Button.setText(answers[answerOrderNo.get(3)]);
            } else {
                ans3Button.setDisable(true);
                ans3Button.setVisible(false);
                ans4Button.setVisible(false);
                ans4Button.setDisable(true);
                String question = resultSet.getString("question");
                String correct_answer = resultSet.getString("correct_answer");
                String incr_ans1 = resultSet.getString("incr_ans1");
                String[] answers = {correct_answer, incr_ans1};
                System.out.println(answers[0]);
                Integer[] arr = {0, 1};
                List<Integer> answerOrderNo = Arrays.asList(arr);
                Collections.shuffle(answerOrderNo);
                for (int i = 0; i < 2; i++) {
                    if (answerOrderNo.get(i) == 0) {
                        ifLabelHasCorrectAns[i] = true;
                    } else {
                        ifLabelHasCorrectAns[i] = false;
                    }
                    System.out.println(i + " is " + ifLabelHasCorrectAns[i]);
                }
                questionLabel.setText(question);
                ans1Button.setText(answers[answerOrderNo.get(0)]);
                ans2Button.setText(answers[answerOrderNo.get(1)]);
            }
        }
    }

    public void onAns1ButtonClicked(ActionEvent actionEvent) throws IOException, InterruptedException {
        if(GameActivity.getCurrentQuestion() < GameActivity.getNumOfQuestion()) {
            if (ifLabelHasCorrectAns[0]) {
                GameActivity.setLastResult(true);
            } else {
                GameActivity.setLastResult(false);
            }
            GameActivity.setCurrentQuestion(GameActivity.getCurrentQuestion() + 1);
            FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("GameView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
            AppMain.window.setTitle("Hello!");
            AppMain.window.setScene(scene);
            AppMain.window.centerOnScreen();
        }
    }

    public void onAns2ButtonClicked(ActionEvent actionEvent) throws IOException {
        if(GameActivity.getCurrentQuestion() < GameActivity.getNumOfQuestion()) {
            if (ifLabelHasCorrectAns[1]) {
                GameActivity.setLastResult(true);
            } else {
                GameActivity.setLastResult(false);
            }
            GameActivity.setCurrentQuestion(GameActivity.getCurrentQuestion() + 1);
            FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("GameView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
            AppMain.window.setTitle("Hello!");
            AppMain.window.setScene(scene);
            AppMain.window.centerOnScreen();
        }
    }

    public void onAns3ButtonClicked(ActionEvent actionEvent) throws IOException {
        if(GameActivity.getCurrentQuestion() < GameActivity.getNumOfQuestion()) {
            if (ifLabelHasCorrectAns[2]) {
                GameActivity.setLastResult(true);
            } else {
                GameActivity.setLastResult(false);
            }
            GameActivity.setCurrentQuestion(GameActivity.getCurrentQuestion() + 1);
            FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("GameView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
            AppMain.window.setTitle("Hello!");
            AppMain.window.setScene(scene);
            AppMain.window.centerOnScreen();
        }
    }

    public void onAns4ButtonClicked(ActionEvent actionEvent) throws IOException {
        if(GameActivity.getCurrentQuestion() < GameActivity.getNumOfQuestion()) {
            if (ifLabelHasCorrectAns[3]) {
                GameActivity.setLastResult(true);
            } else {
                GameActivity.setLastResult(false);
            }
            GameActivity.setCurrentQuestion(GameActivity.getCurrentQuestion() + 1);
            FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("GameView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
            AppMain.window.setTitle("Hello!");
            AppMain.window.setScene(scene);
            AppMain.window.centerOnScreen();
        }
    }
}
