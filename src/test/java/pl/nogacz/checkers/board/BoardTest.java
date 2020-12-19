package pl.nogacz.checkers.board;

import pl.nogacz.checkers.Checkers;
import pl.nogacz.checkers.board.Board;
import pl.nogacz.checkers.application.Design;
import pl.nogacz.checkers.pawns.Pawn;
import pl.nogacz.checkers.pawns.PawnClass;
import pl.nogacz.checkers.pawns.PawnColor;
import pl.nogacz.checkers.pawns.PawnMoves;

import static org.mockito.Mockito.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardTest {

    /*@Test void emptyRestartTest() {
        Board board = mock(Board.class);
       // Board.Menu menu = board.new Menu();
        *//* verify(board,times(12)).getBoard().put(ArgumentMatchers.eq(new Coordinates(anyInt(), anyInt()), new PawnClass(Pawn.PAWN,  PawnColor.WHITE)));
        verify(board,times(12)).getBoard().put(ArgumentMatchers.eq(new Coordinates(anyInt(), anyInt()), new PawnClass(Pawn.PAWN,  PawnColor.BLACK)));*//*
        board.restart();
        ArgumentCaptor<Coordinates> coordinatesArgumentCaptor = ArgumentCaptor.forClass(Coordinates.class);
        ArgumentCaptor<PawnClass> pawnClassArgumentCaptor = ArgumentCaptor.forClass(PawnClass.class);
        verify(board, never()).restart();
        //verify(board, times(5)).getBoard().put(coordinatesArgumentCaptor.capture(), pawnClassArgumentCaptor.capture());
       // verify(board,never()).getBoard().put(new Coordinates(anyInt(), anyInt()), new PawnClass(Pawn.PAWN,  PawnColor.WHITE));
        //verify(board, times(681864)).restart();
        List<Coordinates> coordinatesList = coordinatesArgumentCaptor.getAllValues();
        //System.out.println(coordinatesArgumentCaptor.getAllValues());
        List<PawnClass> pawnClassList = pawnClassArgumentCaptor.getAllValues();
        int countBlack = 0;
        int countWhite = 0;
        for (PawnClass pawn : pawnClassList) {
            if( pawn.getPawn().isPawn() &&  pawn.getColor().isBlack()){
                countBlack++;
            }
            if(pawn.getPawn().isPawn() &&  pawn.getColor().isWhite()){
                countWhite++;
            }
        }
        *//*System.out.println(pawnClassList);
        System.out.println(countBlack);
        assertEquals(12,countBlack);
        assertEquals(12,countWhite);*//*
    }*/
     @Test void trying(){
       // Board board = new Board(); this lines give that error
       // board.restart();
    }
}
