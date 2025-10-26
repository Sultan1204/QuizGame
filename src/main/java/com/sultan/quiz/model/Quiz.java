package com.sultan.quiz.model;

import java.util.List;

public class Quiz {
    private final String quizId;
    private final String title;
    private final String description;
    private final List<Question> questions;

    public Quiz(String quizId, String title, String description, List<Question> questions) {
        this.quizId = quizId;
        this.title = title;
        this.description = description;
        this.questions = questions;
    }

    public String getQuizId() { return quizId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public List<Question> getQuestions() { return questions; }

    public String toPrettyString() {
        return """
                Title: %s
                Description: %s
                Questions: %d
                """.formatted(title, description == null ? "" : description, questions == null ? 0 : questions.size());
    }
}
