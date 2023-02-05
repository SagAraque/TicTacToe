package com.ter.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Client extends Application {
    public static Stage window;
    public static Scene gameScene;

    @Override
    public void start(Stage stage) throws IOException {
        window = stage;
        window.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        initView();
    }

    /**
     * First view
     * @throws IOException
     */
    public static void initView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("init-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 350, 400);
        window.setTitle("Tres en raya");
        window.setScene(scene);
        window.show();
    }

    /**
     * End game view
     * @param winner winner of the game
     * @param play table with the last play
     */
    public static void endGame(String winner, String[][] play){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("end-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 600);

            window.setTitle("Tres en raya");
            window.setScene(scene);
            window.show();

            EndController controller = fxmlLoader.getController();
            controller.initialize(winner, play);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Init game loading the game scene and starting the game thread
     * @throws IOException
     */
    public static void initGame() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("game-view.fxml"));
        gameScene = new Scene(fxmlLoader.load(), 500, 580);

        GameController gameController = new GameController(fxmlLoader.getController());
        gameController.start();
    }

    /**
     * Show the game view
     * @throws IOException
     */
    public static void showGame() throws IOException {
        window.setTitle("Tres en raya");
        window.setScene(gameScene);
        window.show();
    }

    public static void main(String[] args) {
        launch();
    }

}