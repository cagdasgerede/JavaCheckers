package test.java;

import pl.nogacz.checkers.application.Design;
import pl.nogacz.checkers.board.Board;
import pl.nogacz.checkers.board.Coordinates;
import pl.nogacz.checkers.pawns.Pawn;
import pl.nogacz.checkers.pawns.PawnClass;
import pl.nogacz.checkers.pawns.PawnColor;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import main.java.pl.nogacz.checkers.application.SaveLoadOption;

public class SaveLoadOptionTest{

    @Test void saveAndLoadSuccesfully() throws IOException{  
        
        boolean control = true;
        HashMap<Coordinates, PawnClass> board = Board.getBoard();
        ArrayList <PawnClass> pawnList = new ArrayList();
        pawnList.add(new PawnClass(Pawn.QUEEN, PawnColor.WHITE));
        pawnList.add(new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        pawnList.add(new PawnClass(Pawn.QUEEN, PawnColor.BLACK));
        pawnList.add(new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        pawnList.add(null);
        
        for(int x = 0; x < 8 ; x++){
            for(int y = 0; y < 8; y++){
                board.put(new Coordinates(x, y), pawnList.get(((int)Math.random())%4));
            }
        }

        HashMap<Coordinates, PawnClass> subBoard = new HashMap<>();
        for(int x = 0; x < 8 ; x++){
            for(int y = 0; y < 8; y++){

                if(Board.isFieldNotNull(new Coordinates(x,y))){
                    subBoard.put(new Coordinates(x,y), Board.getPawn(new Coordinates(x,y)));
                }
            }
        }

        SaveLoadOption.saveTxt("local");
        //SaveLoadOption.loadTxt("local");

        for(int x = 0; x < 8 ; x++){
            for(int y = 0; y < 8; y++){
                
                if(Board.getPawn(new Coordinates(x,y)).getPawn() != subBoard.get(new Coordinates(x,y)).getPawn() || 
                Board.getPawn(new Coordinates(x,y)).getColor() != subBoard.get(new Coordinates(x,y)).getColor()){
                    control = false;
                }
            }
        }

        assertTrue(control);
    }
}
