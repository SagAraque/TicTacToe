package com.ter.client;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ClientController {
    private static String player;
    private String[][] plays = new String[][]{new String[]{"", "", ""}, new String[]{"", "", ""}, new String[]{"", "", ""}};

    @FXML
    private GridPane table;
    @FXML
    private Label topText;


    /**
     * Updates the GridPane and send the table array to the server
     * @param event
     * @throws IOException
     */
    @FXML
    public void play(MouseEvent event) throws IOException{
        StackPane p = (StackPane) event.getSource();
        int x = GridPane.getColumnIndex(p) == null ? 0 : GridPane.getColumnIndex(p);
        int y = GridPane.getRowIndex(p)== null ? 0 : GridPane.getRowIndex(p);

        if(p.getChildren().size() != 1){
            ImageView img = new ImageView(String.valueOf(getClass().getResource(player.equals("x") ? "cross.png" : "circle.png")));
            p.getChildren().add(img);
            plays[x][y] = player;
        }

        GameController.sendPlay(plays);
    }

    /**
     * Update the GridPane with the new play
     * @param play
     */
    @FXML
    public void updateTable(String[][] play){
        plays = play;

        for(Node n : table.getChildren()){
            StackPane p = (StackPane) n;
            int x = GridPane.getColumnIndex(p) == null ? 0 : GridPane.getColumnIndex(p);
            int y = GridPane.getRowIndex(p)== null ? 0 : GridPane.getRowIndex(p);

            if (plays[x][y].equals("x") && p.getChildren().size() == 0) {
                ImageView img = new ImageView(String.valueOf(getClass().getResource("cross.png")));
                p.getChildren().add(img);
            }else if(plays[x][y].equals("o") && p.getChildren().size() == 0){
                ImageView img = new ImageView(String.valueOf(getClass().getResource("circle.png")));
                p.getChildren().add(img);
            }
        }
    }

    public void initGame(String newPlayer) throws IOException, ClassNotFoundException {
        player = newPlayer;
    }

    public void changeTurn(boolean turn){
        topText.setText(turn ? "Tu turno" : "Turno del contrincante");
        table.setDisable(!turn);
    }

}