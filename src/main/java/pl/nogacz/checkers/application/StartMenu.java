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
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    private Board start_board;
    private JButton startButton = new JButton(Board.Commands.START.name());
    private JButton exitButton = new JButton(Board.Commands.EXIT.name());

    public StartMenu(Board board) {
        super("Game Menu");
        start_board = board;
        board.setMenuActive(true);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setUndecorated(true);
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
            start_board.setMenuActive(false);;
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }else if(command.equalsIgnoreCase(Board.Commands.EXIT.name())) {
            System.exit(0);
        }
    }
} 
