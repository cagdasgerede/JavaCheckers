package pl.nogacz.checkers.pawns;

import pl.nogacz.checkers.board.BoardUtil;
import pl.nogacz.checkers.board.Coordinates;

import java.util.HashSet;
import java.util.Set;


public class PawnMovesUtil {
    private Coordinates coordinates;
    private PawnClass pawn;

    private Set<Coordinates> possibleMoves = new HashSet<>();
    private Set<Coordinates> possibleKick = new HashSet<>();
    private Set<Coordinates> possiblePromote = new HashSet<>();
    private Set<Coordinates> autoMoves = new HashSet<>();

    private BoardUtil board;

    private boolean isKick = false;
    private Coordinates kickedCoordinates = null;

    public PawnMovesUtil(Coordinates coordinates, PawnClass pawn, BoardUtil board) {
        this.coordinates = coordinates;
        this.pawn = pawn;
        this.board = board;
        calculateMoves();
        combine();
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

    private boolean checkCoordinates(Coordinates coordinates, boolean checkMove) {
        if(!coordinates.isValid()) {
            return false;
        }

        if(board.isFieldNotNull(coordinates)) {
            if(!board.isThisSameColor(coordinates, pawn.getColor()) && !isKick) {
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
    public void combine(){
        for(Coordinates coor : possibleKick){
            if(!board.isFieldNotNull(coor)){
                autoMoves.add(coor);
            }
        }

        if(autoMoves.size() == 0){
            for(Coordinates coor : possibleMoves){
                autoMoves.add(coor);
            }
        }
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

    public Set<Coordinates> getAutoMoves() {
        return autoMoves;
    }
}