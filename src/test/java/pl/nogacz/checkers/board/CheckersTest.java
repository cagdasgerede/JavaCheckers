package pl.nogacz.checkers.board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CheckersTest {

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
}