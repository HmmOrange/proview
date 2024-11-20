package org.proview.modal.Game;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GameActivity {
    private static int numOfQuestion = 10;
    private static int currentQuestion = 1;
    private static boolean lastResult = true; ///store last question result: player's answer is true or false
    private static Set<Integer> questionsChosen = new HashSet<>();

    public static void setCurrentQuestion(int q) {
        currentQuestion = q;
    }

    public static int getCurrentQuestion() {
        return currentQuestion;
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

    public static Set<Integer> getQuestionsChosen() {
        return questionsChosen;
    }
}
