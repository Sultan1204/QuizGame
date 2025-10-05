package com.sultan.quiz;

public class BooleanQuestion extends Question {
    private final boolean correct;

    public BooleanQuestion(String id, String title, int timeLimit, boolean correct) {
        super(id, title, timeLimit);
        this.correct = correct;
    }

    @Override public boolean isCorrect(Object answer) {
        return (answer instanceof Boolean b) && b == correct;
    }
}
