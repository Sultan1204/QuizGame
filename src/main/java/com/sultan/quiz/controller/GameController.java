package com.sultan.quiz.controller;

import com.sultan.quiz.App;
import com.sultan.quiz.model.BooleanQuestion;
import com.sultan.quiz.model.MultipleChoiceQuestion;
import com.sultan.quiz.model.Question;
import com.sultan.quiz.service.GameManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicInteger;

public class GameController {

    @FXML
    private TextField txtPlayerName;

    @FXML
    private Button btnBegin;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblTimer;

    @FXML
    private ProgressBar progress;

    @FXML
    private Label lblScore;

    @FXML
    private VBox questionArea;

    @FXML
    private Button btnFinish;

    private final GameManager gm = GameManager.getInstance();
    private Timeline timeline;
    private int timeLimitSeconds;
    private final AtomicInteger remaining = new AtomicInteger(0);

    @FXML
    private void initialize() {
        // Bind live score (Observer via property binding)
        lblScore.textProperty().bind(Bindings.concat("Score: ", gm.scoreProperty().asString(), " / ", gm.totalQuestionsProperty().asString()));
        btnFinish.setDisable(true);

        if (gm.getQuiz() == null) {
            showAlertAndGoMenu("No quiz loaded. Please load a quiz in the menu.");
            return;
        }

        lblTitle.setText(gm.getQuiz().getTitle());

        // If a player name already exists (user navigated back), skip name entry
        if (gm.playerNameProperty().get() != null && !gm.playerNameProperty().get().isBlank()) {
            txtPlayerName.setText(gm.getPlayerName());
            txtPlayerName.setDisable(true);
            btnBegin.setDisable(true);
            startQuestionFlow();
        }

        btnBegin.setOnAction(e -> {
            String name = txtPlayerName.getText();
            if (name == null || name.isBlank()) {
                showWarn("Please enter your name to begin.");
                return;
            }
            gm.setPlayerName(name.trim());
            txtPlayerName.setDisable(true);
            btnBegin.setDisable(true);
            startQuestionFlow();
        });

        btnFinish.setOnAction(e -> {
            stopTimerIfAny();
            gm.finishAndPersistResults();
            App.getInstance().navigate("view/Results.fxml");
        });
    }

    private void startQuestionFlow() {
        renderCurrentQuestion(); // displays first (or current) question
    }

    private void renderCurrentQuestion() {
        questionArea.getChildren().clear();

        Question q = gm.getCurrentQuestion();
        if (q == null) {
            // No more questions -> show Finish
            btnFinish.setDisable(false);
            questionArea.getChildren().add(new Label("All questions answered! Click \"Finish Quiz\"."));
            progress.setProgress(0);
            lblTimer.setText("");
            return;
        }

        Label title = new Label((gm.getCurrentIndex() + 1) + ". " + q.getTitle());
        title.getStyleClass().add("question-title");
        VBox.setMargin(title, new Insets(0, 0, 10, 0));
        questionArea.getChildren().add(title);

        Node answerNode = createAnswerNode(q);
        questionArea.getChildren().add(answerNode);

        Button submit = new Button("Submit Answer");
        submit.getStyleClass().add("primary");
        VBox.setMargin(submit, new Insets(12, 0, 0, 0));
        questionArea.getChildren().add(submit);

        submit.setOnAction(e -> {
            boolean correct = evaluate(q, answerNode);
            gm.applyAnswerAndAdvance(correct);
            stopTimerIfAny();
            renderCurrentQuestion();
        });

        // Start per-question timer
        startTimer(q.getTimeLimit());
    }

    private Node createAnswerNode(Question q) {
        if (q instanceof MultipleChoiceQuestion mcq) {
            ToggleGroup group = new ToggleGroup();
            VBox box = new VBox(6);
            for (String choice : mcq.getChoices()) {
                RadioButton rb = new RadioButton(choice);
                rb.setToggleGroup(group);
                box.getChildren().add(rb);
            }
            return box;
        } else if (q instanceof BooleanQuestion bq) {
            ToggleGroup group = new ToggleGroup();
            RadioButton rbTrue = new RadioButton(bq.getLabelTrue());
            RadioButton rbFalse = new RadioButton(bq.getLabelFalse());
            rbTrue.setToggleGroup(group);
            rbFalse.setToggleGroup(group);

            VBox box = new VBox(6, rbTrue, rbFalse);
            return box;
        } else {
            return new Label("Unsupported question type.");
        }
    }

    private boolean evaluate(Question q, Node node) {
        if (q instanceof MultipleChoiceQuestion mcq) {
            VBox box = (VBox) node;
            for (Node n : box.getChildren()) {
                if (n instanceof RadioButton rb && rb.isSelected()) {
                    return mcq.isCorrect(rb.getText());
                }
            }
            // If required and nothing selected: treat as incorrect
            return false;
        } else if (q instanceof BooleanQuestion bq) {
            VBox box = (VBox) node;
            RadioButton rbTrue = (RadioButton) box.getChildren().get(0);
            RadioButton rbFalse = (RadioButton) box.getChildren().get(1);
            if (rbTrue.isSelected()) return bq.isCorrect(true);
            if (rbFalse.isSelected()) return bq.isCorrect(false);
            return false;
        }
        return false;
    }

    private void startTimer(int seconds) {
        stopTimerIfAny();

        timeLimitSeconds = Math.max(1, seconds);
        remaining.set(timeLimitSeconds);
        progress.setProgress(1.0);
        lblTimer.setText("Time: " + remaining.get() + "s");

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            int r = remaining.decrementAndGet();
            lblTimer.setText("Time: " + Math.max(0, r) + "s");
            progress.setProgress(Math.max(0.0, r / (double) timeLimitSeconds));
            if (r <= 0) {
                stopTimerIfAny();
                // Auto-advance as incorrect if time runs out
                gm.applyAnswerAndAdvance(false);
                renderCurrentQuestion();
            }
        }));
        timeline.setCycleCount(timeLimitSeconds);
        timeline.playFromStart();
    }

    private void stopTimerIfAny() {
        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }
    }

    private void showAlertAndGoMenu(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.setHeaderText("No Quiz");
        a.showAndWait();
        App.getInstance().navigate("view/Menu.fxml");
    }

    private void showWarn(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }
}
