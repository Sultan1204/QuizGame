package com.sultan.quiz;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MenuView extends VBox {
    public MenuView() {
        setSpacing(10);
        setPadding(new Insets(20));

        Label title = new Label("Welcome to the Quiz Game");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button startButton = new Button("Start (sample question)");
        startButton.setOnAction(e -> Main.ROOT.setCenter(new GameView()));

        getChildren().addAll(title, startButton);
    }
}
