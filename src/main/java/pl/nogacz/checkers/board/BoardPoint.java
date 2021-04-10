package pl.nogacz.checkers.board;

import java.util.HashMap;
import java.util.Hashtable;
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

import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;

public class BoardPoint{
    int white_points;
    int black_points;

    enum Turn{ 
        white,
        black
    }

    static Turn turn = Turn.white;

    static Slider s;

    static int queenValue = 100;
    static int pawnValue = 2;
    static int positionMultiplier = 5;
    static int turnAdventage = 20;
    

    BoardPoint(Slider s){
        s.setMax(100);
        s.setMin(0);
        s.setValueChanging(false);
        s.setShowTickLabels(false);
        s.setShowTickMarks(true);
        this.s = s;

    }

    private boolean isWhitesTurn(){
        return this.turn.equals(Turn.white);
    }
    private boolean isBlacksTurn(){
        return this.turn.equals(Turn.black);
    }
    private void changeTurns(){
        if(isBlacksTurn())
            this.turn = Turn.white;
        else
            this.turn = Turn.black;
    }

    private int[] getPoints(){
        /**
         * Heuristics:
         * 1- Turns get more important as game proceeds 
         * 2- Queen's are much more important than others
         * 3- Getting pawns close to end of the board is valuable
         * 
         **/ 
        
        int tmp_white_score = 0;
        int tmp_black_score = 0;
        HashMap<Coordinates, PawnClass> cacheBoard = new HashMap<>(Board.getBoard());

        int index = 0;
       for (Entry<Coordinates , PawnClass > e  : cacheBoard.entrySet()) {

           PawnClass pawn = e.getValue();
           Coordinates coordinate = e.getKey();
           
           
           if(pawn.getColor().isBlack()){

               if(pawn.getPawn().equals(Pawn.QUEEN)){
                tmp_black_score += queenValue;
               }else{
                tmp_black_score += pawnValue * positionMultiplier * ( coordinate.getY());
               }
               
                
           }else{
            if(pawn.getPawn().equals(Pawn.QUEEN)){
                tmp_white_score += queenValue;
               }else{
                tmp_white_score += pawnValue * positionMultiplier * (7- coordinate.getY());
               }
               
           }
           index++;
       }
       /*if(isBlacksTurn()){
        tmp_black_score += turnAdventage;
        }
        if(isWhitesTurn()){
            tmp_white_score += turnAdventage;
        }
        changeTurns();*/
       int[] newPoints = {tmp_white_score, tmp_black_score};
       return newPoints;
    }
    private void setPoints(int w, int b){
        white_points = w;
        black_points = b;
    }
    
    public String toString(){
        return "white : "+white_points+", black : "+black_points;
    }
    void updatePoints(){
        int[] news = getPoints();
        setPoints(news[0], news[1]);
        System.out.println(
            this.toString()
        );
        this.s.setMax(news[0] + news[1]);
        s.setValue(news[0]);
    }

}