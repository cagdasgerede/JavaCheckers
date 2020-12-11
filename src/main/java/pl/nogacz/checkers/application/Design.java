package pl.nogacz.checkers.application;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import pl.nogacz.checkers.board.Coordinates;
import pl.nogacz.checkers.pawns.PawnClass;

import javafx.geometry.Orientation;
import pl.nogacz.checkers.pawns.PawnColor;
import javafx.scene.control.Slider;
import java.util.HashMap;
import java.util.Map;
/**
 * @author Dawid Nogacz on 17.05.2019
 */
public class Design {
    private static GridPane gridPane = new GridPane();
    private static Image lightMove = new Image(Resources.getPath("light.png"));

    public Design() {
        createBoardBackground();
        generateEmptyBoard();
    }

    public static GridPane getGridPane() {
        return gridPane;
    }

    private void createBoardBackground() {
        Image background = new Image(Resources.getPath("board.png"));
        BackgroundSize backgroundSize = new BackgroundSize(750, 750, false, false, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(background, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
        gridPane.setBackground(new Background(backgroundImage));
    }

    private void generateEmptyBoard() {
        gridPane.setMinSize(750, 750);
        gridPane.setMaxSize(750, 750);

        for (int i = 0; i < 8; i++) {
            ColumnConstraints column = new ColumnConstraints(85);
            column.setHgrow(Priority.ALWAYS);
            column.setHalignment(HPos.CENTER);
            gridPane.getColumnConstraints().add(column);

            RowConstraints row = new RowConstraints(85);
            row.setVgrow(Priority.ALWAYS);
            row.setValignment(VPos.CENTER);
            gridPane.getRowConstraints().add(row);
        }

        gridPane.setPadding(new Insets(37, 0, 0, 37));
    }

    public static void addPawn(Coordinates coordinates, PawnClass pawn) {
        gridPane.add(pawn.getImage(), coordinates.getX(), coordinates.getY());
    }

    public static void addLightPawn(Coordinates coordinates, PawnClass pawn) {
        gridPane.add(pawn.getLightImage(), coordinates.getX(), coordinates.getY());
    }

    public static void addLightMove(Coordinates coordinates) {
        gridPane.add(new ImageView(lightMove), coordinates.getX(), coordinates.getY());
    }

    public static void removePawn(Coordinates coordinates) {
       gridPane.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == coordinates.getX() && GridPane.getRowIndex(node) == coordinates.getY());
       /* 
        ObservableList<javafx.scene.Node> childrens = gridPane.getChildren();
        for(javafx.scene.Node node : childrens){
            if(node != null){
                if(GridPane.getColumnIndex(node) == coordinates.getX() && GridPane.getRowIndex(node) == coordinates.getY()) {
                    gridPane.getChildren().remove(node);
                    break;
                }
            }
        }*/
    }

    public static void healthBar(HashMap<Coordinates, PawnClass> board){
        int computerScore = 0;
        int playerScore = 0;
        for(Map.Entry<Coordinates, PawnClass> entry : board.entrySet()) {
            if(entry.getValue().getColor() == PawnColor.BLACK){
                computerScore++;
            }else if(entry.getValue().getColor() == PawnColor.WHITE){
                playerScore++;
            }
        }
        generateSlider(playerScore, computerScore);
    }

    public static void generateSlider(int playerScore, int computerScore){
        //EKSIKLER: slider hareket edebiliyor, iki farklı renk yok
        //oncekini siliyor
        removePawn(new Coordinates(0,8));
        //slider ozellikleri
        int total = computerScore+playerScore;
        Slider slider = new Slider(0, total, playerScore);
        slider.setOrientation(Orientation.HORIZONTAL);
        slider.setMaxWidth(600);
        slider.setBlockIncrement(0);
        slider.setSnapToTicks(true);
        slider.setMajorTickUnit(total/2);
        slider.setMinorTickCount(0);
        //interface'e yerlestirme
        GridPane.setRowIndex(slider, 8);
        GridPane.setColumnIndex(slider, 0);//to avoid error
        GridPane.setColumnSpan(slider, 8);
        gridPane.getChildren().add(slider);
        GridPane.setHgrow(slider, Priority.ALWAYS);
    }
}
