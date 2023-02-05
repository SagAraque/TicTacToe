package com.ter.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;

import java.io.IOException;

public class InitController {
    @FXML
    private ProgressIndicator progress;
    @FXML
    private Button initButton;

    @FXML
    private void initGame() throws IOException {
        progress.setVisible(true);
        initButton.setText("Buscando partida");
        initButton.setDisable(true);
        Client.initGame();
    }
}
