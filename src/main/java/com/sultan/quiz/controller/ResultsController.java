package com.sultan.quiz.controller;

import com.sultan.quiz.App;
import com.sultan.quiz.model.PlayerResult;
import com.sultan.quiz.service.GameManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ResultsController {

    @FXML
    private TableView<PlayerResult> table;

    @FXML
    private TableColumn<PlayerResult, String> colPlayer;

    @FXML
    private TableColumn<PlayerResult, Integer> colCorrect;

    @FXML
    private TableColumn<PlayerResult, Integer> colTotal;

    @FXML
    private TableColumn<PlayerResult, String> colDate;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnPlayAgain;

    private final GameManager gm = GameManager.getInstance();

    @FXML
    private void initialize() {
        colPlayer.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPlayerName()));
        colCorrect.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getCorrectQuestions()).asObject());
        colTotal.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getTotalQuestions()).asObject());
        colDate.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDateIso()));

        table.getItems().setAll(gm.loadPersistedResults());

        btnBack.setOnAction(e -> App.getInstance().navigate("view/Menu.fxml"));

        btnPlayAgain.setOnAction(e -> {
            gm.resetForReplay();
            App.getInstance().navigate("view/Game.fxml");
        });
    }
}
