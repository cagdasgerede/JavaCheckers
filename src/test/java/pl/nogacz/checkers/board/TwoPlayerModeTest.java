package pl.nogacz.checkers.board;

import pl.nogacz.checkers.board.Board;
import pl.nogacz.checkers.board.Coordinates;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class TwoPlayerModeTest{
    @Test void TestWhenStartIsWhiteRound(){
        Board b = new Board (1);
        assertTrue(b.getWhiteRound());
    }

    @Test void TestWhenWhiteRoundBlackPieceCannotBeSelected(){
        Board b = new Board(1);
        Coordinates c = new Coordinates(3,2);
        // click a black pawn when it is white's round(at start)
        b.handleMouse(c);
        // it should not be selected
        assertFalse(b.getSelected());
    }

    @Test void TestWhenWhiteRoundWhitePieceCanBeSelected(){
        Board b = new Board(1);
        Coordinates c = new Coordinates(2,5);
        // click a white pawn
        b.handleMouse(c);
        assertTrue(b.getSelected());
    }

    @Test void TestWhenBlackRoundBlackPieceCanBeSelected(){
        Board b = new Board (1);
        // move a white piece
        Coordinates c = new Coordinates(2,5);
        Coordinates c2 = new Coordinates(3,4);
        b.handleMouse(c);
        b.handleMouse(c2);
        // now it is black's round
        Coordinates c3 = new Coordinates(3,2);
        // click a black pawn
        b.handleMouse(c3);
        // it should be selected
        assertTrue(b.getSelected());
        // unclick
        b.handleMouse(c3);
        // click a white pawn
        b.handleMouse(c2);
        // it should not be selected
        assertFalse(b.getSelected());
    }
}

