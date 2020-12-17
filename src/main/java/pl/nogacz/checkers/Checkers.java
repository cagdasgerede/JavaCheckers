package pl.nogacz.checkers;

import pl.nogacz.checkers.application.Design;
import pl.nogacz.checkers.board.Board;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


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
        Scene scene = setScene();
        primaryStage.setTitle("JavaCheckers");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public Scene setScene(){
        VBox root  =new VBox(design.getGridPane());
        Scene scene =  new Scene(root, 750, 750, Color.BLACK);
        scene.setOnMouseClicked(event -> board.readMouseEvent(event));
        scene.setOnKeyReleased(event -> board.readKeyboard(event));
        return scene;
    }

}
