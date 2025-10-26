package com.sultan.quiz.model;

public abstract class Question {
    private final String name;
    private final String title;
    private final int timeLimit; // seconds
    private final boolean required;

    protected Question(String name, String title, int timeLimit, boolean required) {
        this.name = name;
        this.title = title;
        this.timeLimit = timeLimit;
        this.required = required;
    }

    public String getName() { return name; }
    public String getTitle() { return title; }
    public int getTimeLimit() { return timeLimit; }
    public boolean isRequired() { return required; }
}
