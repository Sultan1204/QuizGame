package com.sultan.quiz.controller;

import com.sultan.quiz.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class StartController {

    @FXML
    private Button btnContinue;

    @FXML
    private void initialize() {
        // Simple smoke test button
        btnContinue.setOnAction(e -> App.getInstance().navigate("view/Menu.fxml"));
    }
}
