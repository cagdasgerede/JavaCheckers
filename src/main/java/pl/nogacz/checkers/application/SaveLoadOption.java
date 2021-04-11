package main.java.pl.nogacz.checkers.application;

import javafx.scene.control.*;
import java.io.*;
import java.util.*;
import pl.nogacz.checkers.Checkers;
import pl.nogacz.checkers.application.Design;
import pl.nogacz.checkers.pawns.Pawn;
import pl.nogacz.checkers.pawns.PawnClass;
import pl.nogacz.checkers.pawns.PawnColor;
import pl.nogacz.checkers.board.Board;
import pl.nogacz.checkers.board.Coordinates;

public class SaveLoadOption{

    private static HashMap<Coordinates, PawnClass> board = Board.getBoard();

    public static void save(){

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Save the Game!");
        alert.setContentText("Please choose the directory that game will be saved to.");

        ButtonType saveToDesktop = new ButtonType("Desktop");
        ButtonType saveToHome = new ButtonType("Home Directory");
        ButtonType saveToLocal = new ButtonType("Local Game Directory");  

        alert.getButtonTypes().setAll(saveToDesktop, saveToHome, saveToLocal, ButtonType.CANCEL);
        Optional<ButtonType> selected = alert.showAndWait();

        if (selected.get() == saveToDesktop)
            saveTxt("desktop"); 
        if (selected.get() == saveToHome)
            saveTxt("home"); 
        if (selected.get() == saveToLocal)
            saveTxt("local");         

        alert.close();
    } 

    public static void saveTxt(String directory){
        
        File savedFile;
        if(directory.equals("desktop")){
            savedFile = new File(System.getProperty("user.home") + "/Desktop", "CheckersSaved.txt");
        }
        else if(directory.equals("home")){
            savedFile = new File(System.getProperty("user.home"), "CheckersSaved.txt");
        }
        else{
            savedFile = new File("CheckersSaved.txt");
        }
        
        ArrayList <String> lines = new ArrayList<>();
        // QW: 1, W: 2, QB: 3, B: 4, NULL: 5
        for(int i = 0; i < 8; i++){

            String line = "";
            for(int j = 0; j < 8; j++){

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
              
            for(int i = 0; i < 8; i++){
                save.write(lines.get(i) + "\n");
            }    
            save.close();
        }

        catch(IOException e){

             e.printStackTrace();
        }   
    }

    public static void load(){

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Load the Game!");
        alert.setContentText("Please choose the directory that game will be loaded from.");

        ButtonType loadFromDesktop = new ButtonType("Desktop");
        ButtonType loadFromHome = new ButtonType("Home Directory");
        ButtonType loadFromLocal = new ButtonType("Local Game Directory");  

        alert.getButtonTypes().setAll(loadFromDesktop, loadFromHome, loadFromLocal, ButtonType.CANCEL);
        Optional<ButtonType> selected = alert.showAndWait();

        if (selected.get() == loadFromDesktop)
            loadTxt("desktop"); 
        if (selected.get() == loadFromHome)
            loadTxt("home"); 
        if (selected.get() == loadFromLocal)
            loadTxt("local");         

        alert.close();
    }

    public static void loadTxt(String directory){
        
        File loadedFile;
        if(directory.equals("desktop")){
            loadedFile = new File(System.getProperty("user.home") + "/Desktop", "CheckersSaved.txt");
        }
        else if(directory.equals("home")){
            loadedFile = new File(System.getProperty("user.home"), "CheckersSaved.txt");
        } 
        else {
            loadedFile = new File("CheckersSaved.txt");
        }

        try(BufferedReader load = new BufferedReader(new FileReader(loadedFile))){

            for(int x = 0; x < 8; x++) {
                for(int y = 0; y < 8; y++) {

                    Coordinates coordinates = new Coordinates(x,y);
                    Design.removePawn(coordinates);
                    board.remove(coordinates);
                }
            }

            for(int x = 0; x < 8; x++) {

                String line = decrypt(load.readLine());
                for(int y = 0; y < 8; y++) {
                    // QW: 1, W: 2, QB: 3, B: 4, NULL: 5
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

            e.printStackTrace();
        }   
    }

    private static String encrypt(String line){

        int decimal = Integer.parseInt(line);
        return Integer.toBinaryString(decimal);
    }

    private static String decrypt(String line){

        int decimal = Integer.parseInt(line,2);
        return decimal+"";
    }
} 