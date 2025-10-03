package com.sultan.quiz;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class  Main extends Application {

    public static BorderPane ROOT = new BorderPane();

    @Override
    public void start(Stage stage) {
        ROOT.setPrefSize(800, 600);

        // Start with MenuView as the first screen
        ROOT.setCenter(new MenuView());

        Scene scene = new Scene(ROOT);
        stage.setTitle("Quiz Game");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
