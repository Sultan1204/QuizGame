package com.sultan.quiz.controller;

import com.sultan.quiz.App;
import com.sultan.quiz.model.Quiz;
import com.sultan.quiz.service.GameManager;
import com.sultan.quiz.service.QuizLoader;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;

public class MenuController {

    @FXML
    private Button btnLoad;

    @FXML
    private Button btnStart;

    @FXML
    private Label lblStatus;

    @FXML
    private TextArea txtQuizInfo;

    private final QuizLoader loader = new QuizLoader();

    @FXML
    private void initialize() {
        btnStart.setDisable(true);
        txtQuizInfo.setEditable(false);

        btnLoad.setOnAction(e -> onLoadQuiz());
        btnStart.setOnAction(e -> App.getInstance().navigate("view/Game.fxml"));

        // If returning from results with a loaded quiz, keep the button enabled
        GameManager gm = GameManager.getInstance();
        if (gm.getQuiz() != null) {
            btnStart.setDisable(false);
            txtQuizInfo.setText(gm.getQuiz().toPrettyString());
            lblStatus.setText("Quiz already loaded.");
        }
    }

    private void onLoadQuiz() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select Quiz JSON");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files", "*.json"));
        File file = fc.showOpenDialog(App.getInstance().getStage());
        if (file == null) return;

        try {
            Quiz quiz = loader.load(file);
            GameManager gm = GameManager.getInstance();
            gm.resetAll();                 // clear previous session
            gm.setQuiz(quiz);
            btnStart.setDisable(false);
            txtQuizInfo.setText(quiz.toPrettyString());
            lblStatus.setText("Loaded: " + file.getName());
        } catch (Exception ex) {
            showError("Failed to load quiz:\n" + ex.getMessage());
        }
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText("Load Error");
        a.showAndWait();
        lblStatus.setText("Error");
        btnStart.setDisable(true);
        txtQuizInfo.clear();
    }
}
