package com.sultan.quiz;

public abstract class Question {
    private final String id;
    private final String title;
    private final int timeLimit; // seconds

    protected Question(String id, String title, int timeLimit) {
        this.id = id; this.title = title; this.timeLimit = timeLimit;
    }

    public String id() { return id; }
    public String title() { return title; }
    public int timeLimit() { return timeLimit; }

    public abstract boolean isCorrect(Object answer);
}
