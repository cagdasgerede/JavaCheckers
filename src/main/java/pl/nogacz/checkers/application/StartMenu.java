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

public class StartMenu extends JFrame implements ActionListener{
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    private JButton Start_button = new JButton("Start");
    private JButton Exit_button = new JButton("EXIT");
    private Board start_board;
    public StartMenu(Board board) {  
        super("Game Menu");   
        start_board = board;  
        board.isMenuActive=true;
        setSize(WIDTH,HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // dont need that but maybe someone want to remove undecorated
        this.setUndecorated(true);
        this.setBackground(new Color(0, 0, 0,100));
        this.add(Start_button);
        this.add(Exit_button);
        //Set Bounds
        String start_path = this.getClass().getClassLoader().getResource("button_start.png").getFile();
        String exit_path = this.getClass().getClassLoader().getResource("button_exit.png").getFile();
        Start_button.setIcon(new ImageIcon(start_path));
        Exit_button.setIcon(new ImageIcon(exit_path));
        Start_button.setBounds(150, 125, 165, 80);
        Exit_button.setBounds(150, 300, 150, 80);
        //Give them Func
        Start_button.addActionListener(this);
        Exit_button.addActionListener(this);
        setLocationRelativeTo(null);
        setLayout(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String command = actionEvent.getActionCommand();
        if(command.equals("Start")) {
                start_board.isMenuActive=false;
                this.dispatchEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));
        }else if(command.equals("EXIT")){
            System.exit(0);
        }
    }
} 