package pl.nogacz.checkers.application;

import pl.nogacz.checkers.Checkers;
import pl.nogacz.checkers.board.Board;

import java.awt.Color;

import javax.sound.midi.Soundbank;
import javax.swing.*;

import jdk.javadoc.internal.doclets.formats.html.SourceToHTMLConverter;
import jdk.javadoc.internal.doclets.toolkit.taglets.SystemPropertyTaglet;

import java.awt.event.*;
import java.util.ArrayList;
import java.io.File;

public class StartMenu extends JFrame implements ActionListener {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private Board startBoard;
    private JButton startButton = new JButton(Board.Commands.START.name());
    private JButton exitButton = new JButton(Board.Commands.EXIT.name());
    private boolean menuOpen;
    public JButton getStartButton() {
        return startButton;
    }

    public void setStartButton(JButton startButton) {
        this.startButton = startButton;
    }

    public Board getStartBoard() {
        return startBoard;
    }

    public boolean isMenuOpen() {
        return menuOpen;
    }

    public void setStartBoard(Board startBoard) {
        this.startBoard = startBoard;
    }

    public JButton getExitButton() {
        return exitButton;
    }

    public void setExitButton(JButton exitButton) {
        this.exitButton = exitButton;
    }

    public StartMenu(Board board) {
        super("Game Menu");
        startBoard = board;
        startBoard.setMenuActive(true);
        menuOpen=true;
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setUndecorated(true);
        Board ozan = new Board(); // is working
        this.setBackground(new Color(0, 0, 0, 100));//transparent black
        this.add(startButton);
        this.add(exitButton); 
        startButton.setIcon(board.getImageIconForFileName("button_start.png"));
        exitButton.setIcon(board.getImageIconForFileName("button_exit.png"));
        startButton.setBounds(150, 125, 165, 80);
        exitButton.setBounds(150, 300, 150, 80);
        startButton.addActionListener(this);
        exitButton.addActionListener(this);
        setLocationRelativeTo(null);
        setLayout(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String command = actionEvent.getActionCommand();
        if(command.equalsIgnoreCase(Board.Commands.START.name())) {
            startBoard.setMenuActive(false);
            menuOpen = false;
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }else if(command.equalsIgnoreCase(Board.Commands.EXIT.name())) {
            System.exit(1735);//random number for not match with an error
        }
    }
} 
