package pl.nogacz.checkers.board;
import org.junit.jupiter.api.Test;
import pl.nogacz.checkers.pawns.Pawn;
import pl.nogacz.checkers.pawns.PawnClass;
import pl.nogacz.checkers.pawns.PawnColor;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EditMenuTest {

    @Test
    void pawnSelectedTest() {
        Board board = mock(Board.class);
        when(board.getIsPawnSelected()).thenReturn(true);
        EditMenu.setIsPawnSelected(true);
        assertEquals(board.getIsPawnSelected(),Board.isPawnSelected);
    }
    @Test
    void pawnNotSelectedTest() {
        Board board = mock(Board.class);
        when(board.getIsPawnSelected()).thenReturn(false);
        EditMenu.setIsPawnSelected(false);
        assertEquals(board.getIsPawnSelected(),Board.isPawnSelected);
    }

    @Test
    void editMenuActiveTest(){
        Board board = mock(Board.class);
        when(board.getIsEditMenuActive()).thenReturn(true);
        EditMenu.setIsEditMenuActive(true);
        assertEquals(board.getIsEditMenuActive(),Board.isEditMenuActive);
    }

    @Test
    void editMenuNotActiveTest(){
        Board board = mock(Board.class);
        when(board.getIsEditMenuActive()).thenReturn(false);
        EditMenu.setIsEditMenuActive(false);
        assertEquals(board.getIsEditMenuActive(),Board.isEditMenuActive);
    }

    @Test
    void getSelectedCoordinatesTest(){
        Board board = mock(Board.class);
        when(board.getSelectedCoordinatesNotStatic()).thenReturn(new Coordinates(0,0));
        EditMenu.setSelectedCoordinates(new Coordinates(0,0));
        assertEquals(board.getSelectedCoordinatesNotStatic(),Board.selectedCoordinates);
    }

    @Test
    void getAddPawnCoordinatesTest(){
        EditMenu.setAddPawnCoordinates(new Coordinates(0,0));
        assertEquals(new Coordinates(0,0),EditMenu.getAddPawnCoordinates());
    }

    @Test
    void replacePawnTest(){
        Board.getBoard().put(new Coordinates(0,0),new PawnClass(Pawn.PAWN,PawnColor.BLACK));
        EditMenu.replacePawnForTest(new Coordinates(0,0), new Coordinates(1,1));
        PawnClass pawn = Board.getPawn(new Coordinates(1,1));
        assertNull(Board.getBoard().get(new Coordinates(0,0)));
        assertTrue((pawn.getPawn() == Pawn.PAWN && pawn.getColor() == PawnColor.BLACK));
    }

    @Test
    void removePawnTest(){
        Coordinates coordinates = new Coordinates(5,5);
        Board.getBoard().put(coordinates,new PawnClass(Pawn.PAWN,PawnColor.WHITE));
        EditMenu.removePawn(new Coordinates(5,5));
        assertNull(Board.getBoard().get(coordinates));
    }

    @Test
    void addPawnTest(){
        Coordinates coordinates = new Coordinates(0,0);
        assertNull(Board.getBoard().get(coordinates));
        PawnClass pawn = new PawnClass(Pawn.PAWN,PawnColor.BLACK);
        EditMenu.addPawn(coordinates,pawn);
        assertTrue((Board.getBoard().get(coordinates).getPawn() == Pawn.PAWN
                && Board.getBoard().get(coordinates).getColor() == PawnColor.BLACK));
    }

}