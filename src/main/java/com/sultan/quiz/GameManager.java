package com.sultan.quiz;

import javafx.beans.property.*;

public final class GameManager {
    private static final GameManager INSTANCE = new GameManager();

    private final StringProperty playerName = new SimpleStringProperty("");
    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty currentIndex = new SimpleIntegerProperty(0);

    private GameManager() {}

    public static GameManager getInstance() { return INSTANCE; }

    public StringProperty playerNameProperty() { return playerName; }
    public IntegerProperty scoreProperty() { return score; }
    public IntegerProperty currentIndexProperty() { return currentIndex; }

    public void reset() {
        playerName.set("");
        score.set(0);
        currentIndex.set(0);
    }

    public void addPoint() { score.set(score.get() + 1); }
    public void nextQuestion() { currentIndex.set(currentIndex.get() + 1); }
}
