package pl.nogacz.checkers.application;

import pl.nogacz.checkers.application.StartMenu;
import pl.nogacz.checkers.board.Board;
import pl.nogacz.checkers.Checkers;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class StartMenuTest {
    @Test
    void startClicked() {
        Board board = mock(Board.class);
        StartMenu startMenu = new StartMenu(board);
        startMenu.getStartButton().doClick();
        assertFalse(startMenu.isMenuOpen());
    }

    @Test
    void startIsNotClicked() {
        Board board = mock(Board.class);
        StartMenu startMenu = new StartMenu(board);
        assertTrue(startMenu.isMenuOpen());
    }
}
