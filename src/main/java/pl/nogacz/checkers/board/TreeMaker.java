package pl.nogacz.checkers.board;

import pl.nogacz.checkers.pawns.PawnClass;
import pl.nogacz.checkers.pawns.PawnColor;
import pl.nogacz.checkers.pawns.PawnMovesUtil;
import java.util.HashMap;
import java.util.Map;

public class TreeMaker {

    public BoardTree construcTree(boolean isComputerRound, HashMap<Coordinates, PawnClass> mainBoard){
        if(isComputerRound){
            return construcTreeUtil(PawnColor.BLACK, isComputerRound, mainBoard);
        } else{
            return construcTreeUtil(PawnColor.WHITE, isComputerRound, mainBoard);
        }
    }

    public BoardTree construcTreeUtil(PawnColor color, boolean isComputerRound, HashMap<Coordinates, PawnClass> mainBoard){
        //level 0 - > current board
        BoardUtil currBoard = createBoard(mainBoard);
        BoardTree tree_level0 = new BoardTree(currBoard, null, null, relativeScore(mainBoard, isComputerRound));
        
        for(Map.Entry<Coordinates, PawnClass> entry : currBoard.getBoard().entrySet()){
            if(!currBoard.isThisSameColor(new Coordinates(entry.getKey().getX(), entry.getKey().getY()), color)){
                continue;
            }
            PawnMovesUtil moves = new PawnMovesUtil(entry.getKey(), entry.getValue(), createBoard(mainBoard));
            for(Coordinates move : moves.getAutoMoves()){
                //level 1 - > first possible moves
                BoardUtil board_level1 = createBoard(mainBoard);
                Coordinates oldCoordinates = new Coordinates(entry.getKey().getX(), entry.getKey().getY());
                board_level1.automaticMove(oldCoordinates);//select the pawn
                board_level1.automaticMove(move);//make the move
                BoardTree tree_level1 = new BoardTree(board_level1, oldCoordinates, move, relativeScore(board_level1.getBoard(), isComputerRound));
                
                for(Map.Entry<Coordinates, PawnClass> entry_level1 : board_level1.getBoard().entrySet()){
                    if(board_level1.isThisSameColor(new Coordinates(entry_level1.getKey().getX(), entry_level1.getKey().getY()), color)){//enemy move
                        continue;
                    }
                    PawnMovesUtil moves2 = new PawnMovesUtil(entry_level1.getKey(), entry_level1.getValue(), board_level1);
                    for(Coordinates move2 : moves2.getAutoMoves()){
                        //level 2 - > second possible moves
                        BoardUtil board_level2 = createBoard(board_level1.getBoard());
                        Coordinates oldCoordinates2 = new Coordinates(entry_level1.getKey().getX(), entry_level1.getKey().getY());
                        board_level2.automaticMove(oldCoordinates2);
                        board_level2.automaticMove(move2);
                        BoardTree tree_level2 = new BoardTree(board_level2, oldCoordinates2, move2, relativeScore(board_level2.getBoard(), isComputerRound));

                        for(Map.Entry<Coordinates, PawnClass> entry_level2 : board_level2.getBoard().entrySet()){
                            if(!board_level2.isThisSameColor(new Coordinates(entry_level2.getKey().getX(), entry_level2.getKey().getY()), color)){
                                continue;
                            }
                            PawnMovesUtil moves3 = new PawnMovesUtil(entry_level2.getKey(), entry_level2.getValue(), board_level2);
                            for(Coordinates move3 : moves3.getAutoMoves()){
                                //level 3 - > third possible moves
                                BoardUtil board_level3 = createBoard(board_level2.getBoard());
                                Coordinates oldCoordinates3 = new Coordinates(entry_level2.getKey().getX(), entry_level2.getKey().getY());
                                board_level3.automaticMove(oldCoordinates3);
                                board_level3.automaticMove(move3);
                                BoardTree tree_level3 = new BoardTree(board_level3, oldCoordinates3, move3, relativeScore(board_level3.getBoard(), isComputerRound));

                                tree_level2.addChild(tree_level3);
                            }
                        }
                        tree_level1.addChild(tree_level2);
                    }
                }
                tree_level0.addChild(tree_level1);
            }     
        }
        
        return tree_level0;
    }

    public int relativeScore(HashMap<Coordinates, PawnClass> board, boolean isComputerRound){
        int computerScore = pcScore(board);
        int playerScore = playerScore(board);

        if(isComputerRound){
            return computerScore-playerScore;
        }
        return playerScore-computerScore;
    }
    
    public int pcScore(HashMap<Coordinates, PawnClass> board){
        int score = 0;
        for(Map.Entry<Coordinates, PawnClass> entry : board.entrySet()) {
            if(entry.getValue().getColor() == PawnColor.BLACK){
                if(entry.getValue().getPawn().isQueen()){
                    score += 3;
                }
                else{
                    score++;
                }
            }
        }
        return score;
    }

    public int playerScore(HashMap<Coordinates, PawnClass> board){
        int score = 0;
        for(Map.Entry<Coordinates, PawnClass> entry : board.entrySet()) {
            if(entry.getValue().getColor() == PawnColor.WHITE){
                if(entry.getValue().getPawn().isQueen()){
                    score += 3;
                }
                else{
                    score++;
                }
            }
        }
        return score;
    }

    public BoardUtil createBoard(HashMap<Coordinates, PawnClass> board){
        BoardUtil newBoard = new BoardUtil(board);
        return newBoard;
    }

}
