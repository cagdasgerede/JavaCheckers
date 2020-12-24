package pl.nogacz.checkers;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pl.nogacz.checkers.application.Design;
import pl.nogacz.checkers.board.Board;

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
        Scene scene = new Scene(design.getGridPane(), 750, 750, Color.BLACK);
        boolean single=false;
        if(single){
            scene.setOnMouseClicked(event -> board.readMouseEvent(event));
        }else{
        scene.setOnMouseClicked(event -> board.readMouseEventWhite(event));
      
    }
        scene.setOnKeyReleased(event -> board.readKeyboard(event));
        primaryStage.setTitle("JavaCheckers");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
