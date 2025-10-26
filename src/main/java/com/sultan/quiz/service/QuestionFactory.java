package com.sultan.quiz.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sultan.quiz.model.BooleanQuestion;
import com.sultan.quiz.model.MultipleChoiceQuestion;
import com.sultan.quiz.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionFactory {

    public static Question fromJson(JsonNode element, int timeLimit) {
        String type = text(element, "type", null);
        String name = text(element, "name", "");
        String title = text(element, "title", "");
        boolean required = element.has("isRequired") && element.get("isRequired").asBoolean();

        if (type == null) return null;

        switch (type) {
            case "radiogroup" -> {
                // Multiple choice
                List<String> choices = new ArrayList<>();
                if (element.has("choices") && element.get("choices").isArray()) {
                    element.get("choices").forEach(n -> choices.add(n.asText()));
                }
                String correct = text(element, "correctAnswer", null);
                String order = text(element, "choicesOrder", "none");
                if ("random".equalsIgnoreCase(order)) {
                    Collections.shuffle(choices);
                }
                return new MultipleChoiceQuestion(name, title, timeLimit, required, choices, correct);
            }
            case "boolean" -> {
                boolean correct = element.has("correctAnswer") && element.get("correctAnswer").asBoolean();
                String labelTrue = text(element, "labelTrue", "True");
                String labelFalse = text(element, "labelFalse", "False");
                return new BooleanQuestion(name, title, timeLimit, required, labelTrue, labelFalse, correct);
            }
            default -> {
                // Unsupported types could be extended here
                return null;
            }
        }
    }

    private static String text(JsonNode n, String k, String def) {
        return n.has(k) && n.get(k).isTextual() ? n.get(k).asText() : def;
    }
}
