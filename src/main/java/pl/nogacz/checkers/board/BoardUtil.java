package pl.nogacz.checkers.board;

import pl.nogacz.checkers.pawns.Pawn;
import pl.nogacz.checkers.pawns.PawnClass;
import pl.nogacz.checkers.pawns.PawnColor;
import pl.nogacz.checkers.pawns.PawnMovesUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BoardUtil {
    private HashMap<Coordinates, PawnClass> board = new HashMap<>();

    private boolean isSelected = false;
    private boolean newKick = false;
    private Coordinates selectedCoordinates;

    private Set<Coordinates> possibleMoves = new HashSet<>();
    private Set<Coordinates> possibleKick = new HashSet<>();
    private Set<Coordinates> possiblePromote = new HashSet<>();

    private boolean isGameEnd = false;
    private int roundWithoutKick = 0;

    public BoardUtil(HashMap<Coordinates, PawnClass> boardMap) {
        for(Map.Entry<Coordinates, PawnClass> entry : boardMap.entrySet()){
            PawnClass pawn = new PawnClass(Pawn.PAWN, PawnColor.WHITE);
            if(entry.getValue().getColor() == PawnColor.BLACK){
                if(entry.getValue().getPawn().isQueen()){
                    pawn = new PawnClass(Pawn.QUEEN, PawnColor.BLACK);
                }
                else{
                    pawn = new PawnClass(Pawn.PAWN, PawnColor.BLACK);
                }
            }
            else if (entry.getValue().getColor() == PawnColor.WHITE){
                if(entry.getValue().getPawn().isQueen()){
                    pawn = new PawnClass(Pawn.QUEEN, PawnColor.WHITE);
                }
                else{
                    pawn = new PawnClass(Pawn.PAWN, PawnColor.WHITE);
                }
            }
            this.board.put(new Coordinates(entry.getKey().getX(), entry.getKey().getY()), pawn);
        }
    }

    public HashMap<Coordinates, PawnClass> getBoard() {
        return board;
    }

    public void automaticMove(Coordinates newCoordinates) {

        checkGameEnd();

        if(isGameEnd) {
            return;
        }

        Coordinates eventCoordinates = newCoordinates;

    
        if(isSelected) {
            if(selectedCoordinates.equals(eventCoordinates) && !newKick) {

                selectedCoordinates = null;
                isSelected = false;
            } else if(possibleMoves.contains(eventCoordinates)) {
                roundWithoutKick++;

                movePawn(selectedCoordinates, eventCoordinates);
                selectedCoordinates = null;
                isSelected = false;

            } else if(possibleKick.contains(eventCoordinates) && !isFieldNotNull(eventCoordinates)) {
                roundWithoutKick = 0;

                if(!kickPawn(selectedCoordinates, eventCoordinates)) {
                    isSelected = false;
                    newKick = false;
                    
                } else {
                    PawnClass pawn = board.get(eventCoordinates);
                    PawnMovesUtil moves = new PawnMovesUtil(eventCoordinates, pawn, this);
                    newKick = true;
                    selectedCoordinates = eventCoordinates;
                    Iterator<Coordinates> iter = moves.getPossibleKick().iterator();
                    automaticMove(iter.next());//select a random move if there are more than one possible kicks
                }
            }
        } else if(eventCoordinates.isValid()) {
            if(isFieldNotNull(eventCoordinates)) {
                if(isPossiblePawn(eventCoordinates, PawnColor.BLACK) || isPossiblePawn(eventCoordinates, PawnColor.WHITE)) {
                    isSelected = true;
                    selectedCoordinates = eventCoordinates;
                    lightSelect(eventCoordinates);
                }
            }
        }
    }

    private boolean isPossiblePawn(Coordinates coordinates, PawnColor color) {
        Set<Coordinates> possiblePawn = new HashSet<>();

        for(Map.Entry<Coordinates, PawnClass> entry : board.entrySet()) {
            if(entry.getValue().getColor() == color) {
                PawnMovesUtil pawnMoves = new PawnMovesUtil(entry.getKey(), entry.getValue(), this);

                if(pawnMoves.getPossibleKick().size() > 0) {
                    possiblePawn.add(entry.getKey());
                }
            }
        }

        if(possiblePawn.size() == 0 || possiblePawn.contains(coordinates)) {
            return true;
        }

        return false;
    }

    private void movePawn(Coordinates oldCoordinates, Coordinates newCoordinates) {
        PawnClass pawn = getPawn(oldCoordinates);

        if(possiblePromote.contains(newCoordinates)) {
            pawn = new PawnClass(Pawn.QUEEN, pawn.getColor());
        }

        board.remove(oldCoordinates);
        board.put(newCoordinates, pawn);
    }

    private boolean kickPawn(Coordinates oldCoordinates, Coordinates newCoordinates) {
        PawnClass pawn = getPawn(oldCoordinates);

        if(possiblePromote.contains(newCoordinates)) {
            pawn = new PawnClass(Pawn.QUEEN, pawn.getColor());
        }

        Coordinates enemyCoordinates = getEnemyCoordinates(newCoordinates);

        board.remove(oldCoordinates);
        board.remove(enemyCoordinates);
        board.put(newCoordinates, pawn);
        PawnMovesUtil pawnMoves = new PawnMovesUtil(newCoordinates, pawn, this);

        if(pawnMoves.getPossibleKick().size() > 0) {
            lightNewKick(newCoordinates);
            return true;
        }

        return false;
    }

    private Coordinates getEnemyCoordinates(Coordinates coordinates) {
        Coordinates checkUpLeft = new Coordinates(coordinates.getX() - 1, coordinates.getY() - 1);

        if(possibleKick.contains(checkUpLeft)) {
            return checkUpLeft;
        }

        Coordinates checkUpRight = new Coordinates(coordinates.getX() + 1, coordinates.getY() - 1);

        if(possibleKick.contains(checkUpRight)) {
            return checkUpRight;
        }

        Coordinates checkBottomLeft = new Coordinates(coordinates.getX() - 1, coordinates.getY() + 1);

        if(possibleKick.contains(checkBottomLeft)) {
            return checkBottomLeft;
        }

        Coordinates checkBottomRight = new Coordinates(coordinates.getX() + 1, coordinates.getY() + 1);

        if(possibleKick.contains(checkBottomRight)) {
            return checkBottomRight;
        }

        return null;
    }

    private void lightSelect(Coordinates coordinates) {
        PawnMovesUtil pawnMoves = new PawnMovesUtil(coordinates, getPawn(coordinates), this);

        possibleMoves = pawnMoves.getPossibleMoves();
        possibleKick = pawnMoves.getPossibleKick();
        possiblePromote = pawnMoves.getPossiblePromote();

        if(possibleKick.size() > 0) {
            possibleMoves.clear();
        }
    }

    private void lightNewKick(Coordinates coordinates) {
        PawnMovesUtil pawnMoves = new PawnMovesUtil(coordinates, getPawn(coordinates), this);

        possibleMoves.clear();
        possibleKick = pawnMoves.getPossibleKick();
        possiblePromote = pawnMoves.getPossiblePromote();
    }

    public void checkGameEnd() {
        Set<Coordinates> possibleMovesWhite = new HashSet<>();
        Set<Coordinates> possibleMovesBlack = new HashSet<>();
        int pawnWhiteCount = 0;
        int pawnBlackCount = 0;

        for(Map.Entry<Coordinates, PawnClass> entry : board.entrySet()) {
            PawnMovesUtil moves = new PawnMovesUtil(entry.getKey(), entry.getValue(), this);

            if(entry.getValue().getColor().isBlack()) {
                pawnBlackCount++;
                possibleMovesBlack.addAll(moves.getPossibleKick());
                possibleMovesBlack.addAll(moves.getPossibleMoves());
            } else {
                pawnWhiteCount++;
                possibleMovesWhite.addAll(moves.getPossibleKick());
                possibleMovesWhite.addAll(moves.getPossibleMoves());
            }
        }

        if(roundWithoutKick == 12) {
            isGameEnd = true;
        } else if(possibleMovesWhite.size() == 0 || pawnWhiteCount <= 1) {
            isGameEnd = true;
        } else if(possibleMovesBlack.size() == 0 || pawnBlackCount <= 1) {
            isGameEnd = true;
        }
    }

    public boolean isFieldNotNull(Coordinates coordinates) {
        return getPawn(coordinates) != null;
    }

    public boolean isThisSameColor(Coordinates coordinates, PawnColor color) {
        return getPawn(coordinates).getColor() == color;
    }

    public PawnClass getPawn(Coordinates coordinates) {
        return board.get(coordinates);
    }
}
