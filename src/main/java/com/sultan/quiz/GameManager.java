package com.sultan.quiz;

public class GameManager {
    private static final GameManager INSTANCE = new GameManager();

    private GameManager() {}

    public static GameManager getInstance() {
        return INSTANCE;
    }
}
