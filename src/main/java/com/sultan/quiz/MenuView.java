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
        Button startButton = new Button("Start Quiz");

        startButton.setOnAction(e -> {
            System.out.println("Start button clicked!");
            // Later this will go to GameView
        });

        getChildren().addAll(title, startButton);
    }
}
