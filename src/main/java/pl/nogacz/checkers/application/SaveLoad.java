package pl.nogacz.checkers.application;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import pl.nogacz.checkers.Checkers;
import pl.nogacz.checkers.application.Design;
import pl.nogacz.checkers.board.*;
import pl.nogacz.checkers.pawns.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class SaveLoad {

    private static HashMap<Coordinates, PawnClass> board = Board.getBoard();

    public static void autoSave() {        
        saveFile("auto");
    }

    public static void saveFile(String loc) {

        File file;
        if(loc.equals("home")) {
            String userHomeFolder = System.getProperty("user.home");
            file = new File(userHomeFolder, "JavaCheckersSavedGame.txt");
        }
        else if(loc.equals("local")){
            file = new File("JavaCheckersSavedGame.txt");
        }
        else {
            file = new File("JavaCheckersAutosavedGame.txt");
        }

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
            e.printStackTrace();
        }    

    }

    public static void saveGame() {

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Save Game");
        alert.setContentText("Game will be saved as 'JavaCheckersSavedGame.txt' to selected directory(Your Home Directory or Game Directory)");
        ButtonType saveToHome = new ButtonType("Home Directory");
        ButtonType saveToLocal = new ButtonType("Game Directory");  
        alert.getButtonTypes().setAll(saveToHome, saveToLocal, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == saveToHome)
            saveFile("home"); 
        if (result.get() == saveToLocal)
            saveFile("local");         

        alert.close();                 
    }

    public static void addPawn(ArrayList<String> list, HashMap<Coordinates, PawnClass> board, int a, int b) {

        for(int i = 0; i < list.size(); i++) {          

            int x = Integer.parseInt(list.get(i).charAt(0)+"");  
            int y = Integer.parseInt(list.get(i).charAt(2)+"");  
            Coordinates coord = new Coordinates(x,y);

            if (a==0 && b == 0) {
                PawnClass pawn = new PawnClass(Pawn.PAWN, PawnColor.WHITE);
                Design.removePawn(coord);
                board.remove(coord);
                Design.addPawn(coord, pawn);
                board.put(coord, pawn);
            }    
            else if (a == 0 && b == 1) {
                PawnClass pawn = new PawnClass(Pawn.PAWN, PawnColor.BLACK);
                Design.removePawn(coord);
                board.remove(coord);
                Design.addPawn(coord, pawn);
                board.put(coord, pawn);
            }
            else if (a == 1 && b == 1) {
                PawnClass pawn = new PawnClass(Pawn.QUEEN, PawnColor.BLACK);
                Design.removePawn(coord);
                board.remove(coord);
                Design.addPawn(coord, pawn);
                board.put(coord, pawn);
            }
            else if (a == 1 && b == 0) {
                PawnClass pawn = new PawnClass(Pawn.QUEEN, PawnColor.WHITE);
                Design.removePawn(coord);
                board.remove(coord);
                Design.addPawn(coord, pawn);
                board.put(coord, pawn);
            }

        }       
    }

    public static void loadFile(String loc) {

        File file;

        if(loc.equals("home")) {
            String userHomeFolder = System.getProperty("user.home");
            file = new File(userHomeFolder, "JavaCheckersSavedGame.txt");
        }
        else if(loc.equals("local")){
            file = new File("JavaCheckersSavedGame.txt");
        }
        else {
            file = new File("JavaCheckersAutosavedGame.txt");
        }

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
            
            addPawn(whiteCoords, board, 0, 0);
            addPawn(blackCoords, board, 0, 1);
            addPawn(whiteQueenCoords, board, 1, 0);
            addPawn(blackQueenCoords, board, 1, 1);
                   
        }
        catch(IOException e){
            e.printStackTrace();
        }  
    } 
    
    public static void loadGame() {

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Load Game");
        alert.setContentText("Game will be loaded from selected directory if you have saved before as 'JavaCheckedSavedGame.txt'.(Your Home Directory or Game Directory or Autosave)");
        ButtonType loadFromHome = new ButtonType("Home Directory");
        ButtonType loadFromLocal = new ButtonType("Game Directory");  
        ButtonType loadFromAuto = new ButtonType("Autosave");  
        alert.getButtonTypes().setAll(loadFromHome, loadFromLocal, loadFromAuto, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == loadFromHome)
            loadFile("home"); 
        if (result.get() == loadFromLocal)
            loadFile("local");    
        if (result.get() == loadFromAuto)
            loadFile("auto");

        alert.close();   

    }
}