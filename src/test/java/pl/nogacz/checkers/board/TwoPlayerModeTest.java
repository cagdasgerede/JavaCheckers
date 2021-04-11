package pl.nogacz.checkers.board;

import pl.nogacz.checkers.board.Board;
import pl.nogacz.checkers.board.Coordinates;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class TwoPlayerModeTest{

    @Test void TestWhenBlackRoundWhitePieceCanBeSelected(){
        // firstly select 2,5 then move the pawn to 3,4 then round should be black's then, when mouse clicked on a 
        // white pawn isSelected should be false because round is black's
        Board b = new Board (1);
        Coordinates c = new Coordinates(210,465);
        Coordinates c2 = new Coordinates(300,385);
        b.handleMouse(c);
        b.handleMouse(c2);
        Coordinates c3 = new Coordinates(300,385);
        b.handleMouse(c3);
        assertFalse(b.getSelected());
    }

    @Test void TestWhenStartIsWhiteRound(){
        Board b = new Board (1);
        assertTrue(b.getWhiteRound());
    }

}

