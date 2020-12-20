package pl.nogacz.checkers.application;

import pl.nogacz.checkers.application.Design;
import pl.nogacz.checkers.board.Board;
import pl.nogacz.checkers.board.Coordinates;
import pl.nogacz.checkers.pawns.Pawn;
import pl.nogacz.checkers.pawns.PawnClass;
import pl.nogacz.checkers.pawns.PawnColor;
import pl.nogacz.checkers.pawns.PawnMoves;

import javafx.application.Platform;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Restart {
    protected static Board board;
    protected static Design design;
    protected Board testBoard;
    protected Design testDesign;
    public Restart(){}
    public Restart(Design design,Board board){
        this.design = design;
        this.board = board;
        this.testBoard = board;
        this.testDesign = design;
    }

    public static void restartGame() {
        Platform.runLater(() -> {
            for (Map.Entry<Coordinates, PawnClass> entry : board.getBoard().entrySet()) {
                design.removePawn(entry.getKey());
            }
            board.getBoard().clear();
            for (int i = 1; i <= 7; i += 2) {
                for (int j = 0; j <= 2; j += 2) {
                    board.getBoard().put(new Coordinates(i, j), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
                }
            }
            for (int i = 0; i <= 6; i += 2) {
                board.getBoard().put(new Coordinates(i, 1), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
            }
            for (int i = 0; i <= 6; i += 2) {
                for (int j = 5; j <= 7; j += 2) {
                    board.getBoard().put(new Coordinates(i, j), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
                }
            }
            for (int i = 1; i <= 7; i += 2) {
                board.getBoard().put(new Coordinates(i, 6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
            }
            for (Map.Entry<Coordinates, PawnClass> entry : board.getBoard().entrySet()) {
                design.addPawn(entry.getKey(), entry.getValue());
            }
        }
        );
    }
    public static void restartTestGame(){//Just for Testing purpose
        board.getBoard().clear();
        for (int i = 1; i <= 7; i += 2) {
            for (int j = 0; j <= 2; j += 2) {
                board.getBoard().put(new Coordinates(i, j), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
            }
        }
        for (int i = 0; i <= 6; i += 2) {
            board.getBoard().put(new Coordinates(i, 1), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        }
        for (int i = 0; i <= 6; i += 2) {
            for (int j = 5; j <= 7; j += 2) {
                board.getBoard().put(new Coordinates(i, j), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
            }
        }
        for (int i = 1; i <= 7; i += 2) {
            board.getBoard().put(new Coordinates(i, 6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        }
    }
}