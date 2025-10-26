package com.sultan.quiz.model;

import java.util.List;
import java.util.Objects;

public class MultipleChoiceQuestion extends Question {
    private final List<String> choices;
    private final String correctAnswer;

    public MultipleChoiceQuestion(String name, String title, int timeLimit, boolean required,
                                  List<String> choices, String correctAnswer) {
        super(name, title, timeLimit, required);
        this.choices = choices;
        this.correctAnswer = correctAnswer;
    }

    public List<String> getChoices() { return choices; }

    public boolean isCorrect(String selected) {
        return Objects.equals(correctAnswer, selected);
    }
}
