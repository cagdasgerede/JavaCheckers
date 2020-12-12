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

import java.io.*;
import java.util.ArrayList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.util.Optional;

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
    private int roundWithoutKick = 0;

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
        if(isComputerRound) {
            return;
        }

        checkGameEnd();

        if(isGameEnd) {
            return;
        }

        Coordinates eventCoordinates = new Coordinates((int) ((event.getX() - 37) / 85), (int) ((event.getY() - 37) / 85));

        if(isSelected) {
            if(selectedCoordinates.equals(eventCoordinates) && !newKick) {
                unLightSelect(selectedCoordinates);

                selectedCoordinates = null;
                isSelected = false;
            } else if(possibleMoves.contains(eventCoordinates)) {
                roundWithoutKick++;

                unLightSelect(selectedCoordinates);
                movePawn(selectedCoordinates, eventCoordinates);
                selectedCoordinates = null;
                isSelected = false;

                computerMove();
            } else if(possibleKick.contains(eventCoordinates) && !isFieldNotNull(eventCoordinates)) {
                roundWithoutKick = 0;

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
                if(getPawn(eventCoordinates).getColor().isWhite() && isPossiblePawn(eventCoordinates, PawnColor.WHITE)) {
                    isSelected = true;
                    selectedCoordinates = eventCoordinates;
                    lightSelect(eventCoordinates);
                }
            }
        }
    }

    public void readKeyboard(KeyEvent event) {
        if(event.getCode().equals(KeyCode.R) || event.getCode().equals(KeyCode.N)) {
            EndGame.restartApplication();
        }

        if(event.getCode().equals(KeyCode.S)) {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Save Game");
            alert.setContentText("Game will be saved as 'JavaCheckersSavedGame.txt' to selected directory(Your Home Directory or Game Directory)");
            ButtonType saveToHome = new ButtonType("Home Directory");
            ButtonType saveToLocal = new ButtonType("Game Directory");  
            alert.getButtonTypes().setAll(saveToHome, saveToLocal, ButtonType.CANCEL);
            Optional<ButtonType> result = alert.showAndWait();
            ArrayList<String> blackCoords = new ArrayList<String>();
            ArrayList<String> whiteCoords = new ArrayList<String>();
            ArrayList<String> blackQueenCoords = new ArrayList<String>();
            ArrayList<String> whiteQueenCoords = new ArrayList<String>();
            int black_count = 0;
            int white_count = 0;
            int blackqueen_count = 0;
            int whitequeen_count = 0;
            for(int x = 0; x <= 7; x++) {
                for(int y = 0; y <= 7; y++) {
                    Coordinates coord = new Coordinates(x, y);
                    if(isFieldNotNull(coord) && getPawn(coord).getColor().isWhite()){
                        whiteCoords.add(coord.getX() + " " + coord.getY());
                        white_count++;
                        if(getPawn(coord).getPawn().isQueen()){
                            whiteQueenCoords.add(coord.getX() + " " + coord.getY());
                            whitequeen_count++;
                        }    
                    }
                    else if (isFieldNotNull(coord) && getPawn(coord).getColor().isBlack()) {
                        blackCoords.add(coord.getX() + " " + coord.getY());
                        black_count++;
                        if(getPawn(coord).getPawn().isQueen()) {
                            blackQueenCoords.add(coord.getX() + " " + coord.getY());   
                            blackqueen_count++;
                        }                         
                    }
                }
            }
            if (result.get() == saveToLocal){
                try(FileWriter save = new FileWriter("JavaCheckersSavedGame.txt")) {
                    save.write(white_count + "\n"); 
                    for(int i = 0; i < whiteCoords.size(); i++)
                        save.write(whiteCoords.get(i)+"\n");
                    save.write(black_count + "\n");
                    for(int i = 0; i < blackCoords.size(); i++)
                        save.write(blackCoords.get(i)+"\n");    
                    save.write(whitequeen_count + "\n"); 
                    for(int i = 0; i < whiteQueenCoords.size(); i++)
                        save.write(whiteQueenCoords.get(i)+"\n");
                    save.write(blackqueen_count + "\n");
                    for(int i = 0; i < blackQueenCoords.size(); i++)
                        save.write(blackQueenCoords.get(i)+"\n");                        
                    save.close();
                    System.out.println("You saved game!");
                }
                catch(IOException e){
                    e.printStackTrace();
                }   
                alert.close();
            }
            else if (result.get() == saveToHome) {
                String userHomeFolder = System.getProperty("user.home");
                File file = new File(userHomeFolder, "JavaCheckersSavedGame.txt");
                try(FileWriter save = new FileWriter(file)) {
                    save.write(white_count + "\n"); 
                    for(int i = 0; i < whiteCoords.size(); i++)
                        save.write(whiteCoords.get(i)+"\n");
                    save.write(black_count + "\n");
                    for(int i = 0; i < blackCoords.size(); i++)
                        save.write(blackCoords.get(i)+"\n");    
                    save.write(whitequeen_count + "\n"); 
                    for(int i = 0; i < whiteQueenCoords.size(); i++)
                        save.write(whiteQueenCoords.get(i)+"\n");
                    save.write(blackqueen_count + "\n");
                    for(int i = 0; i < blackQueenCoords.size(); i++)
                        save.write(blackQueenCoords.get(i)+"\n");                        
                    save.close();
                    System.out.println("You saved game!");
                }
                catch(IOException e){
                    e.printStackTrace();
                }   
                alert.close();                
            }           
        }
        if(event.getCode().equals(KeyCode.L)) {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Load Game");
            alert.setContentText("Game will be loaded from selected directory if you have saved before as 'JavaCheckedSavedGame.txt'.(Your Home Directory or Game Directory)");
            ButtonType loadFromHome = new ButtonType("Home Directory");
            ButtonType loadFromLocal = new ButtonType("Game Directory");  
            alert.getButtonTypes().setAll(loadFromHome, loadFromLocal, ButtonType.CANCEL);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == loadFromLocal) {
                try(BufferedReader load = new BufferedReader(new FileReader("JavaCheckersSavedGame.txt"))) {
                    ArrayList<String> blackCoords = new ArrayList<String>();
                    ArrayList<String> whiteCoords = new ArrayList<String>();
                    ArrayList<String> blackQueenCoords = new ArrayList<String>();
                    ArrayList<String> whiteQueenCoords = new ArrayList<String>();
                    int white_count = Integer.parseInt(load.readLine());  
                    for(int i = 0; i < white_count; i++)
                        whiteCoords.add(load.readLine());
                    int black_count = Integer.parseInt(load.readLine());     
                    for(int i = 0; i < black_count; i++)
                        blackCoords.add(load.readLine());     
                    int whitequeen_count = Integer.parseInt(load.readLine());  
                    for(int i = 0; i < whitequeen_count; i++)
                        whiteQueenCoords.add(load.readLine());
                    int blackqueen_count = Integer.parseInt(load.readLine());     
                    for(int i = 0; i < blackqueen_count; i++)
                        blackQueenCoords.add(load.readLine());                        
                    load.close();          
                    for(int x = 0; x <= 7; x++) {
                        for(int y = 0; y <= 7; y++) {
                            Coordinates coord = new Coordinates(x,y);
                            Design.removePawn(coord);
                            board.remove(coord);
                        }
                    }
                    for(int i = 0; i < whiteCoords.size(); i++) {           
                        int x = Integer.parseInt(whiteCoords.get(i).charAt(0)+"");  
                        int y = Integer.parseInt(whiteCoords.get(i).charAt(2)+"");  
                        Coordinates coord = new Coordinates(x,y);
                        PawnClass pawn = new PawnClass(Pawn.PAWN, PawnColor.WHITE);
                        Design.addPawn(coord, pawn);
                        board.put(coord, pawn);
                    }
        
                    for(int i = 0; i < blackCoords.size(); i++) {           
                        int x = Integer.parseInt(blackCoords.get(i).charAt(0)+"");  
                        int y = Integer.parseInt(blackCoords.get(i).charAt(2)+"");  
                        Coordinates coord = new Coordinates(x,y);
                        PawnClass pawn = new PawnClass(Pawn.PAWN, PawnColor.BLACK);
                        Design.addPawn(coord, pawn);
                        board.put(coord, pawn);
                    }
                    for(int i = 0; i < whiteQueenCoords.size(); i++) {           
                        int x = Integer.parseInt(whiteQueenCoords.get(i).charAt(0)+"");  
                        int y = Integer.parseInt(whiteQueenCoords.get(i).charAt(2)+"");  
                        Coordinates coord = new Coordinates(x,y);
                        PawnClass pawn = new PawnClass(Pawn.QUEEN, PawnColor.WHITE);
                        Design.removePawn(coord);
                        board.remove(coord);
                        Design.addPawn(coord, pawn);
                        board.put(coord, pawn);
                    }
        
                    for(int i = 0; i < blackQueenCoords.size(); i++) {           
                        int x = Integer.parseInt(blackQueenCoords.get(i).charAt(0)+"");  
                        int y = Integer.parseInt(blackQueenCoords.get(i).charAt(2)+"");  
                        Coordinates coord = new Coordinates(x,y);
                        PawnClass pawn = new PawnClass(Pawn.QUEEN, PawnColor.BLACK);
                        Design.removePawn(coord);
                        board.remove(coord);
                        Design.addPawn(coord, pawn);
                        board.put(coord, pawn);
                    }  
                    System.out.println("You loaded a game!");                         
                }
                catch(IOException e){
                    System.out.println("There is no saved game!");
                }
                alert.close();
            }
            else if (result.get() == loadFromHome) {
                String userHomeFolder = System.getProperty("user.home");
                File file = new File(userHomeFolder, "JavaCheckersSavedGame.txt");
                try(BufferedReader load = new BufferedReader(new FileReader(file))) {
                    ArrayList<String> blackCoords = new ArrayList<String>();
                    ArrayList<String> whiteCoords = new ArrayList<String>();
                    ArrayList<String> blackQueenCoords = new ArrayList<String>();
                    ArrayList<String> whiteQueenCoords = new ArrayList<String>();
                    int white_count = Integer.parseInt(load.readLine());  
                    for(int i = 0; i < white_count; i++)
                        whiteCoords.add(load.readLine());
                    int black_count = Integer.parseInt(load.readLine());     
                    for(int i = 0; i < black_count; i++)
                        blackCoords.add(load.readLine());     
                    int whitequeen_count = Integer.parseInt(load.readLine());  
                    for(int i = 0; i < whitequeen_count; i++)
                        whiteQueenCoords.add(load.readLine());
                    int blackqueen_count = Integer.parseInt(load.readLine());     
                    for(int i = 0; i < blackqueen_count; i++)
                        blackQueenCoords.add(load.readLine());                        
                    load.close();          
                    for(int x = 0; x <= 7; x++) {
                        for(int y = 0; y <= 7; y++) {
                            Coordinates coord = new Coordinates(x,y);
                            Design.removePawn(coord);
                            board.remove(coord);
                        }
                    }
                    for(int i = 0; i < whiteCoords.size(); i++) {           
                        int x = Integer.parseInt(whiteCoords.get(i).charAt(0)+"");  
                        int y = Integer.parseInt(whiteCoords.get(i).charAt(2)+"");  
                        Coordinates coord = new Coordinates(x,y);
                        PawnClass pawn = new PawnClass(Pawn.PAWN, PawnColor.WHITE);
                        Design.addPawn(coord, pawn);
                        board.put(coord, pawn);
                    }
        
                    for(int i = 0; i < blackCoords.size(); i++) {           
                        int x = Integer.parseInt(blackCoords.get(i).charAt(0)+"");  
                        int y = Integer.parseInt(blackCoords.get(i).charAt(2)+"");  
                        Coordinates coord = new Coordinates(x,y);
                        PawnClass pawn = new PawnClass(Pawn.PAWN, PawnColor.BLACK);
                        Design.addPawn(coord, pawn);
                        board.put(coord, pawn);
                    }
                    for(int i = 0; i < whiteQueenCoords.size(); i++) {           
                        int x = Integer.parseInt(whiteQueenCoords.get(i).charAt(0)+"");  
                        int y = Integer.parseInt(whiteQueenCoords.get(i).charAt(2)+"");  
                        Coordinates coord = new Coordinates(x,y);
                        PawnClass pawn = new PawnClass(Pawn.QUEEN, PawnColor.WHITE);
                        Design.removePawn(coord);
                        board.remove(coord);
                        Design.addPawn(coord, pawn);
                        board.put(coord, pawn);
                    }
        
                    for(int i = 0; i < blackQueenCoords.size(); i++) {           
                        int x = Integer.parseInt(blackQueenCoords.get(i).charAt(0)+"");  
                        int y = Integer.parseInt(blackQueenCoords.get(i).charAt(2)+"");  
                        Coordinates coord = new Coordinates(x,y);
                        PawnClass pawn = new PawnClass(Pawn.QUEEN, PawnColor.BLACK);
                        Design.removePawn(coord);
                        board.remove(coord);
                        Design.addPawn(coord, pawn);
                        board.put(coord, pawn);
                    }  
                    System.out.println("You loaded a game!");                         
                }
                catch(IOException e){
                    System.out.println("There is no saved game!");
                }
                alert.close();
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
}
