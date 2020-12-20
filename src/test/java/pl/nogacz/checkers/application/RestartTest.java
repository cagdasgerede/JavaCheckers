package pl.nogacz.checkers.application;

import pl.nogacz.checkers.application.Restart;
import pl.nogacz.checkers.board.*;
import pl.nogacz.checkers.application.Design;
import pl.nogacz.checkers.pawns.Pawn;
import pl.nogacz.checkers.pawns.PawnClass;
import pl.nogacz.checkers.pawns.PawnColor;
import pl.nogacz.checkers.pawns.PawnMoves;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RestartTest {

    @Test void emptyRestart(){
        int countBlack = 0;
        int countWhite = 0;
        Restart.board.getBoard().clear();
        Restart.restartTestGame();
        Restart.board.getBoard().size();
        assertEquals(24,Restart.board.getBoard().size());
        for (Map.Entry<Coordinates, PawnClass> entry : Restart.board.getBoard().entrySet()) {
            if( entry.getValue().getPawn().isPawn() &&  entry.getValue().getColor().isBlack()){
                countBlack++;
            }
            if(entry.getValue().getPawn().isPawn() &&  entry.getValue().getColor().isWhite()){
                countWhite++;
            }
        }
        assertEquals(12,countBlack);
        assertEquals(12,countWhite);
    }

    @Test void restartTest(){
        int countBlack = 0;
        int countWhite = 0;
        for (int i = 0; i <5 ; i++) {
            Restart.board.getBoard().put(new Coordinates(i, i), new PawnClass(Pawn.PAWN,  PawnColor.BLACK));
            Restart.board.getBoard().put(new Coordinates(i+5, i+5), new PawnClass(Pawn.PAWN,  PawnColor.WHITE));
        }
        assertEquals(10,Restart.board.getBoard().size());
        Restart.restartTestGame();
        Restart.board.getBoard().size();
        assertEquals(24,Restart.board.getBoard().size());
        for (Map.Entry<Coordinates, PawnClass> entry : Restart.board.getBoard().entrySet()) {
            if( entry.getValue().getPawn().isPawn() &&  entry.getValue().getColor().isBlack()){
                countBlack++;
            }
            if(entry.getValue().getPawn().isPawn() &&  entry.getValue().getColor().isWhite()){
                countWhite++;
            }
        }
        assertEquals(12,countBlack);
        assertEquals(12,countWhite);
    }
    @Test void nullrestartTest(){
        Restart.board.setBoard(null);
        assertThrows(
                NullPointerException.class,
                () -> { Restart.restartTestGame(); }
                );
        HashMap<Coordinates, PawnClass> test = new HashMap<>();
        Restart.board.setBoard(test);
    }
}
