package com.sultan.quiz.service;

import com.sultan.quiz.model.PlayerResult;
import com.sultan.quiz.model.Question;
import com.sultan.quiz.model.Quiz;
import javafx.beans.property.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class GameManager {

    private static final GameManager INSTANCE = new GameManager();

    public static GameManager getInstance() {
        return INSTANCE;
    }

    private GameManager() {}

    // Observable state (Observer Pattern through JavaFX properties)
    private final StringProperty playerName = new SimpleStringProperty();
    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty totalQuestions = new SimpleIntegerProperty(0);
    private final IntegerProperty currentIndex = new SimpleIntegerProperty(0);

    private Quiz quiz;

    public void resetAll() {
        quiz = null;
        playerName.set(null);
        score.set(0);
        totalQuestions.set(0);
        currentIndex.set(0);
    }

    public void resetForReplay() {
        score.set(0);
        currentIndex.set(0);
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
        totalQuestions.set(quiz.getQuestions().size());
        score.set(0);
        currentIndex.set(0);
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public String getPlayerName() {
        return playerName.get();
    }

    public void setPlayerName(String name) {
        this.playerName.set(name);
    }

    public StringProperty playerNameProperty() { return playerName; }
    public IntegerProperty scoreProperty() { return score; }
    public IntegerProperty totalQuestionsProperty() { return totalQuestions; }
    public IntegerProperty currentIndexProperty() { return currentIndex; }

    public Question getCurrentQuestion() {
        if (quiz == null) return null;
        List<Question> qs = quiz.getQuestions();
        int idx = currentIndex.get();
        if (idx >= 0 && idx < qs.size()) return qs.get(idx);
        return null;
    }

    public int getCurrentIndex() {
        return currentIndex.get();
    }

    public void applyAnswerAndAdvance(boolean correct) {
        if (correct) {
            score.set(score.get() + 1);
        }
        currentIndex.set(currentIndex.get() + 1);
    }

    public void finishAndPersistResults() {
        if (quiz == null) return;
        PlayerResult result = new PlayerResult(
                getPlayerName(),
                totalQuestions.get(),
                score.get(),
                Instant.now().toString()
        );
        ResultsWriter.persist(quiz, result);
    }

    public List<PlayerResult> loadPersistedResults() {
        if (quiz == null) return List.of();
        return new ArrayList<>(ResultsWriter.loadAll(quiz));
    }
}
