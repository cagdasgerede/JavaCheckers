package pl.nogacz.checkers.application;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import pl.nogacz.checkers.board.Coordinates;
import pl.nogacz.checkers.pawns.PawnClass;

/**
 * @author Dawid Nogacz on 17.05.2019
 */
public class Design {
    private static final int TOTAL_LENGTH_OF_HEALTH_BARS = 680;
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
    }

    public static void generateHealthBar(float playerScore, float computerScore){
        removePawn(new Coordinates(0, 8));
        removePawn(new Coordinates(0, 8));

        //portion the total length of bars according to the current score between user and pc
        float total = computerScore + playerScore;
        int userBarLength = (int)(TOTAL_LENGTH_OF_HEALTH_BARS * playerScore / total);
        int pcBarLength   = (int)(TOTAL_LENGTH_OF_HEALTH_BARS * computerScore / total);
        Rectangle recUser = new Rectangle(0, 0, userBarLength, 15);
        Rectangle recPc   = new Rectangle(0, 0, pcBarLength, 15);

        //add user bar
        recUser.setArcHeight(15);
        recUser.setArcWidth(15);
        recUser.setStroke(Color.BLACK);
        recUser.setFill(Color.DARKKHAKI);
        GridPane.setRowIndex(recUser, 8);
        GridPane.setColumnIndex(recUser, 0);
        GridPane.setColumnSpan(recUser, 8);
        gridPane.getChildren().add(recUser);
        GridPane.setHgrow(recUser, Priority.ALWAYS);
        GridPane.setHalignment(recUser, HPos.LEFT);

        //add computer bar
        recPc.setArcHeight(15);
        recPc.setArcWidth(15);
        recPc.setStroke(Color.DARKKHAKI);
        recPc.setFill(Color.BLACK);
        GridPane.setRowIndex(recPc, 8);
        GridPane.setColumnIndex(recPc, 0);
        GridPane.setColumnSpan(recPc, 8);
        gridPane.getChildren().add(recPc);
        GridPane.setHgrow(recPc, Priority.ALWAYS);
        GridPane.setHalignment(recPc, HPos.RIGHT);
    }
}
