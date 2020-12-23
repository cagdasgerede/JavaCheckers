package pl.nogacz.checkers.board;

import pl.nogacz.checkers.pawns.Pawn;
import pl.nogacz.checkers.pawns.PawnClass;
import pl.nogacz.checkers.pawns.PawnColor;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BoardTest {
    HashMap<Coordinates, PawnClass> boardMap = new HashMap<>();
    @BeforeEach
    void setUp(){
        boardMap.put(new Coordinates(1, 0), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        boardMap.put(new Coordinates(3, 0), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        boardMap.put(new Coordinates(5, 0), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        boardMap.put(new Coordinates(7, 0), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        boardMap.put(new Coordinates(0, 1), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        boardMap.put(new Coordinates(2, 1), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        boardMap.put(new Coordinates(4, 1), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        boardMap.put(new Coordinates(6, 1), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        boardMap.put(new Coordinates(1, 2), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        boardMap.put(new Coordinates(3, 2), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        boardMap.put(new Coordinates(5, 2), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        boardMap.put(new Coordinates(7, 2), new PawnClass(Pawn.PAWN, PawnColor.BLACK));

        boardMap.put(new Coordinates(0, 5), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        boardMap.put(new Coordinates(2, 5), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        boardMap.put(new Coordinates(4, 5), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        boardMap.put(new Coordinates(6, 5), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        boardMap.put(new Coordinates(1, 6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        boardMap.put(new Coordinates(3, 6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        boardMap.put(new Coordinates(5, 6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        boardMap.put(new Coordinates(7, 6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        boardMap.put(new Coordinates(0, 7), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        boardMap.put(new Coordinates(2, 7), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        boardMap.put(new Coordinates(4, 7), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        boardMap.put(new Coordinates(6, 7), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
    }

    @Test
    void relativeScoreAtStartTest(){
        TreeMaker maker = new TreeMaker(); 
        assertEquals(0, maker.relativeScore(boardMap, true));
    }
    @Test
    void relativeScoreWhenPcWinningTest(){
        TreeMaker maker = new TreeMaker();
        boardMap.remove(new Coordinates(0, 7));
        assertEquals(1, maker.relativeScore(boardMap, true));
    }
    @Test
    void relativeScoreWhenPlayerWinningTest(){
        TreeMaker maker = new TreeMaker();
        boardMap.remove(new Coordinates(7, 2));
        boardMap.remove(new Coordinates(1, 2));
        assertEquals(-2, maker.relativeScore(boardMap, true));
    }
    @Test
    void scoreWithQueenTest(){
        TreeMaker maker = new TreeMaker();
        boardMap.remove(new Coordinates(6, 7));
        boardMap.remove(new Coordinates(4, 7));
        boardMap.put(new Coordinates(0, 0), new PawnClass(Pawn.QUEEN, PawnColor.WHITE));
        assertEquals(13, maker.playerScore(boardMap));
    }
    @Test
    void scoreWithoutQueenTest(){
        TreeMaker maker = new TreeMaker();
        boardMap.remove(new Coordinates(6, 7));
        boardMap.remove(new Coordinates(4, 7));
        assertEquals(10, maker.playerScore(boardMap));
    }
    @Test
    void findCorrectMoveTest(){
        BoardTree child1 = new BoardTree(null, new Coordinates(0, 1), new Coordinates(3,5), 10);
        BoardTree child2 = new BoardTree(null, new Coordinates(3, 2), new Coordinates(0,7), 20);
        BoardTree child3 = new BoardTree(null, new Coordinates(7, 0), new Coordinates(6,5), 30);
        BoardTree parent = new BoardTree(null, null, null, 0, child1, child2, child3);
        assertEquals(20, parent.findMoveScore(new Coordinates(3, 2), new Coordinates(0,7)));
    }
    @Test
    void noBetterMoveInFirstRoundTest(){
        TreeMaker maker = new TreeMaker();
        BoardTree decisionTree = maker.construcTree(false, boardMap);
        assertEquals(0, decisionTree.findMaxScore());
    }
    @Test
    void possibleMoveNumberInFirstRoundTest(){
        TreeMaker maker = new TreeMaker();
        BoardTree decisionTree = maker.construcTree(false, boardMap);
        assertEquals(7, decisionTree.getNumChildren());
    }
}   
        