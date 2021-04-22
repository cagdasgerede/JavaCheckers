package main.java.pl.nogacz.checkers.application;

import pl.nogacz.checkers.Checkers;
import pl.nogacz.checkers.application.Design;
import pl.nogacz.checkers.pawns.Pawn;
import pl.nogacz.checkers.pawns.PawnClass;
import pl.nogacz.checkers.pawns.PawnColor;
import pl.nogacz.checkers.board.Board;
import pl.nogacz.checkers.board.Coordinates;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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

public class SaveLoadOption{

    private static HashMap<Coordinates, PawnClass> board = Board.getBoard();

    private static final int SIZE = 8;
    private static final String LOCATION_BUTTON_DESKTOP = "Desktop";
    private static final String LOCATION_BUTTON_HOME = "Home Directory";
    private static final String LOCATION_BUTTON_LOCAL = "Local Game Directory";

    private static final String SAVE_TITLE = "Save the Game!";
    private static final String SAVE_CONTENT_TEXT = "Please choose the directory that game will be saved to.";
    private static final String LOAD_TITLE = "Load the Game!";
    private static final String LOAD_CONTENT_TEXT = "Please choose the directory that game will be loaded from.";

    private static final String DIRECTORY_DESKTOP = System.getProperty("user.home") + "/Desktop";
    private static final String DIRECTORY_HOME = System.getProperty("user.home");
    private static final String DOCUMENT_NAME = "CheckersSaved.txt";

    private static final String LOG_FILE = "log.txt";
    private static final String ERROR_MESSAGE = "File can not found!";

    public static void save(){

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(SAVE_TITLE);
        alert.setContentText(SAVE_CONTENT_TEXT);

        ButtonType saveToDesktop = new ButtonType(LOCATION_BUTTON_DESKTOP);
        ButtonType saveToHome = new ButtonType(LOCATION_BUTTON_HOME);
        ButtonType saveToLocal = new ButtonType(LOCATION_BUTTON_LOCAL);  

        alert.getButtonTypes().setAll(saveToDesktop, saveToHome, saveToLocal, ButtonType.CANCEL);
        Optional<ButtonType> selected = alert.showAndWait();

        if (selected.get() == saveToDesktop)
            saveTxt(LOCATION_BUTTON_DESKTOP); 
        if (selected.get() == saveToHome)
            saveTxt(LOCATION_BUTTON_HOME); 
        if (selected.get() == saveToLocal)
            saveTxt(LOCATION_BUTTON_LOCAL);         

        alert.close();
    } 

    public static void saveTxt(String directory){
        
        File savedFile;
        if(directory.equals(LOCATION_BUTTON_DESKTOP)){
            savedFile = new File(DIRECTORY_DESKTOP, DOCUMENT_NAME);
        }
        else if(directory.equals(LOCATION_BUTTON_HOME)){
            savedFile = new File(DIRECTORY_HOME, DOCUMENT_NAME);
        }
        else{
            savedFile = new File(DOCUMENT_NAME);
        }
        
        ArrayList <String> lines = new ArrayList<>();

        for(int i = 0; i < SIZE; i++){

            String line = "";
            for(int j = 0; j < SIZE; j++){

                Coordinates coordinate = new Coordinates(i,j);
                if(Board.isFieldNotNull(coordinate)){

                    if(Board.getPawn(coordinate).getColor().isWhite()){
                        if(Board.getPawn(coordinate).getPawn().isQueen()){
                            line += "1";
                        }
                        else{
                            line += "2";
                        }
                    }

                    else{
                        if(Board.getPawn(coordinate).getPawn().isQueen()){
                            line += "3";
                        }
                        else{
                            line += "4";
                        }
                    }
                }

                else{
                    line += "5";
                }
            } 

            lines.add(encrypt(line));
        }

        try(FileWriter save = new FileWriter(savedFile)) {
              
            for(int i = 0; i < SIZE; i++){
                save.write(lines.get(i) + "\n");
            }    
            save.close();
        }

        catch(IOException e){

            try(FileWriter logfile = new FileWriter(LOG_FILE)) {
                logfile.write(ERROR_MESSAGE);
            }
            catch(IOException ee){
                ee.printStackTrace();
            } 
        }   
    }

    public static void load(){

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(LOAD_TITLE);
        alert.setContentText(LOAD_CONTENT_TEXT);

        ButtonType loadFromDesktop = new ButtonType(LOCATION_BUTTON_DESKTOP);
        ButtonType loadFromHome = new ButtonType(LOCATION_BUTTON_HOME);
        ButtonType loadFromLocal = new ButtonType(LOCATION_BUTTON_LOCAL);

        alert.getButtonTypes().setAll(loadFromDesktop, loadFromHome, loadFromLocal, ButtonType.CANCEL);
        Optional<ButtonType> selected = alert.showAndWait();

        if (selected.get() == loadFromDesktop)
            loadTxt(LOCATION_BUTTON_DESKTOP); 
        if (selected.get() == loadFromHome)
            loadTxt(LOCATION_BUTTON_HOME); 
        if (selected.get() == loadFromLocal)
            loadTxt(LOCATION_BUTTON_LOCAL);         

        alert.close();
    }

    public static void loadTxt(String directory){
        
        File loadedFile;
        if(directory.equals(LOCATION_BUTTON_DESKTOP)){
            loadedFile = new File(DIRECTORY_DESKTOP, DOCUMENT_NAME);
        }
        else if(directory.equals(LOCATION_BUTTON_HOME)){
            loadedFile = new File(DIRECTORY_HOME, DOCUMENT_NAME);
        }
        else{
            loadedFile = new File(DOCUMENT_NAME);
        }

        try(BufferedReader load = new BufferedReader(new FileReader(loadedFile))){

            for(int x = 0; x < SIZE; x++) {
                for(int y = 0; y < SIZE; y++) {

                    Coordinates coordinates = new Coordinates(x,y);
                    Design.removePawn(coordinates);
                    board.remove(coordinates);
                }
            }

            for(int x = 0; x < SIZE; x++) {

                String line = decrypt(load.readLine());
                for(int y = 0; y < SIZE; y++) {

                    char num = line.charAt(y);
                    if(num != '5'){

                        Coordinates coordinates = new Coordinates(x,y);
                        PawnClass pawn;
                        if(num == '1'){
                            pawn = new PawnClass(Pawn.QUEEN, PawnColor.WHITE);
                        }
                        else if(num == '2'){
                            pawn = new PawnClass(Pawn.PAWN, PawnColor.WHITE);
                        }
                        else if(num == '3'){
                            pawn = new PawnClass(Pawn.QUEEN, PawnColor.BLACK);
                        }
                        else{
                            pawn = new PawnClass(Pawn.PAWN, PawnColor.BLACK);
                        }

                        Design.addPawn(coordinates,pawn);
                        board.put(coordinates, pawn);
                    }
                }
            }

            load.close();
        }

        catch(IOException e){

            try(FileWriter logfile = new FileWriter(LOG_FILE)) {
                logfile.write(ERROR_MESSAGE);
            }
            catch(IOException ee){
                ee.printStackTrace();
            } 
        }   
    }

    public static String encrypt(String line){

        int decimal = Integer.parseInt(line);
        return Integer.toBinaryString(decimal);
    }

    public static String decrypt(String line){

        int decimal = Integer.parseInt(line,2);
        return decimal+"";
    }
} 