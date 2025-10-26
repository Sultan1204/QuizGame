package com.sultan.quiz.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sultan.quiz.model.Question;
import com.sultan.quiz.model.Quiz;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuizLoader {

    private final ObjectMapper mapper = new ObjectMapper();

    public Quiz load(File file) throws IOException {
        JsonNode root = mapper.readTree(file);

        String title = optText(root, "title", "Untitled Quiz");
        String description = optText(root, "description", "");
        String quizId = optText(root, "quizId", slug(title));

        List<Question> questions = new ArrayList<>();
        if (root.has("pages") && root.get("pages").isArray()) {
            for (JsonNode page : root.get("pages")) {
                int timeLimit = page.has("timeLimit") ? page.get("timeLimit").asInt(20) : 20;

                if (page.has("elements") && page.get("elements").isArray()) {
                    for (JsonNode el : page.get("elements")) {
                        Question q = QuestionFactory.fromJson(el, timeLimit);
                        if (q != null) questions.add(q);
                    }
                }
            }
        } else {
            throw new IOException("Invalid quiz JSON: missing 'pages' array.");
        }

        return new Quiz(quizId, title, description, questions);
    }

    private static String optText(JsonNode node, String key, String def) {
        return node.has(key) && node.get(key).isTextual() ? node.get(key).asText() : def;
    }

    private static String slug(String input) {
        String s = input.toLowerCase().replaceAll("[^a-z0-9]+", "-");
        s = s.replaceAll("(^-|-$)", "");
        return s.isBlank() ? "quiz" : s;
    }
}
