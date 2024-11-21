package org.proview.test.Scene;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.proview.modal.Game.GameActivity;
import org.proview.test.AppMain;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameView {
    public Label questionLabel;
    public Button ans1Button;
    public Button ans2Button;
    public Button ans3Button;
    public Button ans4Button;
    public Label pointLabel;
    public Label lifeRemainsLabel;
    public Button nextButton;
    public Label difficultyLabel;
    public Label highScoreLabel;

    private boolean[] ifLabelHasCorrectAns = new boolean[4];
    private List<Button> ansButtons = new ArrayList<>();
    private String difficulty = "easy";
    private int highScore = 0;

    public void initialize() throws SQLException {
        nextButton.setDisable(true);
        nextButton.setVisible(false);
        pointLabel.setText("Score: " + GameActivity.getScore());
        lifeRemainsLabel.setText(lifeRemainsLabel.getText() + GameActivity.getLifeRemains());
        highScore = GameActivity.getHighScore();
        highScoreLabel.setText(highScoreLabel.getText() + highScore);
        int qId = GameActivity.getCurrentQuestionID();

        String sql = """
                SELECT type, difficulty, question, correct_answer, incr_ans1, incr_ans2, incr_ans3
                FROM questions
                WHERE id = ?
                """;
        PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
        preparedStatement.setInt(1, qId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String type = resultSet.getString("type");
            if (type.equals("multiple")) {
                difficulty = resultSet.getString("difficulty");
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
                }
                questionLabel.setText(question);
                ans1Button.setText(answers[answerOrderNo.get(0)]);
                ans2Button.setText(answers[answerOrderNo.get(1)]);
            }
        }
        ansButtons = List.of(ans1Button, ans2Button, ans3Button, ans4Button);
        difficultyLabel.setText(difficultyLabel.getText() + difficulty);
    }

    private void onAnswerButtonClick(int ansId) {
        GameActivity.setNumberOfQuestionsAnswered(GameActivity.getNumberOfQuestionsAnswered() + 1);

        if (ifLabelHasCorrectAns[ansId]) {
            GameActivity.setScore(GameActivity.getScore() + GameActivity.getScoreAdded(difficulty));
            ansButtons.get(ansId).setStyle("-fx-background-color: green; -fx-text-fill: white;");
        } else {
            GameActivity.setLifeRemains(GameActivity.getLifeRemains() - 1);
            lifeRemainsLabel.setText("Life remains: " + GameActivity.getLifeRemains());
            ansButtons.get(ansId).setStyle("-fx-background-color: red; -fx-text-fill: white;");
        }
        pointLabel.setText("Score: " + GameActivity.getScore());
        if (GameActivity.getLifeRemains() == 0) {
            if (GameActivity.getScore() > highScore) {
                pointLabel.setText("End game. New High Score: " + GameActivity.getScore());
            } else {
                pointLabel.setText("End game. Final " + pointLabel.getText());
            }
        }
        GameActivity.setCurrentQuestionID(GameActivity.getQuestionsChosen().get(GameActivity.getNumberOfQuestionsAnswered()));
        nextButton.setVisible(true);
        nextButton.setDisable(false);
        disableAnswerButtons();
    }

    private void disableAnswerButtons() {
        for (Button b : ansButtons) {
            b.setDisable(true);
        }
    }

    public void onAns1ButtonClicked(ActionEvent actionEvent) {
        onAnswerButtonClick(0);
    }

    public void onAns2ButtonClicked(ActionEvent actionEvent) {
        onAnswerButtonClick(1);
    }

    public void onAns3ButtonClicked(ActionEvent actionEvent) {
        onAnswerButtonClick(2);
    }

    public void onAns4ButtonClicked(ActionEvent actionEvent) {
        onAnswerButtonClick(3);
    }

    public void onNextButtonClicked(ActionEvent actionEvent) throws IOException, SQLException {
        if(GameActivity.getNumberOfQuestionsAnswered() < GameActivity.getNumOfQuestion() && GameActivity.getLifeRemains() > 0) {
            FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("GameView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
            AppMain.window.setTitle("Hello!");
            AppMain.window.setScene(scene);
            AppMain.window.centerOnScreen();
        } else {
            GameActivity.endGame();
            FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("HomeView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
            AppMain.window.setTitle("Hello!");
            AppMain.window.setScene(scene);
            AppMain.window.centerOnScreen();
        }
    }

    public void onRestartButtonClicked(ActionEvent actionEvent) throws SQLException, IOException {
        GameActivity.endGame();
        GameActivity.restartGame();
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("GameView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }


}