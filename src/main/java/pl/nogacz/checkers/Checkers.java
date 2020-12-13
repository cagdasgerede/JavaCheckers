package pl.nogacz.checkers;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pl.nogacz.checkers.application.Design;
import pl.nogacz.checkers.audio.AudioPlayer;
import pl.nogacz.checkers.board.Board;

/**
 * @author Dawid Nogacz on 17.05.2019
 */
public class Checkers extends Application {
    Design design = new Design();
    Board board = new Board();
    AudioPlayer audioPlayer = new AudioPlayer();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(Design.getGridPane(), 750, 750, Color.BLACK);
        audioPlayer.playBackgroundMusic("gameStart.wav");
        audioPlayer.playBackgroundMusic("background2.wav");
        scene.setOnMouseClicked(event -> board.readMouseEvent(event));
        scene.setOnKeyReleased(event -> board.readKeyboard(event));

        primaryStage.setTitle("JavaCheckers");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
