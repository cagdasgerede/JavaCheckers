package pl.nogacz.checkers.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import jdk.nashorn.internal.parser.TokenStream;
import pl.nogacz.checkers.board.Coordinates;
import pl.nogacz.checkers.pawns.PawnClass;
import pl.nogacz.checkers.pawns.PawnMoves;
import pl.nogacz.checkers.pawns.Pawn;
import pl.nogacz.checkers.pawns.PawnColor;

public class BoardNode {

    
    private HashMap<Coordinates, PawnClass> board;
    private int turn;    //1 for white, -1 for black
    private double value;


    private BoardNode parent;
    private ArrayList<BoardNode> chlidren;
    PawnMovesForTree pawnMovesForTree;

    BoardNode(HashMap<Coordinates, PawnClass> board, int turn,  BoardNode parent){
       this.board = board;
       chlidren = new ArrayList<>();
       this.parent = parent;
       this.turn = turn;
       value = 0; 
    }
    void generateMyChildren(int side){
        //tarafa gore pawn cek ve hamlelerini alip cocuk uret
        //System.out.println("generate my children");
        HashMap<Coordinates, PawnClass> movablePawns = new HashMap<>();
        for(Map.Entry<Coordinates, PawnClass> entry : board.entrySet()){
            if(turn==1){
                if(entry.getValue().getColor().isWhite()){
                    movablePawns.put(entry.getKey(), entry.getValue());
                }      
            }else
                if(entry.getValue().getColor().isWhite()){
                    movablePawns.put(entry.getKey(), entry.getValue());
                }
        }
        ArrayList< HashMap<Coordinates, PawnClass>>  allPosibleBoards = new ArrayList<>();
        for(Map.Entry<Coordinates, PawnClass> pawn : movablePawns.entrySet()){
            pawnMovesForTree = new PawnMovesForTree(pawn.getKey(), pawn.getValue(),board);
            if(pawnMovesForTree.getPossibleKick().size()>0){
                    Iterator<Coordinates> iter =pawnMovesForTree.getPossibleKick().iterator();
                    HashMap<Coordinates, PawnClass> afterKickBoard = copyBoard(board);
                    afterKickBoard.remove(pawn.getKey());
                    Coordinates newPos = iter.next();
                    afterKickBoard.put(newPos,  coordinateContains(pawnMovesForTree.getPossiblePromote(),newPos) ? new PawnClass(Pawn.QUEEN, pawn.getValue().getColor() ): pawn.getValue() );
                    Coordinates toRemovePos = iter.next();
                    Iterator<Map.Entry<Coordinates, PawnClass>> afterKickBoIterator = afterKickBoard.entrySet().iterator();
                    for(int i = 0;i<afterKickBoard.size();i++){
                        Coordinates tempPos = afterKickBoIterator.next().getKey();
                        if(toRemovePos.equals(tempPos)){
                            afterKickBoard.remove(tempPos);
                            break;
                        }
                    }
                    allPosibleBoards.addAll(proceed(afterKickBoard, newPos, coordinateContains(pawnMovesForTree.getPossiblePromote(),newPos) ? new PawnClass(Pawn.QUEEN, pawn.getValue().getColor() ): pawn.getValue() ));

                
            }else{
                for(Coordinates c : pawnMovesForTree.getPossibleMoves()){
                    HashMap<Coordinates, PawnClass> afterMoveBoard = copyBoard(board);
                    afterMoveBoard.put(c,  coordinateContains(pawnMovesForTree.getPossiblePromote(),c) ? new PawnClass(Pawn.QUEEN, pawn.getValue().getColor() ): pawn.getValue() );
                    afterMoveBoard.remove(pawn.getKey());
                    allPosibleBoards.add(afterMoveBoard);
                }
            }
        }

        
        //Test
        int from = -1;
        for(HashMap<Coordinates, PawnClass> oneBoard : allPosibleBoards){
            int index = 0;
            from++;
            for(HashMap<Coordinates, PawnClass> oneBoard2 : allPosibleBoards){
                if(index<=from)
                    continue;
                if(areBoardsSame(oneBoard, oneBoard2)){
                    System.out.println("generated boards can not be same !!!");
                    BoardPoint.printBoard(oneBoard);
                    BoardPoint.printBoard(oneBoard2);

                }
            }
            
            
        }

        for(HashMap<Coordinates, PawnClass> oneBoard : allPosibleBoards){
            
            BoardNode newNode = new BoardNode(oneBoard, side *-1, this);
            this.addChild(newNode);
            newNode.setParent(this);
        }
            
        //cekmem gereken piyonlari aldim, simdi tek tek cekip 

    }
     boolean areBoardsSame(HashMap<Coordinates, PawnClass> oneBoard , HashMap<Coordinates, PawnClass> oneBoard2 ){//test
        if(oneBoard.size()!=oneBoard2.size())
            return false;
        int from = -1;

        for(Map.Entry<Coordinates, PawnClass> entry1 : oneBoard.entrySet()){
            int innerIndex = 0;
            boolean contains = false;
            for(Map.Entry<Coordinates, PawnClass> entry2 : oneBoard2.entrySet()){
                innerIndex++;
                if(innerIndex<=from+1)
                    continue;
                if(entry1.getKey().equals(entry2.getKey())) 
                if(entry1.getValue().getColor() == entry2.getValue().getColor())
                if(entry1.getValue().getPawn() == entry2.getValue().getPawn())
                    contains = true;
            }
            if(!contains)
                return false;
            from++;
            
        }
        return  true;
    }
     HashMap<Coordinates, PawnClass> copyBoard(HashMap<Coordinates, PawnClass> board){//test
        HashMap<Coordinates, PawnClass> newBoard = new HashMap<>();
        for(Map.Entry<Coordinates, PawnClass> entry : board.entrySet()){
            newBoard.put(new Coordinates(entry.getKey().getX(),entry.getKey().getY()), new PawnClass(entry.getValue().getPawn(), entry.getValue().getColor()));
        }
        return newBoard;
    }
    private ArrayList<HashMap<Coordinates, PawnClass>> proceed(HashMap<Coordinates, PawnClass> board, Coordinates pawnToMove, PawnClass pawn ){
        PawnMovesForTree moveGenerator = new PawnMovesForTree(pawnToMove, pawn, board);
        Set<Coordinates> possibleKicks = moveGenerator.getPossibleKick();
        Iterator<Coordinates> kickIterator = possibleKicks.iterator();
        ArrayList<HashMap<Coordinates, PawnClass>> toReturn = new ArrayList<>();
        
        if(moveGenerator.getPossibleKick().size()==0){
            
            toReturn.add(board);
            return toReturn;
        }
        
        for(int i = 0;i<possibleKicks.size();i+=2){
                HashMap<Coordinates, PawnClass> tempSet = copyBoard(board);
                tempSet.remove(pawnToMove);
                Coordinates newPos = kickIterator.next();
                Coordinates toRemove = kickIterator.next();

                int newPosDis = pawnToMove.getX() - newPos.getX();
                if(newPosDis < 0)
                    newPosDis*=-1;
                
                int remPosDis = pawnToMove.getX() - toRemove.getX();
                if(remPosDis < 0)
                    remPosDis*=-1;

                if(remPosDis>newPosDis){
                    Coordinates tmp = newPos;
                    newPos = toRemove;
                    toRemove = tmp;
                }

                PawnClass tmp = tempSet.remove(toRemove);
                
                tempSet.put(newPos, coordinateContains(moveGenerator.getPossiblePromote(),newPos) ? new PawnClass(Pawn.QUEEN, pawn.getColor() ): pawn);

                toReturn.addAll(proceed(tempSet, newPos, coordinateContains(moveGenerator.getPossiblePromote(),newPos) ? new PawnClass(Pawn.QUEEN, pawn.getColor() ): pawn));
            
        }
        return toReturn;
    }
    public void addChild(BoardNode e){
        chlidren.add(e);
    }
     boolean coordinateContains(Set<Coordinates> base, Coordinates query )//test
    {
        for(Coordinates  c : base)
            if(c.equals(query))
            return true;
        return false;
    }public HashMap<Coordinates,PawnClass> getBoard() {
        return this.board;
    }

    public void setBoard(HashMap<Coordinates,PawnClass> board) {
        this.board = board;
    }

    public int getTurn() {
        return this.turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public BoardNode getParent() {
        return this.parent;
    }

    public void setParent(BoardNode parent) {
        this.parent = parent;
    }

    public ArrayList<BoardNode> getChlidren() {
        return this.chlidren;
    }

    public void setChlidren(ArrayList<BoardNode> chlidren) {
        this.chlidren = chlidren;
    }
}

class PawnMovesForTree{
    HashMap<Coordinates, PawnClass> board;

    private Coordinates coordinates;
    private PawnClass pawn;

    private Set<Coordinates> possibleMoves = new HashSet<>();
     private Set<Coordinates> possibleKick = new HashSet<>();
     private Set<Coordinates> possiblePromote = new HashSet<>();

     private boolean isKick = false;
     private Coordinates kickedCoordinates = null;


    public PawnMovesForTree(Coordinates coordinates, PawnClass pawn1, HashMap<Coordinates, PawnClass> b ) {
        //System.out.println("yeni bir pawnmoves for tree olusturuldu");
        this.coordinates = coordinates;
        this.pawn = pawn1;

        this.board = b;
        this.calculateMoves();
    }
    private void calculateMoves() {
        if(pawn.getPawn().isPawn()) {
            if(pawn.getColor().isBlack()) {
                checkBottomLeft(true);
                checkBottomRight(true);
                checkUpLeft(false);
                checkUpRight(false);
            } else {
                checkUpLeft(true);
                checkUpRight(true);
                checkBottomLeft(false);
                checkBottomRight(false);
            }
        } else {
            checkUpLeft(true);
            checkUpRight(true);
            checkBottomLeft(true);
            checkBottomRight(true);
        }
    }

    private void checkUpLeft(boolean checkMove) {
        boolean checkUpLeft = true;
        isKick = false;

        for(int i = 1; i < 8; i++) {
            if(checkUpLeft) {
                checkUpLeft = checkCoordinates(new Coordinates(coordinates.getX() - i, coordinates.getY() - i), checkMove);
            }
        }
    }

    private void checkUpRight(boolean checkMove) {
        boolean checkUpRight = true;
        isKick = false;

        for(int i = 1; i < 8; i++) {
            if(checkUpRight) {
                checkUpRight = checkCoordinates(new Coordinates(coordinates.getX() + i, coordinates.getY() - i), checkMove);
            }
        }
    }

    private void checkBottomLeft(boolean checkMove) {
        boolean checkBottomLeft = true;
        isKick = false;

        for(int i = 1; i < 8; i++) {
            if(checkBottomLeft) {
                checkBottomLeft = checkCoordinates(new Coordinates(coordinates.getX() - i, coordinates.getY() + i), checkMove);
            }
        }
    }

    private void checkBottomRight(boolean checkMove) {
        boolean checkBottomRight = true;
        isKick = false;

        for(int i = 1; i < 8; i++) {
            if(checkBottomRight) {
                checkBottomRight = checkCoordinates(new Coordinates(coordinates.getX() + i, coordinates.getY() + i), checkMove);
            }
        }
    }

     boolean checkCoordinates(Coordinates coordinates, boolean checkMove) {
        if(!coordinates.isValid()) {
            return false;
        }

        Map.Entry<Coordinates, PawnClass> boardEntry = null;
        Iterator <Map.Entry<Coordinates, PawnClass>> boardEntryIterator = board.entrySet().iterator();
        while(boardEntryIterator.hasNext()){
            boardEntry = boardEntryIterator.next();
            if(boardEntry.getKey().equals(coordinates))
                break;
        }


        if(boardEntry.getKey().equals(coordinates)) {
            if(board.get(boardEntry.getKey()).getColor()!=pawn.getColor() && !isKick) {
                kickedCoordinates = coordinates;
                isKick = true;
                return true;
            }
        } else {
            if((pawn.getColor().isWhite() && coordinates.getY() == 0 || pawn.getColor().isBlack() && coordinates.getY() == 7) && pawn.getPawn().isPawn()) {
                possiblePromote.add(coordinates);
            }

            if(isKick) {
                isKick = false;

                possibleKick.add(coordinates);
                possibleKick.add(kickedCoordinates);
            } else if(checkMove) {
                possibleMoves.add(coordinates);

                if(pawn.getPawn().isQueen()) {
                    return true;
                }
            }
        }

        return false;
    }
    public Set<Coordinates> getPossibleMoves() {
        return possibleMoves;
    }

    public Set<Coordinates> getPossibleKick() {
        return possibleKick;
    }

    public Set<Coordinates> getPossiblePromote() {
        return possiblePromote;
    }
    
}
