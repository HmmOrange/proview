package org.proview.modal.Game;

import java.util.*;

public class GameActivity {
    private static int numOfQuestion = 100;
    private static int currentQuestionID = 1;
    private static List<Integer> questionsChosen = new ArrayList<>();
    private static int numberOfQuestionsAnswered = 0;
    private static int score = 0;
    private static int lifeRemains = 3;


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
}
