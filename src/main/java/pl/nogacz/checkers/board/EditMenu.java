package pl.nogacz.checkers.board;

import javafx.application.Platform;
import pl.nogacz.checkers.application.Design;
import pl.nogacz.checkers.pawns.Pawn;
import pl.nogacz.checkers.pawns.PawnClass;
import pl.nogacz.checkers.pawns.PawnColor;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;

import static pl.nogacz.checkers.board.Board.getPawn;

public class EditMenu extends JFrame implements ActionListener {

    protected static Coordinates addPawnCoordinates;

    private Set<Coordinates> possibleReplaces = new HashSet<>();
    private Set<Coordinates> possibleAddPawns = new HashSet<>();

    private static final int WIDTH = 350;
    private static final int HEIGHT = 650;

    private static final int BUTTON_WIDTH = 300;
    private static final int BUTTON_HEIGHT = 60;

    private JButton removeButton = new JButton("Remove");
    private JButton replaceButton = new JButton("Replace");
    private JButton addWhitePawnButton = new JButton("White");
    private JButton addBlackPawnButton = new JButton("Black");
    private JButton addWhiteQueenButton = new JButton("WhiteQ");
    private JButton addBlackQueenButton = new JButton("BlackQ");
    private JButton exitButton = new JButton("Resume");

    private static final String BUTTON_REMOVE_SELECTED_PAWN = "button_remove-selected-pawn.png";
    private static final String BUTTON_REPLACE_SELECTED_PAWN = "button_replace-selected-pawn.png";
    private static final String BUTTON_RESUME = "button_resume.png";
    private static final String BUTTON_ADD_WHITE_PAWN = "button_add-white-pawn.png";
    private static final String BUTTON_ADD_BLACK_PAWN = "button_add-black-pawn.png";
    private static final String BUTTON_ADD_WHITE_QUEEN = "button_add-white-queen.png";
    private static final String BUTTON_ADD_BLACK_QUEEN = "button_add-black-queen.png";

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
        this.add(addWhiteQueenButton);
        this.add(addBlackQueenButton);
        this.add(exitButton);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Board.setEditMenuActive(false);
            }
        });
        setLayout(null);
        setVisible(true);
        setLocation(1330,130);
        toFront();
    }

    protected static void setIsPawnSelected(boolean value) {
        Board.isPawnSelected = value;
    }

    protected static void setIsEditMenuActive(boolean value) {
        Board.isEditMenuActive = value;
    }

    private void setButtons() {
        String removeButtonPNG = this.getClass().getClassLoader().getResource(BUTTON_REMOVE_SELECTED_PAWN).getFile();
        removeButton.setIcon(new ImageIcon(removeButtonPNG));
        removeButton.setBorder(BorderFactory.createEmptyBorder());
        removeButton.setContentAreaFilled(false);
        removeButton.setFocusable(false);

        String replaceButtonPNG = this.getClass().getClassLoader().getResource(BUTTON_REPLACE_SELECTED_PAWN).getFile();
        replaceButton.setIcon(new ImageIcon(replaceButtonPNG));
        replaceButton.setBorder(BorderFactory.createEmptyBorder());
        replaceButton.setContentAreaFilled(false);
        replaceButton.setFocusable(false);

        String exitButtonPNG = this.getClass().getClassLoader().getResource(BUTTON_RESUME).getFile();
        exitButton.setIcon(new ImageIcon(exitButtonPNG));
        exitButton.setBorder(BorderFactory.createEmptyBorder());
        exitButton.setContentAreaFilled(false);
        exitButton.setFocusable(false);

        String addWhiteButtonPNG = this.getClass().getClassLoader().getResource(BUTTON_ADD_WHITE_PAWN).getFile();
        addWhitePawnButton.setIcon(new ImageIcon(addWhiteButtonPNG));
        addWhitePawnButton.setBorder(BorderFactory.createEmptyBorder());
        addWhitePawnButton.setContentAreaFilled(false);
        addWhitePawnButton.setFocusable(false);

        String addWhiteQueenButtonPNG = this.getClass().getClassLoader().getResource(BUTTON_ADD_WHITE_QUEEN).getFile();
        addWhiteQueenButton.setIcon(new ImageIcon(addWhiteQueenButtonPNG));
        addWhiteQueenButton.setBorder(BorderFactory.createEmptyBorder());
        addWhiteQueenButton.setContentAreaFilled(false);
        addWhiteQueenButton.setFocusable(false);

        String addBlackButtonPNG = this.getClass().getClassLoader().getResource(BUTTON_ADD_BLACK_PAWN).getFile();
        addBlackPawnButton.setIcon(new ImageIcon(addBlackButtonPNG));
        addBlackPawnButton.setBorder(BorderFactory.createEmptyBorder());
        addBlackPawnButton.setContentAreaFilled(false);
        addBlackPawnButton.setFocusable(false);

        String addBlackQueenButtonPNG = this.getClass().getClassLoader().getResource(BUTTON_ADD_BLACK_QUEEN).getFile();
        addBlackQueenButton.setIcon(new ImageIcon(addBlackQueenButtonPNG));
        addBlackQueenButton.setBorder(BorderFactory.createEmptyBorder());
        addBlackQueenButton.setContentAreaFilled(false);
        addBlackQueenButton.setFocusable(false);

        removeButton.setBounds(20, 20, BUTTON_WIDTH, BUTTON_HEIGHT);
        replaceButton.setBounds(20, 100, BUTTON_WIDTH, BUTTON_HEIGHT);
        addWhitePawnButton.setBounds(20,180,BUTTON_WIDTH,BUTTON_HEIGHT);
        addBlackPawnButton.setBounds(20,260,BUTTON_WIDTH,BUTTON_HEIGHT);
        addWhiteQueenButton.setBounds(20,340,BUTTON_WIDTH,BUTTON_HEIGHT);
        addBlackQueenButton.setBounds(20,420,BUTTON_WIDTH,BUTTON_HEIGHT);
        exitButton.setBounds(20, 520, BUTTON_WIDTH, BUTTON_HEIGHT);

        addWhitePawnButton.addActionListener(this);
        addBlackPawnButton.addActionListener(this);
        addWhiteQueenButton.addActionListener(this);
        addBlackQueenButton.addActionListener(this);
        removeButton.addActionListener(this);
        replaceButton.addActionListener(this);
        exitButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Resume")) {
            Board.setIsPawnSelected(false);
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
        if (command.equals("Remove")) {
            Platform.runLater(() -> {
                removePawn(Board.getSelectedCoordinates());
                Design.removePawn(Board.getSelectedCoordinates());
                possibleReplaces.forEach(this::unLightReplace);
                possibleReplaces.clear();
                possibleAddPawns.clear();
                Board.setIsPawnSelected(false);
                Board.setSelectedCoordinates(null);
            });
        }
        if(command.equals("Replace")) {
            Platform.runLater(() -> {
                if(!possibleReplaces.isEmpty()){
                    Coordinates newCoordinate = possibleReplaces.iterator().next();
                    replacePawn(Board.getSelectedCoordinates(),newCoordinate);
                    possibleReplaces.forEach(this::unLightReplace);
                    possibleReplaces.clear();
                    possibleAddPawns.clear();
                    Board.setSelectedCoordinates(null);
                    Board.setIsPawnSelected(false);
                }
            });
        }
        if(command.equals("White")) {
            Platform.runLater(() -> {
                PawnClass pawn = new PawnClass(Pawn.PAWN, PawnColor.WHITE);
                addPawn(addPawnCoordinates,pawn);
                Design.addPawn(addPawnCoordinates,pawn);
                possibleAddPawns.forEach(this::unLightReplace);
                possibleAddPawns.clear();
                possibleReplaces.clear();
                addPawnCoordinates = null;
                Board.setSelectedCoordinates(null);
                Board.setIsPawnSelected(false);
            });
        }
        if(command.equals("Black")) {
            Platform.runLater(() -> {
                PawnClass pawn = new PawnClass(Pawn.PAWN, PawnColor.BLACK);
                addPawn(addPawnCoordinates,pawn);
                Design.addPawn(addPawnCoordinates,pawn);
                possibleAddPawns.forEach(this::unLightReplace);
                possibleAddPawns.clear();
                possibleReplaces.clear();
                addPawnCoordinates = null;
                Board.setSelectedCoordinates(null);
                Board.setIsPawnSelected(false);
            });
        }
        if(command.equals("WhiteQ")) {
            Platform.runLater(() -> {
                PawnClass pawn = new PawnClass(Pawn.QUEEN, PawnColor.WHITE);
                addPawn(addPawnCoordinates,pawn);
                Design.addPawn(addPawnCoordinates,pawn);
                possibleAddPawns.forEach(this::unLightReplace);
                possibleAddPawns.clear();
                possibleReplaces.clear();
                addPawnCoordinates = null;
                Board.setSelectedCoordinates(null);
                Board.setIsPawnSelected(false);
            });
        }
        if(command.equals("BlackQ")) {
            Platform.runLater(() -> {
                PawnClass pawn = new PawnClass(Pawn.QUEEN, PawnColor.BLACK);
                addPawn(addPawnCoordinates,pawn);
                Design.addPawn(addPawnCoordinates,pawn);
                possibleAddPawns.forEach(this::unLightReplace);
                possibleAddPawns.clear();
                possibleReplaces.clear();
                addPawnCoordinates = null;
                Board.setSelectedCoordinates(null);
                Board.setIsPawnSelected(false);
            });
        }
    }

    protected static Coordinates getAddPawnCoordinates() {
        return addPawnCoordinates;
    }

    protected static void setAddPawnCoordinates(Coordinates coordinates) {
        addPawnCoordinates = coordinates;
    }

    protected void unLightReplace(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);
        Design.removePawn(coordinates);
        if(pawn!= null){
            Design.addPawn(coordinates,pawn);
        }
    }

    protected void replacePawn(Coordinates oldCoordinates, Coordinates newCoordinates) {
        PawnClass pawn = getPawn(oldCoordinates);
        Design.removePawn(oldCoordinates);
        Design.addPawn(newCoordinates,pawn);
        Board.getBoard().remove(oldCoordinates);
        Board.getBoard().put(newCoordinates, pawn);
    }

    protected static void replacePawnForTest(Coordinates oldCoordinates, Coordinates newCoordinates) {
        PawnClass pawn = getPawn(oldCoordinates);
        Board.getBoard().remove(oldCoordinates);
        Board.getBoard().put(newCoordinates, pawn);
    }

    protected Set<Coordinates> getPossibleReplaces() {
        return possibleReplaces;
    }

    protected Set<Coordinates> getPossibleAddPawns() {
        return  possibleAddPawns;
    }

    protected void forEachUnlightPossibleReplace() {
        possibleReplaces.forEach(this::unLightReplace);
    }

    protected void forEachUnlightAddPawn() {
        possibleAddPawns.forEach(this::unLightReplace);
    }

    protected static void unLightEditSelect(Coordinates coordinates) {
        Board.unLightPawn(coordinates);
    }

    protected static void editLightPawn(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);
        Design.removePawn(coordinates);
        Design.addEditLightPawn(coordinates,pawn);
    }

    protected static void setSelectedCoordinates(Coordinates coordinates){
        Board.selectedCoordinates = coordinates;
    }

    protected static void removePawn(Coordinates coordinates){
        Board.getBoard().remove(coordinates);
    }

    protected static void addPawn(Coordinates coordinates, PawnClass pawn) {
        Board.getBoard().put(coordinates,pawn);
    }
}