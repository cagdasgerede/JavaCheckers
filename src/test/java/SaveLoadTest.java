package pl.nogacz.checkers.application;

import pl.nogacz.checkers.application.Design;
import pl.nogacz.checkers.board.Board;
import pl.nogacz.checkers.board.Coordinates;
import pl.nogacz.checkers.pawns.PawnClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException; 
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SaveLoadTest {

    private static final String COORDS = "2\n0 0\n1 1";
    private static final String COUNT = "0"; 
    private static final String FILE_NAME = "test.txt"; 
    private static final String LIST = "0 0";

    @Test void parseCoordsSuccessfully() throws IOException{  
        File file = new File(FILE_NAME);
        FileWriter w = new FileWriter(file);
        w.write(COORDS);
        w.close();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        ArrayList<String> coords = new ArrayList<>();
        assertEquals(2, SaveLoad.parseCoords(reader, coords));
    }

    @Test void parseCoords_WhenNoCoords() throws IOException {
        File file = new File(FILE_NAME);
        FileWriter w = new FileWriter(file);
        w.write(COUNT);
        w.close();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        ArrayList<String> coords = new ArrayList<>();
        assertEquals(0, SaveLoad.parseCoords(reader, coords));
    }

    @Test void addPawn_WhenWrongParameters() {
        ArrayList<String> list = new ArrayList<>();
        list.add(LIST);
        HashMap<Coordinates, PawnClass> board = new HashMap<>();
        assertFalse(SaveLoad.addPawn(list, board, 0,2));
        assertFalse(SaveLoad.addPawn(list, board, 1,2));
        assertFalse(SaveLoad.addPawn(list, board, 2,0));
        assertFalse(SaveLoad.addPawn(list, board, 2,1));
        assertFalse(SaveLoad.addPawn(list, board, 2,2));  
    }

    @Test void addPawn_WhenListIsEmpty() {
        ArrayList<String> list = new ArrayList<>();
        HashMap<Coordinates, PawnClass> board = new HashMap<>();
        assertFalse(SaveLoad.addPawn(list, board, 0,0));
        assertFalse(SaveLoad.addPawn(list, board, 0,1));
        assertFalse(SaveLoad.addPawn(list, board, 1,0));
        assertFalse(SaveLoad.addPawn(list, board, 1,1));  
    }
}
