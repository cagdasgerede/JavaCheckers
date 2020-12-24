package pl.nogacz.checkers.application;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import pl.nogacz.checkers.Checkers;
import pl.nogacz.checkers.application.Design;
import pl.nogacz.checkers.board.Board;
import pl.nogacz.checkers.board.Coordinates;
import pl.nogacz.checkers.pawns.Pawn;
import pl.nogacz.checkers.pawns.PawnClass;
import pl.nogacz.checkers.pawns.PawnColor;
import pl.nogacz.checkers.pawns.PawnMoves;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SaveLoad {

    public enum Process {
        AUTO,
        HOME,
        LOCAL;

        public boolean isHome() {
            return this == HOME;
        }
    
        public boolean isLocal() {
            return this == LOCAL;
        }
    }

    private static final boolean BOO = true; 
    private static final int SIZE = 7;
    private static final String AUTO_SAVED_FILE = "JavaCheckersAutosavedGame.txt";
    private static final String BUTTON_AUTO = "AutoSave";
    private static final String BUTTON_HOME = "Home Directory";
    private static final String BUTTON_LOCAL = "Game Directory";
    private static final String ERROR = "File not found";
    private static final String HOME = "user.home";
    private static final String LOAD_TEXT = "Game will be loaded from selected directory if you have saved before as 'JavaCheckedSavedGame.txt'.(Your Home Directory or Game Directory or Autosave)";
    private static final String LOAD_TITLE = "Load Game";
    private static final String LOG_FILE = "log.txt";
    private static final String SAVE_TEXT = "Game will be saved as 'JavaCheckersSavedGame.txt' to selected directory(Your Home Directory or Game Directory)";
    private static final String SAVE_TITLE = "Save Game";
    private static final String SAVED_FILE = "JavaCheckersSavedGame.txt";
    private static HashMap<Coordinates, PawnClass> board = Board.getBoard();

    public static void autoSave() {        
        saveFile(Process.AUTO);
    }

    public static void saveFile(Process p) {

        File file;
        if(p.isHome()) {
            String userHomeFolder = System.getProperty(HOME);
            file = new File(userHomeFolder, SAVED_FILE);
        }
        else if(p.isLocal()){
            file = new File(SAVED_FILE);
        }
        else {
            file = new File(AUTO_SAVED_FILE);
        }

        ArrayList<String> blackCoords = new ArrayList<>(),
        whiteCoords = new ArrayList<>(),
        blackQueenCoords = new ArrayList<>(),
        whiteQueenCoords = new ArrayList<>();
        int black_count = 0,
        white_count = 0,
        blackqueen_count = 0,
        whitequeen_count = 0;

        for(int x = 0; x <= SIZE; x++) {
            
            for(int y = 0; y <= SIZE; y++) {

                Coordinates coord = new Coordinates(x, y);

                if(Board.isFieldNotNull(coord) && Board.getPawn(coord).getColor().isWhite()){
                    whiteCoords.add(coord.getX() + " " + coord.getY());
                    white_count++;
                    if(Board.getPawn(coord).getPawn().isQueen()){
                        whiteQueenCoords.add(coord.getX() + " " + coord.getY());
                        whitequeen_count++;
                    }    
                }
                else if (Board.isFieldNotNull(coord) && Board.getPawn(coord).getColor().isBlack()) {
                    blackCoords.add(coord.getX() + " " + coord.getY());
                    black_count++;
                    if(Board.getPawn(coord).getPawn().isQueen()) {
                        blackQueenCoords.add(coord.getX() + " " + coord.getY());   
                        blackqueen_count++;
                    }                         
                }
            }
        }

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
        }
        catch(IOException e){
            try(FileWriter log = new FileWriter(LOG_FILE)) {
                log.write(ERROR);
            }
            catch(IOException ex){
                ex.printStackTrace();
            } 
        }    
    }

    public static void saveGame() {

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(SAVE_TITLE);
        alert.setContentText(SAVE_TEXT);
        ButtonType saveToHome = new ButtonType(BUTTON_HOME);
        ButtonType saveToLocal = new ButtonType(BUTTON_LOCAL);  
        alert.getButtonTypes().setAll(saveToHome, saveToLocal, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == saveToHome)
            saveFile(Process.HOME); 
        if (result.get() == saveToLocal)
            saveFile(Process.LOCAL);         

        alert.close();                 
    }

    public static boolean addPawn(ArrayList<String> list, HashMap<Coordinates, PawnClass> board, int a, int b) {
        boolean boo = BOO;
        if(list.size() == 0) {
            boo = false;
        }
        else {
            for(int i = 0; i < list.size(); i++) {          

                int x = Integer.parseInt(list.get(i).charAt(0)+"");  
                int y = Integer.parseInt(list.get(i).charAt(2)+"");  
                Coordinates coord = new Coordinates(x,y);
    
                if (a==0 && b == 0) {
                    PawnClass pawn = new PawnClass(Pawn.PAWN, PawnColor.WHITE);
                    Design.addPawn(coord, pawn);
                    board.put(coord, pawn);
                }    
                else if (a == 0 && b == 1) {
                    PawnClass pawn = new PawnClass(Pawn.PAWN, PawnColor.BLACK);
                    Design.addPawn(coord, pawn);
                    board.put(coord, pawn);
                }
                else if (a == 1 && b == 1) {
                    PawnClass pawn = new PawnClass(Pawn.QUEEN, PawnColor.BLACK);
                    Design.addPawn(coord, pawn);
                    board.put(coord, pawn);
                }
                else if (a == 1 && b == 0) {
                    PawnClass pawn = new PawnClass(Pawn.QUEEN, PawnColor.WHITE);
                    Design.addPawn(coord, pawn);
                    board.put(coord, pawn);
                }
                else {
                    boo = false;
                }
            } 
        }  
        return boo;    
    }

    public static void loadFile(Process p) {

        File file;

        if(p.isHome()) {
            String userHomeFolder = System.getProperty(HOME);
            file = new File(userHomeFolder, SAVED_FILE);
        }
        else if(p.isLocal()){
            file = new File(SAVED_FILE);
        }
        else {
            file = new File(AUTO_SAVED_FILE);
        }

        try(BufferedReader load = new BufferedReader(new FileReader(file))) {

            ArrayList<String> blackCoords = new ArrayList<>(),
            whiteCoords = new ArrayList<>(),
            blackQueenCoords = new ArrayList<>(),
            whiteQueenCoords = new ArrayList<>();
            parseCoords(load, whiteCoords);
            parseCoords(load, blackCoords);
            parseCoords(load, whiteQueenCoords);
            parseCoords(load, blackQueenCoords);                      
            load.close();  
            
            for(int x = 0; x <= SIZE; x++) {
                for(int y = 0; y <= SIZE; y++) {
                    Coordinates coord = new Coordinates(x,y);
                    Design.removePawn(coord);
                    board.remove(coord);
                }
            }
            
            addPawn(whiteCoords, board, 0, 0);
            addPawn(blackCoords, board, 0, 1);
            addPawn(whiteQueenCoords, board, 1, 0);
            addPawn(blackQueenCoords, board, 1, 1);
                   
        }
        catch(IOException e){
            try(FileWriter log = new FileWriter(LOG_FILE)) {
                log.write(ERROR);
            }
            catch(IOException ex){
                ex.printStackTrace();
            } 
        }  
    } 
    
    public static void loadGame() {

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(LOAD_TITLE);
        alert.setContentText(LOAD_TEXT);
        ButtonType loadFromHome = new ButtonType(BUTTON_HOME);
        ButtonType loadFromLocal = new ButtonType(BUTTON_LOCAL);  
        ButtonType loadFromAuto = new ButtonType(BUTTON_AUTO);  
        alert.getButtonTypes().setAll(loadFromHome, loadFromLocal, loadFromAuto, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == loadFromHome)
            loadFile(Process.HOME); 
        if (result.get() == loadFromLocal)
            loadFile(Process.LOCAL);    
        if (result.get() == loadFromAuto)
            loadFile(Process.AUTO);

        alert.close();   
    }

    public static int parseCoords(BufferedReader reader, ArrayList<String> coords) throws IOException {

        int count = Integer.parseInt(reader.readLine());  
        for(int i = 0; i < count; i++)
          coords.add(reader.readLine());
        return count;
    }
}