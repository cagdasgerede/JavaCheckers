package pl.nogacz.checkers.board;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JLabel;

import pl.nogacz.checkers.board.Coordinates;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pl.nogacz.checkers.application.Design;
import pl.nogacz.checkers.board.Board;
import pl.nogacz.checkers.pawns.Pawn;
import pl.nogacz.checkers.pawns.PawnClass;
import pl.nogacz.checkers.board.BoardTree;


import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;


public class BoardPoint{

    int white_points;
    int black_points;

    static Slider s;

    static int queenValue = 300;
    static int pawnValue = 2;
    static int positionMultiplier = 5;

    boolean isCompRound = false;
    BoardTree tree;

    

    public BoardPoint(Slider s){

        if(s!=null){
            s.setShowTickLabels(false);
            s.setShowTickMarks(true);
        }
        this.s = s;

    }



    private double getPoints(int side){ //differ perth differ val
        /**
         * Heuristics:
         * 1- Turns get more important as game proceeds 
         * 2- Queen's are much more important than others
         * 3- Getting pawns close to end of the board is valuable
         * 
         **/ 
        
        HashMap<Coordinates, PawnClass> cacheBoard = new HashMap<>(Board.getBoard());
        //printBoard(cacheBoard);
        this.tree = new BoardTree();
        
        tree.setSide(side);
        tree.generateTree(3);
        double val = tree.calculateWithDfs(tree.getRoot(), side);
        //System.out.println("In this table possible points in three moves: "+ val + "      side: " + side);
        
        //this.tree = new BoardTree();
        
        //tree.setSide(side);
        //tree.generateTree(1);
        //val = tree.calculateWithDfs(tree.getRoot(), side);
        //System.out.println("In this table possible points in one moves: "+ val + "      side: " + side);
        return val;

    }
    static String printBoard(HashMap<Coordinates, PawnClass> oneBoard){
            String board = "";
            char[][] table = new char[8][8];
            for(Coordinates oneEnt : oneBoard.keySet()){
                table[oneEnt.getY()][oneEnt.getX()] = oneBoard.get(oneEnt).getColor().isWhite() ? 'w' : 'b';
            } 
            for(int i = 0;i<8;i++){
                for(int j = 0;j<8;j++){
                    board = board + (table[i][j]!='w' && table[i][j]!='b' ? " " : table[i][j]);
                    System.out.print(table[i][j]!='w' && table[i][j]!='b' ? " " : table[i][j]);
                }
                board = board + "\n";
                System.out.println();
            }
            return board;
    }
    void updatePoints(int side){
        double pts = getPoints(side);
        this.s.setMax(1);
        s.setValue(pts);
    }
    public static int[] evaluate(HashMap<Coordinates, PawnClass> board){ //test
        int index = 0;
        int tmp_white_score = 0;
        int tmp_black_score = 0;
        for (Entry<Coordinates , PawnClass > e  : board.entrySet()) { // bir tane daha dongu icine al cocuklarda donsun tek tek ouanlarinji hesaplasin

            PawnClass pawn = e.getValue();
            Coordinates coordinate = e.getKey();
            
            
            if(pawn.getColor().isBlack()){
 
                if(pawn.getPawn().equals(Pawn.QUEEN)){
                 tmp_black_score += queenValue;
                }else{
                 tmp_black_score += pawnValue * positionMultiplier * (1 + coordinate.getY());
                }
                
                 
            }else{
             if(pawn.getPawn().equals(Pawn.QUEEN)){
                 tmp_white_score += queenValue;
                }else{
                 tmp_white_score += pawnValue * positionMultiplier * (8- coordinate.getY());
                }
                
            }
            index++;
        }
 
        int[] newPoints = {tmp_white_score, tmp_black_score};
        return newPoints;
     
    }

}