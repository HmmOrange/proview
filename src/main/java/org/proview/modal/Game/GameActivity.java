package org.proview.modal.Game;

import java.util.*;

public class GameActivity {
    private static int numOfQuestion = 10;
    private static int currentQuestionID = 1;
    private static boolean lastResult = true; ///store last question result: player's answer is true or false
    private static Set<Integer> questionsChosen = new HashSet<>();
    private static int numberOfQuestionsAnswered = 0;
    private static int score = 0;


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

    public static boolean getLastResult() {
        return lastResult;
    }

    public static void setLastResult(boolean lastR) {
        lastResult = lastR;
    }

    public static int getNumOfQuestion() {
        return numOfQuestion;
    }

    public static void setNumOfQuestion(int numOfQuestion) {
        GameActivity.numOfQuestion = numOfQuestion;
    }

    public static void setNewQuestionsList() {
        questionsChosen = new HashSet<>();
        Random random = new Random();
        while (questionsChosen.size() < numOfQuestion) {
            int num = random.nextInt(200) + 1; // Random number between 1 and 200
            questionsChosen.add(num);
        }
    }

    public static List<Integer> getQuestionsChosen() {
        List<Integer> respond = new ArrayList<>(questionsChosen);
        return respond;
    }

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        GameActivity.score = score;
    }
}
