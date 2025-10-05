package com.sultan.quiz;

import java.util.List;

public class MultipleChoiceQuestion extends Question {
    private final List<String> choices;
    private final String correct;

    public MultipleChoiceQuestion(String id, String title, int timeLimit,
                                  List<String> choices, String correct) {
        super(id, title, timeLimit);
        this.choices = choices;
        this.correct = correct;
    }

    public List<String> choices() { return choices; }

    @Override public boolean isCorrect(Object answer) {
        return correct.equals(answer);
    }
}
