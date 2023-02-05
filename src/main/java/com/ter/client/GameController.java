package com.ter.client;

import javafx.application.Platform;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GameController extends Thread {
    private Socket server;
    private ObjectInputStream in;
    private static ObjectOutputStream out;
    private static boolean turn;
    private static ClientController controller;
    private String winner, player;

    private String[][] plays = new String[][]{new String[]{"", "", ""}, new String[]{"", "", ""}, new String[]{"", "", ""}};

    public GameController(ClientController controller) {
        this.winner = "";
        GameController.controller = controller; //Game view controller
    }

    @Override
    public void run(){

        try {
            server = new Socket("localhost", 5555);
            in = new ObjectInputStream(server.getInputStream());
            out = new ObjectOutputStream(server.getOutputStream());

            player = in.readUTF().trim(); // Get the player (X or O)
            Platform.runLater(() -> {
                try {
                    Client.showGame(); // Show the game view
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            controller.initGame(player);

            turn = player.equals("x"); // Get the turn. X is always the first

            Platform.runLater(()-> controller.changeTurn(turn));

            while(winner.equals("")){ // Game loop
                if(!turn){ // Only enter if is not the player turn
                    plays = (String[][]) in.readObject();
                    Platform.runLater(()-> controller.updateTable(plays)); // Update view table

                    turn = !turn;
                    Platform.runLater(()-> controller.changeTurn(turn));
                }

                winner = in.readUTF();

                if(!winner.equals("")) endGame();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send the current table to the server
     * @param table Array with the current state of the view table
     * @throws IOException
     */
    public static void sendPlay(String[][] table) throws IOException {
        out.writeObject(table);
        out.flush();
        turn = !turn;
        Platform.runLater(()-> controller.changeTurn(turn));
    }

    /**
     * Gets the last play from the server and close its connection
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void endGame() throws IOException, ClassNotFoundException {
        plays = (String[][]) in.readObject();
        in.close();
        out.close();
        server.close();

        winner = winner.equals(player) ? "GANADOR" : winner.equals("tie") ? "EMPATE" : "PERDEDOR";

        Platform.runLater(() -> Client.endGame(winner, plays));
    }
}
