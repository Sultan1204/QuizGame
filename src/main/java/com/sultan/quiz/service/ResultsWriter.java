package com.sultan.quiz.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import com.sultan.quiz.model.PlayerResult;
import com.sultan.quiz.model.Quiz;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ResultsWriter {

    private static final ObjectMapper M = new ObjectMapper();

    private static File ensureDir() throws IOException {
        File dir = new File(System.getProperty("user.home"), "QuizResults");
        if (!dir.exists()) Files.createDirectories(dir.toPath());
        return dir;
    }

    private static File targetFile(Quiz quiz) throws IOException {
        File dir = ensureDir();
        return new File(dir, quiz.getQuizId() + "-results.json");
    }

    public static synchronized void persist(Quiz quiz, PlayerResult result) {
        try {
            File file = targetFile(quiz);
            ObjectNode root;

            if (file.exists()) {
                root = (ObjectNode) M.readTree(file);
            } else {
                root = M.createObjectNode();
                root.put("quizId", quiz.getQuizId());
                root.put("name", quiz.getTitle());
                root.set("results", M.createArrayNode());
            }

            ArrayNode arr;
            if (root.has("results") && root.get("results").isArray()) {
                arr = (ArrayNode) root.get("results");
            } else {
                arr = M.createArrayNode();
                root.set("results", arr);
            }

            ObjectNode r = M.createObjectNode();
            r.put("playerName", result.getPlayerName());
            r.put("totalQuestions", result.getTotalQuestions());
            r.put("correctQuestions", result.getCorrectQuestions());
            r.put("date", result.getDateIso());

            arr.add(r);

            M.writerWithDefaultPrettyPrinter().writeValue(file, root);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static synchronized List<PlayerResult> loadAll(Quiz quiz) {
        List<PlayerResult> list = new ArrayList<>();
        try {
            File file = targetFile(quiz);
            if (!file.exists()) return list;

            JsonNode root = M.readTree(file);
            if (root.has("results") && root.get("results").isArray()) {
                for (JsonNode n : root.get("results")) {
                    String player = n.has("playerName") ? n.get("playerName").asText() : "Unknown";
                    int total = n.has("totalQuestions") ? n.get("totalQuestions").asInt() : 0;
                    int correct = n.has("correctQuestions") ? n.get("correctQuestions").asInt() : 0;
                    String date = n.has("date") ? n.get("date").asText() : "";
                    list.add(new PlayerResult(player, total, correct, date));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
