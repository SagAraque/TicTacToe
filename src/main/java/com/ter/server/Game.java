package com.ter.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Game extends Thread{
    private String[] simbols = new String[]{"x", "o"};
    private Map<String, Socket> players = new HashMap<>();
    private Map<String, ObjectOutputStream> out = new HashMap<>();
    private Map<String, ObjectInputStream> in = new HashMap<>();
    private String[][] table = new String[][]{new String[]{"", "", ""}, new String[]{"", "", ""}, new String[]{"", "", ""}};
    private String winner = "";

    public Game(Socket player1, Socket player2) throws IOException {
        int rand = ThreadLocalRandom.current().nextInt(0,2); // Random number between 0 and 1

        players.put(simbols[rand], player1);
        out.put(simbols[rand],  new ObjectOutputStream(player1.getOutputStream()));
        in.put(simbols[rand], new ObjectInputStream(player1.getInputStream()));

        players.put(simbols[Math.abs(rand - 1)], player1);
        out.put(simbols[Math.abs(rand - 1)],  new ObjectOutputStream(player2.getOutputStream()));
        in.put(simbols[Math.abs(rand - 1)], new ObjectInputStream(player2.getInputStream()));
    }

    @Override
    public void run(){
        try {
            responseString(simbols[0], simbols[0]); // Send X
            responseString(simbols[1], simbols[1]); // Send o

            while (winner.equals("")){
                for (String s: simbols) {
                    table = (String[][]) in.get(s).readObject();

                    for (String s2:simbols) // Send new table to the other player
                        if(!s2.equals(s)) responseTable(s2);

                    checkPlay();

                    for (String s3: simbols) { // Send winner
                        responseString(s3, winner);
                        if(!winner.equals("")) responseTable(s3);
                    }
                }
            }

            endGame();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkPlay(){
        for (String s: simbols) {
            for (int x = 0; x < 3; x++) {
                if (table[x][0].equals(s) && table[x][1].equals(s) && table[x][2].equals(s)) {
                    winner = s;
                    break;
                }
                if (table[0][x].equals(s) && table[1][x].equals(s) && table[2][x].equals(s)) {
                    winner = s;
                    break;
                }

            }

            if(table[0][0].equals(s) && table[1][1].equals(s) && table[2][2].equals(s))
                winner = s;

            if(table[0][2].equals(s) && table[1][1].equals(s) && table[2][0].equals(s))
                winner = s;
        }

        int whiteSpaces = 0;
        for (String[] s: table)
            for (String s2: s)
                if(s2.equals("")) whiteSpaces++;

        if (whiteSpaces == 0) winner = "tie";
    }

    private void endGame() throws IOException {
        for (String s : simbols) {
            in.get(s).close();
            out.get(s).close();
            players.get(s).close();
        }
    }

    private void responseString(String player, String response) throws IOException {
        out.get(player).writeUTF(response);
        out.get(player).flush();
    }

    private void responseTable(String player) throws IOException {
        out.get(player).writeObject(table);
        out.get(player).flush();
    }


}
