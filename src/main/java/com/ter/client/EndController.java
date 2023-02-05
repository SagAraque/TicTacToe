package com.ter.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class EndController
{
    @FXML
    private Label winnerText;
    @FXML
    private GridPane endTable;

    /**
     * Updates the view with the last play and the winner of the game
     * @param winner
     * @param winnerPlay
     */
    @FXML
    public void initialize(String winner, String[][] winnerPlay){
        winnerText.setText(winner);
        String circle = String.valueOf(getClass().getResource("circle.png"));
        String cross = String.valueOf(getClass().getResource("cross.png"));

        for (Node n: endTable.getChildren()) {
            StackPane p = (StackPane) n;
            int x = GridPane.getColumnIndex(p) == null ? 0 : GridPane.getColumnIndex(p);
            int y = GridPane.getRowIndex(p)== null ? 0 : GridPane.getRowIndex(p);

            if(!winnerPlay[x][y].equals("")){
                ImageView img = new ImageView(winnerPlay[x][y].equals("x") ? cross : circle);
                img.setFitHeight(30);
                img.setFitWidth(30);
                p.getChildren().add(img);
            }
        }
    }

    @FXML
    public void exit(){
        Platform.exit();
    }

    /**
     * Init a new game
     */
    @FXML
    public void newGame(){
        try{
            Client.initView();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
