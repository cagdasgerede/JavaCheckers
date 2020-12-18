package pl.nogacz.checkers.application;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.robot.Robot;
import pl.nogacz.checkers.board.Board;

import org.junit.jupiter.api.Test;
import pl.nogacz.checkers.board.EditMenu;

import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CheckersTest {

    @Test
    public void readyButtonClicked() {
        EditMenu editMenu = new EditMenu();
        editMenu.getReadyButton().doClick();
        assertFalse(editMenu.getIsEditMenuActive());
    }


}