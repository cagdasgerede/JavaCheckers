package pl.nogacz.checkers.board;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import pl.nogacz.checkers.application.Design;
import pl.nogacz.checkers.application.computer.Computer;
import pl.nogacz.checkers.pawns.Pawn;
import pl.nogacz.checkers.pawns.PawnClass;
import pl.nogacz.checkers.pawns.PawnColor;
import pl.nogacz.checkers.pawns.PawnMoves;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Dawid Nogacz on 17.05.2019
 */
public class Board {
    private static HashMap<Coordinates, PawnClass> board = new HashMap<>();

    private boolean isSelected = false;
    private boolean newKick = false;
    private Coordinates selectedCoordinates;

    private Set<Coordinates> possibleMoves = new HashSet<>();
    private Set<Coordinates> possibleKick = new HashSet<>();
    private Set<Coordinates> possiblePromote = new HashSet<>();

    private boolean isGameEnd = false;

    private boolean isComputerRound = false;
    private Computer computer = new Computer();

    public Board() {
        addStartPawn();
    }

    public static HashMap<Coordinates, PawnClass> getBoard() {
        return board;
    }

    private void addStartPawn() {
        board.put(new Coordinates(1, 0), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(3, 0), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(5, 0), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(7, 0), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(0, 1), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(2, 1), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(4, 1), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(6, 1), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(1, 2), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(3, 2), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(5, 2), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(7, 2), new PawnClass(Pawn.PAWN, PawnColor.BLACK));

        board.put(new Coordinates(0, 5), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(2, 5), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(4, 5), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(6, 5), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(1, 6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(3, 6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(5, 6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(7, 6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(0, 7), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(2, 7), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(4, 7), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(6, 7), new PawnClass(Pawn.PAWN, PawnColor.WHITE));

        for(Map.Entry<Coordinates, PawnClass> entry : board.entrySet()) {
            Design.addPawn(entry.getKey(), entry.getValue());
        }
    }

    public void readMouseEvent(MouseEvent event) {
        if(isGameEnd || isComputerRound) {
            return;
        }

        Coordinates eventCoordinates = new Coordinates((int) ((event.getX() - 37) / 85), (int) ((event.getY() - 37) / 85));

        if(isSelected) {
            if(selectedCoordinates.equals(eventCoordinates) && !newKick) {
                unLightSelect(selectedCoordinates);

                selectedCoordinates = null;
                isSelected = false;
            } else if(possibleMoves.contains(eventCoordinates)) {
                unLightSelect(selectedCoordinates);
                movePawn(selectedCoordinates, eventCoordinates);
                selectedCoordinates = null;
                isSelected = false;

                computerMove();
            } else if(possibleKick.contains(eventCoordinates) && !isFieldNotNull(eventCoordinates)) {
                unLightSelect(selectedCoordinates);

                if(!kickPawn(selectedCoordinates, eventCoordinates)) {
                    isSelected = false;
                    newKick = false;
                    computerMove();
                } else {
                    newKick = true;
                    selectedCoordinates = eventCoordinates;
                }
            }
        } else if(eventCoordinates.isValid()) {
            if(isFieldNotNull(eventCoordinates)) {
                if(/*getPawn(eventCoordinates).getColor().isWhite() &&*/ isPossiblePawn(eventCoordinates, PawnColor.WHITE)) {
                    isSelected = true;
                    selectedCoordinates = eventCoordinates;
                    lightSelect(eventCoordinates);
                }
            }
        }
    }

    public void readKeyboard(KeyEvent event) {
        if(event.getCode().equals(KeyCode.R) || event.getCode().equals(KeyCode.N)) {
            //TODO Add keyboard
        }
    }

    private void computerMove() {
        //TODO Add computer player
    }

    private boolean isPossiblePawn(Coordinates coordinates, PawnColor color) {
        Set<Coordinates> possiblePawn = new HashSet<>();

        for(Map.Entry<Coordinates, PawnClass> entry : board.entrySet()) {
            if(entry.getValue().getColor() == color) {
                PawnMoves pawnMoves = new PawnMoves(entry.getKey(), entry.getValue());

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

        Design.removePawn(oldCoordinates);
        Design.removePawn(newCoordinates);
        Design.addPawn(newCoordinates, pawn);

        board.remove(oldCoordinates);
        board.put(newCoordinates, pawn);
    }

    private boolean kickPawn(Coordinates oldCoordinates, Coordinates newCoordinates) {
        PawnClass pawn = getPawn(oldCoordinates);

        if(possiblePromote.contains(newCoordinates)) {
            pawn = new PawnClass(Pawn.QUEEN, pawn.getColor());
        }

        Coordinates enemyCoordinates = getEnemyCoordinates(newCoordinates);

        Design.removePawn(oldCoordinates);
        Design.removePawn(enemyCoordinates);
        Design.addPawn(newCoordinates, pawn);

        board.remove(oldCoordinates);
        board.remove(enemyCoordinates);
        board.put(newCoordinates, pawn);

        PawnMoves pawnMoves = new PawnMoves(newCoordinates, pawn);

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
        PawnMoves pawnMoves = new PawnMoves(coordinates, getPawn(coordinates));

        possibleMoves = pawnMoves.getPossibleMoves();
        possibleKick = pawnMoves.getPossibleKick();
        possiblePromote = pawnMoves.getPossiblePromote();

        if(possibleKick.size() > 0) {
            possibleMoves.clear();
        }

        possibleMoves.forEach(this::lightMove);
        possibleKick.forEach(this::lightKick);

        lightPawn(coordinates);
    }

    private void lightNewKick(Coordinates coordinates) {
        PawnMoves pawnMoves = new PawnMoves(coordinates, getPawn(coordinates));

        possibleMoves.clear();
        possibleKick = pawnMoves.getPossibleKick();
        possiblePromote = pawnMoves.getPossiblePromote();

        possibleKick.forEach(this::lightKick);

        lightPawn(coordinates);
    }

    private void lightPawn(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);
        Design.removePawn(coordinates);
        Design.addLightPawn(coordinates, pawn);
    }

    private void lightMove(Coordinates coordinates) {
        Design.addLightMove(coordinates);
    }

    private void lightKick(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);

        if(pawn == null) {
            lightMove(coordinates);
        }
    }

    private void unLightSelect(Coordinates coordinates) {
        possibleMoves.forEach(this::unLightMove);
        possibleKick.forEach(this::unLightKick);

        unLightPawn(coordinates);
    }

    private void unLightPawn(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);
        Design.removePawn(coordinates);
        Design.addPawn(coordinates, pawn);
    }

    private void unLightMove(Coordinates coordinates) {
        Design.removePawn(coordinates);
    }

    private void unLightKick(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);

        if(pawn != null) {
            unLightPawn(coordinates);
        } else {
            unLightMove(coordinates);
        }
    }

    public static PawnClass addPawnWithoutDesign(Coordinates coordinates, PawnClass pawn) {
        PawnClass oldPawn = null;

        if(isFieldNotNull(coordinates)) {
            oldPawn = getPawn(coordinates);
            board.remove(coordinates);
        }

        board.put(coordinates, pawn);
        return oldPawn;
    }

    public static void removePawnWithoutDesign(Coordinates coordinates) {
        board.remove(coordinates);
    }

    public static boolean isFieldNotNull(Coordinates coordinates) {
        return getPawn(coordinates) != null;
    }

    public static boolean isThisSameColor(Coordinates coordinates, PawnColor color) {
        return getPawn(coordinates).getColor() == color;
    }

    public static PawnClass getPawn(Coordinates coordinates) {
        return board.get(coordinates);
    }
}
