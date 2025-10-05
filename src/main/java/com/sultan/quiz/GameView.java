package com.sultan.quiz;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.List;

public class GameView extends VBox {
    private final GameManager gm = GameManager.getInstance();

    private final Label scoreLabel = new Label();
    private final Label timerLabel = new Label();

    private Timeline timeline;
    private int timeLeft;

    // temporary: one hardcoded question so you can test the timer/flow
    private final MultipleChoiceQuestion sample =
            new MultipleChoiceQuestion(
                    "q1",
                    "Which company originally developed Java?",
                    10,
                    List.of("Microsoft", "Sun Microsystems", "Apple", "Google"),
                    "Sun Microsystems");

    public GameView() {
        setPadding(new Insets(20));
        setSpacing(12);

        Label header = new Label("Game");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");

        Button continueBtn = new Button("Continue");
        continueBtn.setOnAction(e -> {
            if (nameField.getText().isBlank()) {
                new Alert(Alert.AlertType.WARNING, "Please enter your name").showAndWait();
                return;
            }
            gm.playerNameProperty().set(nameField.getText().trim());
            gm.scoreProperty().set(0);
            showQuestion(sample);
        });

        // live score (Observer pattern via binding)
        scoreLabel.textProperty().bind(gm.scoreProperty().asString("Score: %d"));

        getChildren().addAll(header, nameField, continueBtn, scoreLabel, timerLabel);
    }

    private void showQuestion(Question q) {
        // remove any previous question UIs (but keep header/score/timer)
        getChildren().removeIf(n -> n.getUserData() != null && n.getUserData().equals("questionBlock"));

        VBox block = new VBox(8);
        block.setUserData("questionBlock");

        Label title = new Label(q.title());
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        final Object[] selected = new Object[1];

        if (q instanceof MultipleChoiceQuestion mcq) {
            ToggleGroup tg = new ToggleGroup();
            VBox choicesBox = new VBox(4);
            for (String c : mcq.choices()) {
                RadioButton rb = new RadioButton(c);
                rb.setToggleGroup(tg);
                rb.setOnAction(e -> selected[0] = c);
                choicesBox.getChildren().add(rb);
            }
            block.getChildren().addAll(title, choicesBox);
        } else if (q instanceof BooleanQuestion) {
            ToggleGroup tg = new ToggleGroup();
            RadioButton t = new RadioButton("True");
            RadioButton f = new RadioButton("False");
            t.setToggleGroup(tg); f.setToggleGroup(tg);
            t.setOnAction(e -> selected[0] = true);
            f.setOnAction(e -> selected[0] = false);
            block.getChildren().addAll(title, new VBox(4, t, f));
        }

        Button submit = new Button("Submit");
        submit.setOnAction(e -> {
            boolean correct = selected[0] != null && q.isCorrect(selected[0]);
            if (correct) gm.addPoint();
            finish(); // only one question for now
        });

        block.getChildren().add(submit);
        getChildren().add(block);

        startTimer(q.timeLimit(), this::finish);
    }

    private void startTimer(int seconds, Runnable onFinish) {
        if (timeline != null) timeline.stop();
        timeLeft = seconds;
        timerLabel.setText("Time: " + timeLeft + "s");

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft--;
            timerLabel.setText("Time: " + Math.max(timeLeft, 0) + "s");
            if (timeLeft <= 0) {
                timeline.stop();
                onFinish.run();
            }
        }));
        timeline.setCycleCount(seconds);
        timeline.playFromStart();
    }

    private void finish() {
        if (timeline != null) timeline.stop();
        Alert a = new Alert(Alert.AlertType.INFORMATION,
                gm.playerNameProperty().get() + ", your score is " + gm.scoreProperty().get() + " / 1");
        a.setHeaderText("Finished!");
        a.showAndWait();
        Main.ROOT.setCenter(new MenuView()); // back to menu
    }
}
