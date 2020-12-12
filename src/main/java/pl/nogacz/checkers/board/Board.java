package pl.nogacz.checkers.board;

import javafx.application.Platform;
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

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Dawid Nogacz on 17.05.2019
 */
public class Board{
    private static HashMap<Coordinates, PawnClass> board = new HashMap<>();

    private boolean isSelected = false;
    private boolean newKick = false;
    private Coordinates selectedCoordinates;
    public boolean isEditMenuActive = false;

    private Set<Coordinates> possibleMoves = new HashSet<>();
    private Set<Coordinates> possibleKick = new HashSet<>();
    private Set<Coordinates> possiblePromote = new HashSet<>();
    private Set<Coordinates> possibleReplaces = new HashSet<>();

    private boolean isGameEnd = false;
    private int roundWithoutKick = 0;

    private boolean isComputerRound = false;
    private Computer computer = new Computer();

    EditMenu editMenu;
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
           possibleReplaces.forEach(this::unLightReplace);
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
                editMenu = null;
            }
        }else{
            Coordinates eventCoordinates = new Coordinates((int) ((event.getX() - 37) / 85), (int) ((event.getY() - 37) / 85));
          if(isSelected) {
                if(eventCoordinates.getY() == selectedCoordinates.getY() && eventCoordinates.getX() == selectedCoordinates.getX()) {
                    if(isFieldNotNull(eventCoordinates)) {
                        unLightSelect(selectedCoordinates);
                    }
                    else{
                        unLightReplace(eventCoordinates);
                    }
                         isSelected = false;
                }
                else if(!isFieldNotNull(eventCoordinates)){
                    if(eventCoordinates.isValid()) {
                        possibleReplaces.forEach(this::unLightReplace);
                        possibleReplaces.clear();
                        lightReplace(eventCoordinates);
                        possibleReplaces.add(eventCoordinates);
                    }
                }
            }
            else if (!isFieldNotNull(eventCoordinates) && eventCoordinates.isValid()){
                isSelected = true;
                selectedCoordinates = eventCoordinates;
                possibleReplaces.add(eventCoordinates);
                lightReplace(eventCoordinates);
            }
            else if(isFieldNotNull(eventCoordinates) && eventCoordinates.isValid()) {
                possibleReplaces.forEach(this::unLightReplace);
                if (isFieldNotNull(eventCoordinates)) {
                    isSelected = true;
                    selectedCoordinates = eventCoordinates;
                    editLightSelect(eventCoordinates);
                }
            }


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
    private void replacePawn(Coordinates oldCoordinates, Coordinates newCoordinates){
        PawnClass pawn = getPawn(oldCoordinates);
        Design.removePawn(oldCoordinates);
        Design.addPawn(newCoordinates,pawn);
        board.remove(oldCoordinates);
        board.put(newCoordinates, pawn);
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

    private void unLightPawn(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);
        Design.removePawn(coordinates);
        Design.addPawn(coordinates, pawn);
    }

    private void unLightMove(Coordinates coordinates) {
        Design.removePawn(coordinates);
    }
    private void unLightReplace(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);
        Design.removePawn(coordinates);
        if(pawn!= null){
            Design.addPawn(coordinates,pawn);
        }
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

    class EditMenu extends JFrame implements  ActionListener {

        public static final int WIDTH = 350;
        public static final int HEIGHT = 550;
        private JButton removeButton = new JButton("Remove");
        private JButton replaceButton = new JButton("Replace");
        private JButton addWhitePawnButton = new JButton("White");
        private JButton addBlackPawnButton = new JButton("Black");
        private JButton exitButton = new JButton("Ready");
        public EditMenu() {
            super(" Customize the game");
            setSize(WIDTH, HEIGHT);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setContentPane(new JLabel(new ImageIcon(this.getClass().getClassLoader().getResource("edit-menu-background.png").getFile())));
            setButtons();
            this.add(removeButton);
            this.add(replaceButton);
            this.add(addWhitePawnButton);
            this.add(addBlackPawnButton);
            this.add(exitButton);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                     isEditMenuActive = false;
                }
            });
            setLayout(null);
            setVisible(true);
            setLocation(1330,200);
            toFront();
        }

        private void setButtons() {
            String removeButtonPNG = this.getClass().getClassLoader().getResource("button_remove-selected-pawn.png").getFile();
            removeButton.setIcon(new ImageIcon(removeButtonPNG));
            removeButton.setBorder(BorderFactory.createEmptyBorder());
            removeButton.setContentAreaFilled(false);
            removeButton.setFocusable(false);
            String replaceButtonPNG = this.getClass().getClassLoader().getResource("button_replace-selected-pawn.png").getFile();
            replaceButton.setIcon(new ImageIcon(replaceButtonPNG));
            replaceButton.setBorder(BorderFactory.createEmptyBorder());
            replaceButton.setContentAreaFilled(false);
            replaceButton.setFocusable(false);
            String exitButtonPNG = this.getClass().getClassLoader().getResource("button_ready.png").getFile();
            exitButton.setIcon(new ImageIcon(exitButtonPNG));
            exitButton.setBorder(BorderFactory.createEmptyBorder());
            exitButton.setContentAreaFilled(false);
            exitButton.setFocusable(false);
            String addWhiteButtonPNG = this.getClass().getClassLoader().getResource("button_add-white-pawn.png").getFile();
            addWhitePawnButton.setIcon(new ImageIcon(addWhiteButtonPNG));
            addWhitePawnButton.setBorder(BorderFactory.createEmptyBorder());
            addWhitePawnButton.setContentAreaFilled(false);
            addWhitePawnButton.setFocusable(false);
            String addBlackButtonPNG = this.getClass().getClassLoader().getResource("button_add-black-pawn.png").getFile();
            addBlackPawnButton.setIcon(new ImageIcon(addBlackButtonPNG));
            addBlackPawnButton.setBorder(BorderFactory.createEmptyBorder());
            addBlackPawnButton.setContentAreaFilled(false);
            addBlackPawnButton.setFocusable(false);
            removeButton.setBounds(20, 20, 300, 60);
            replaceButton.setBounds(20, 100, 300, 60);
            addWhitePawnButton.setBounds(20,180,300,60);
            addBlackPawnButton.setBounds(20,260,300,60);
            exitButton.setBounds(20, 360, 310, 60);
            addWhitePawnButton.addActionListener(this);
            addBlackPawnButton.addActionListener(this);
            removeButton.addActionListener(this);
            replaceButton.addActionListener(this);
            exitButton.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("Ready")) {
                isEditMenuActive = false;
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }
            if (command.equals("Remove")) {
                Platform.runLater(() -> {
                    board.remove(selectedCoordinates);
                    Design.removePawn(selectedCoordinates);
                    possibleReplaces.forEach(Board.this::unLightReplace);
                    possibleReplaces.clear();
                    isSelected = false;
                    selectedCoordinates = null;
                });
            }
            if(command.equals("Replace")){
                Platform.runLater(() -> {
                            if(!possibleReplaces.isEmpty()){
                                Coordinates newCoordinate = possibleReplaces.iterator().next();
                                replacePawn(selectedCoordinates,newCoordinate);
                                possibleReplaces.forEach(Board.this::unLightReplace);
                                possibleReplaces.clear();
                                selectedCoordinates = null;
                                isSelected = false;
                            }
                });
            }
            if(command.equals("White")){
                Platform.runLater(() -> {
                    PawnClass pawn = new PawnClass(Pawn.PAWN, PawnColor.WHITE);
                    board.put(selectedCoordinates,pawn);
                    Design.addPawn(selectedCoordinates,pawn);
                    possibleReplaces.forEach(Board.this::unLightReplace);
                    possibleReplaces.clear();
                    selectedCoordinates = null;
                    isSelected = false;
                });
            }
            if(command.equals("Black")){
                Platform.runLater(() -> {
                    PawnClass pawn = new PawnClass(Pawn.PAWN, PawnColor.BLACK);
                    board.put(selectedCoordinates,pawn);
                    Design.addPawn(selectedCoordinates,pawn);
                    possibleReplaces.forEach(Board.this::unLightReplace);
                    possibleReplaces.clear();
                    selectedCoordinates = null;
                    isSelected = false;
                });
            }
        }


    }

}
