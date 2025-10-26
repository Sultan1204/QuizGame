package com.sultan.quiz.model;

public class PlayerResult {
    private final String playerName;
    private final int totalQuestions;
    private final int correctQuestions;
    private final String dateIso;

    public PlayerResult(String playerName, int totalQuestions, int correctQuestions, String dateIso) {
        this.playerName = playerName;
        this.totalQuestions = totalQuestions;
        this.correctQuestions = correctQuestions;
        this.dateIso = dateIso;
    }

    public String getPlayerName() { return playerName; }
    public int getTotalQuestions() { return totalQuestions; }
    public int getCorrectQuestions() { return correctQuestions; }
    public String getDateIso() { return dateIso; }
}
