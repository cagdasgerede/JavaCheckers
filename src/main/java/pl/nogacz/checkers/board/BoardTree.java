package pl.nogacz.checkers.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BoardTree {
    private BoardUtil board;
    private Coordinates oldCoordinates;
    private Coordinates newCoordinates;
    private int score;
    private ArrayList<BoardTree> children;

    public BoardTree(BoardUtil board, Coordinates oldCoordinates, Coordinates newCoordinates, int score, BoardTree ... children){
        this.board = board;
        this.children = new ArrayList<>(Arrays.asList(children));
        this.score = score;
        this.oldCoordinates = oldCoordinates;
        this.newCoordinates = newCoordinates;
    }

    public int findMoveScore(Coordinates oldC, Coordinates newC){
        int score = 0 ;
        for(int i = 0 ; i < children.size() ; i++){
            BoardTree child = this.getChild(i);
            if(child.oldCoordinates.equals(oldC) && child.newCoordinates.equals(newC)){
                score = child.getScore();
                break;
            }
        }
        return score;
    }

    public int findMaxScore(){
        int max = -50;
        int index = 0;
        for(int i = 0 ; i < children.size() ; i++ ){
            BoardTree child = this.getChild(i);
            int sMin = 50;
            //find max leaf
            for(BoardTree sChild : child.getChildren()){
                int leafMax = -50;
                for(BoardTree tchild : sChild.getChildren()){
                    if(tchild.getScore() >= leafMax){
                        leafMax = tchild.getScore();
                    }
                }
                sChild.setScore(leafMax);
                //find min on second level (enemy move, assume that enemy will select the best move for himself/herself)
                if(sChild.getScore() <= sMin){
                    sMin = sChild.getScore();
                }
            }
            child.setScore(sMin);
            //find max on the first layer
            if(child.getScore() >= max){
                max = child.getScore();
                index = i;
            }
        }
        this.score = this.getChild(index).getScore();
        return this.score;
    }

    public BoardUtil getBoard(){return board;}
    public int getScore(){return score;}
    public List<BoardTree> getChildren(){return children;}
    public int getNumChildren(){return children.size();}
    public void setScore(int score){this.score = score; }
    public BoardTree getChild(int index){return children.get(index);}
    public void addChild(BoardTree child){children.add(child);}
    public void addChild(BoardTree ... children){
        for(BoardTree board: children){
            addChild(board);
        }
    }
}
