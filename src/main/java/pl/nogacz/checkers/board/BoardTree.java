package pl.nogacz.checkers.board;

import java.util.ArrayList;
import java.util.HashMap;

import pl.nogacz.checkers.board.Board;
import pl.nogacz.checkers.board.BoardPoint;
import pl.nogacz.checkers.board.Coordinates;
import pl.nogacz.checkers.pawns.PawnClass;
import pl.nogacz.checkers.pawns.PawnMoves;

public class BoardTree {
    
    private BoardNode root;
    private int depth;
    private int side;

    public BoardTree(){
        
        this.root = new BoardNode( Board.getBoard(), 1, null);    }  

    public BoardNode getRoot() {
        return this.root;
    }

    public void setRoot(BoardNode root) {
        this.root = root;
    }

    public int getDepth() {
        return this.depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getSide() {
        return this.side;
    }

    public void setSide(int side) {
        this.side = side;
    }
     
    public int getPoint(int side, int depth){
        this.side = side;
        generateTree(depth);
        return -1;
    }
    public void generateTree(int depth){
        generateChildren(root, side, depth);
    }
    private void generateChildren(BoardNode from, int side, int iterationLeft){
        if(iterationLeft == 0) // achieved needed depth
            return;
        from.generateMyChildren(side);
        for(int i = iterationLeft;i>0;i--){
            for(BoardNode child: from.getChlidren())
                generateChildren(child, side*-1, iterationLeft -1);;
        }
    }

    public double calculateWithDfs(BoardNode node, int side){
        BoardNode tmp = node;
        if(node.getChlidren().size()==0){

            int[] points =  BoardPoint.evaluate(node.getBoard());
            return (double)points[0]/((double)points[0] + points[1]);
        }
        ArrayList<BoardNode> children = node.getChlidren();
        double[] childPoints = new double[children.size()];
        for(int i = 0;i<childPoints.length;i++){
            childPoints[i] = calculateWithDfs(children.get(i), side *-1);
        }
        double max = childPoints[0];
        double total = max;
        for(int i = 1;i<childPoints.length;i++){
            total += childPoints[i];
            if(side > 0)
                if( max < childPoints[i])
                    max = childPoints[i];
            else
                if( max > childPoints[i])
                    max = childPoints[i];

        }
        //System.out.println("optimal for me:  " + max + "I am " + side);
        return max;
    }
}
