package com.ter.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(5555);

        while(true){
            Socket player1 = server.accept();
            Socket player2 = server.accept();

            Game game = new Game(player1, player2);
            game.start();
        }
    }
}
