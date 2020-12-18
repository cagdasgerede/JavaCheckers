package pl.nogacz.checkers.board;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import pl.nogacz.checkers.application.Design;
import pl.nogacz.checkers.application.Computer;
import pl.nogacz.checkers.application.EndGame;
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
public class Board{
    private static HashMap<Coordinates, PawnClass> board = new HashMap<>();

    private static boolean isPawnSelected = false;
    private boolean newKick = false;
    private static boolean isEditMenuActive = false;
    private static Coordinates selectedCoordinates;
    private Set<Coordinates> possibleMoves = new HashSet<>();
    private Set<Coordinates> possibleKick = new HashSet<>();
    private Set<Coordinates> possiblePromote = new HashSet<>();

    private boolean isGameEnd = false;
    private int roundWithoutKick = 0;

    private boolean isComputerRound = false;
    private Computer computer = new Computer();

    private EditMenu editMenu;
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
        if(!isEditMenuActive){
            if(isComputerRound) {
                return;
            }
            checkGameEnd();

            if(isGameEnd) {
                return;
            }
            Coordinates eventCoordinates = new Coordinates((int) ((event.getX() - 37) / 85), (int) ((event.getY() - 37) / 85));

            if(isPawnSelected) {
                if(selectedCoordinates.equals(eventCoordinates) && !newKick) {
                    unLightSelect(selectedCoordinates);
                    selectedCoordinates = null;
                    isPawnSelected = false;
                } else if(possibleMoves.contains(eventCoordinates)) {
                    roundWithoutKick++;
                    unLightSelect(selectedCoordinates);
                    movePawn(selectedCoordinates, eventCoordinates);
                    selectedCoordinates = null;
                    isPawnSelected = false;

                    computerMove();
                } else if(possibleKick.contains(eventCoordinates) && !isFieldNotNull(eventCoordinates)) {
                    roundWithoutKick = 0;

                    unLightSelect(selectedCoordinates);

                    if(!kickPawn(selectedCoordinates, eventCoordinates)) {
                        isPawnSelected = false;
                        newKick = false;
                        computerMove();
                    } else {
                        newKick = true;
                        selectedCoordinates = eventCoordinates;
                    }
                }
            } else if(eventCoordinates.isValid()) {
                if(isFieldNotNull(eventCoordinates)) {
                    if(getPawn(eventCoordinates).getColor().isWhite() && isPossiblePawn(eventCoordinates, PawnColor.WHITE)) {
                        isPawnSelected = true;
                        selectedCoordinates = eventCoordinates;
                        lightSelect(eventCoordinates);
                    }
                }
                editMenu = null;
            }
        }else {
            Coordinates eventCoordinates = new Coordinates((int) ((event.getX() - 37) / 85), (int) ((event.getY() - 37) / 85));
            runEditMenuFunctions(eventCoordinates);
        }
    }

    public void readKeyboard(KeyEvent event) {
        if(event.getCode().equals(KeyCode.R)){
            EndGame.restartApplication();
        }
        if(event.getCode().equals(KeyCode.TAB)){
            if(editMenu== null){
                isEditMenuActive = true;
                editMenu = new EditMenu();
            }
        }
    }

    private void computerMove() {
        checkGameEnd();

        if(isGameEnd) {
            return;
        }
        Task<Void> computerSleep = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                return null;
            }
        };

        computerSleep.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Coordinates moveCoordinates = computer.chooseMove(selectedCoordinates);
                unLightSelect(selectedCoordinates);
                if(computer.isKickedMove()) {
                    if(!kickPawn(selectedCoordinates, moveCoordinates)) {
                        newKick = false;
                        isComputerRound = false;
                        selectedCoordinates = null;
                    } else {
                        newKick = true;
                        selectedCoordinates = moveCoordinates;
                        computerMove();
                    }
                } else {
                    movePawn(selectedCoordinates, moveCoordinates);
                    isComputerRound = false;
                    selectedCoordinates = null;
                }
            }
        });

        isComputerRound = true;
        computer.getGameData();

        if(!newKick) {
            selectedCoordinates = computer.choosePawn();
        }

        lightSelect(selectedCoordinates);

        new Thread(computerSleep).start();
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
    private void editLightSelect(Coordinates coordinates) {
        editLightPawn(coordinates);
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
    private void editLightPawn(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);
        Design.removePawn(coordinates);
        Design.addEditLightPawn(coordinates,pawn);
    }

    private void lightMove(Coordinates coordinates) {
        Design.addLightMove(coordinates);
    }

    private void lightReplace(Coordinates coordinates) {
        Design.addLightReplace(coordinates);
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
    private void unLightEditSelect(Coordinates coordinates) {
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

    public void checkGameEnd() {
        Set<Coordinates> possibleMovesWhite = new HashSet<>();
        Set<Coordinates> possibleMovesBlack = new HashSet<>();
        int pawnWhiteCount = 0;
        int pawnBlackCount = 0;

        for(Map.Entry<Coordinates, PawnClass> entry : board.entrySet()) {
            PawnMoves moves = new PawnMoves(entry.getKey(), entry.getValue());

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
            new EndGame("Draw. Maybe you try again?");
        } else if(possibleMovesWhite.size() == 0 || pawnWhiteCount <= 1) {
            isGameEnd = true;
            new EndGame("You loss. Maybe you try again?");
        } else if(possibleMovesBlack.size() == 0 || pawnBlackCount <= 1) {
            isGameEnd = true;
            new EndGame("You win! Congratulations! :)");
        }
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

    public static void setEditMenuActive(boolean value){
        isEditMenuActive = value;
    }
    public static void setIsPawnSelected(boolean value){
        isPawnSelected = value;
    }

    public static Coordinates getSelectedCoordinates(){
        return selectedCoordinates;
    }
    public static void setSelectedCoordinates(Coordinates coordinates){
        selectedCoordinates = coordinates;
    }


    private void runEditMenuFunctions(Coordinates eventCoordinates) {
        if(isPawnSelected) {
            if(eventCoordinates.getY() == selectedCoordinates.getY() && eventCoordinates.getX() == selectedCoordinates.getX()
                    || (eventCoordinates.getX() == editMenu.getAddPawnCoordinates().getX() && eventCoordinates.getY() ==  editMenu.getAddPawnCoordinates().getY())){
                if(isFieldNotNull(eventCoordinates)) {
                    editMenu.forEachUnlightPossibleReplace();
                    editMenu.forEachUnlightAddPawn();
                    unLightEditSelect(eventCoordinates);
                    editMenu.getPossibleReplaces().clear();
                    editMenu.getPossibleAddPawns().clear();
                }
                else {
                    editMenu.forEachUnlightPossibleReplace();
                    editMenu.forEachUnlightAddPawn();
                    editMenu.unLightReplace(eventCoordinates);
                    editMenu.getPossibleReplaces().clear();
                    editMenu.getPossibleAddPawns().clear();
                }
                isPawnSelected = false;
            }
            else if(!isFieldNotNull(eventCoordinates)){
                if(eventCoordinates.isValid()) {
                    editMenu.forEachUnlightPossibleReplace();
                    editMenu.forEachUnlightAddPawn();
                    lightReplace(eventCoordinates);
                    editMenu.getPossibleReplaces().clear();
                    editMenu.getPossibleAddPawns().clear();
                    editMenu.getPossibleReplaces().add(eventCoordinates);
                    editMenu.getPossibleAddPawns().add(eventCoordinates);
                    editMenu.setAddPawnCoordinates(eventCoordinates);
                }
            }
        }
        else if(!isFieldNotNull(eventCoordinates) && eventCoordinates.isValid()){
            editMenu.forEachUnlightPossibleReplace();
            isPawnSelected = true;
            editMenu.getPossibleReplaces().clear();
            editMenu.getPossibleAddPawns().clear();
            editMenu.setAddPawnCoordinates(eventCoordinates);
            selectedCoordinates = eventCoordinates;;
            editMenu.getPossibleAddPawns().add(eventCoordinates);
            lightReplace(eventCoordinates);
        }
        else if(isFieldNotNull(eventCoordinates) && eventCoordinates.isValid()) {
            editMenu.forEachUnlightPossibleReplace();
            isPawnSelected = true;
            editMenu.setAddPawnCoordinates(eventCoordinates);
            selectedCoordinates = eventCoordinates;
            editLightSelect(eventCoordinates);
        }
    }

    public static boolean getIsEditMenuActive() {
        return isEditMenuActive;
    }

}
