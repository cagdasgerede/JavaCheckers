package pl.nogacz.checkers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pl.nogacz.checkers.application.Design;
import pl.nogacz.checkers.board.Board;

import javafx.scene.control.MenuBar;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * @author Dawid Nogacz on 17.05.2019
 */
public class Checkers extends Application implements EventHandler<ActionEvent> {
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

    @Override
    public void handle(ActionEvent event) {
       board.isEditMenuActive = !board.isEditMenuActive;
    }
}
