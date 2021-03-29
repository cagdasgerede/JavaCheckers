package pl.nogacz.checkers;

import java.util.HashMap;
import pl.nogacz.checkers.board.Coordinates;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pl.nogacz.checkers.application.Design;
import pl.nogacz.checkers.board.Board;
import pl.nogacz.checkers.pawns.PawnClass;

public class BoardPoint{
    int white_points;
    int black_points;
    

    private int[] getPoints(){
        int tmp_white_score = 0;
        int tmp_black_score = 0;
        HashMap<Coordinates, PawnClass> cacheBoard = new HashMap<>(Board.getBoard());
       for (PawnClass pawn : cacheBoard.values()) {
           if(pawn.getColor().isBlack()){
                tmp_black_score++;
           }else{
                tmp_white_score++;
           }
       }
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

    }

}