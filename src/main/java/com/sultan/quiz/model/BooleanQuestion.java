package com.sultan.quiz.model;

public class BooleanQuestion extends Question {
    private final String labelTrue;
    private final String labelFalse;
    private final boolean correctAnswer;

    public BooleanQuestion(String name, String title, int timeLimit, boolean required,
                           String labelTrue, String labelFalse, boolean correctAnswer) {
        super(name, title, timeLimit, required);
        this.labelTrue = labelTrue;
        this.labelFalse = labelFalse;
        this.correctAnswer = correctAnswer;
    }

    public String getLabelTrue() { return labelTrue; }
    public String getLabelFalse() { return labelFalse; }

    public boolean isCorrect(boolean selected) {
        return selected == correctAnswer;
    }
}
