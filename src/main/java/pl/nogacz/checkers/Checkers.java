package pl.nogacz.checkers;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pl.nogacz.checkers.application.Design;
import pl.nogacz.checkers.board.Board;
import javafx.scene.control.Slider;

import  javafx.scene.layout.VBox;

/**
 * @author Dawid Nogacz on 17.05.2019
 */
public class Checkers extends Application {
    Design design = new Design();
    Board board = new Board();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox stackPane = new VBox();
        
        stackPane.getChildren().addAll(design.getSlider(),design.getGridPane());
        Scene scene = new Scene(stackPane, 750, 750, Color.BLACK);
        scene.setOnMouseClicked(event -> board.readMouseEvent(event));
        scene.setOnKeyReleased(event -> board.readKeyboard(event));

        primaryStage.setTitle("JavaCheckers");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
