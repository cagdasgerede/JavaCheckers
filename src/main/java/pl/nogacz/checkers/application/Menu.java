package pl.nogacz.checkers.application;
import pl.nogacz.checkers.Checkers;

import java.awt.Color;

import javax.sound.midi.Soundbank;
import javax.swing.*;

import jdk.javadoc.internal.doclets.formats.html.SourceToHTMLConverter;
import jdk.javadoc.internal.doclets.toolkit.taglets.SystemPropertyTaglet;

import java.awt.event.*;
import java.util.ArrayList;
import java.io.File;

public class Menu extends JFrame implements ActionListener{
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    private JButton Resume_button = new JButton("Resume");
    private JButton NewGame_button = new JButton("New Game");
    private JButton Exit_button = new JButton("EXIT");

    public Menu() {
        //Initialize
        super("Game Menu");
        setSize(WIDTH,HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setBackground(Color.LIGHT_GRAY);
        this.add(Resume_button);
        this.add(NewGame_button);
        this.add(Exit_button);
        //Set Bounds
        Resume_button.setBounds(200, 50, 100, 100);
        NewGame_button.setBounds(200, 200, 100, 100);
        Exit_button.setBounds(200, 350, 100, 100);
        //Give them Func
        Resume_button.addActionListener(this);
        NewGame_button.addActionListener(this);
        Exit_button.addActionListener(this);

        setLocationRelativeTo(null);
        setLayout(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String command = actionEvent.getActionCommand();
        if(command.equals("Resume")) {
                this.dispatchEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));
        }else if(command.equals("New Game")) {
            restartApplication();
        }else if(command.equals("EXIT")){
            System.exit(0);
        }
    }

    public static void restartApplication()
    {
        try {
            final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
            String executionPath = System.getProperty("user.dir");
            System.out.println(executionPath);
            //Process cmd_build = Runtime.getRuntime().exec("gradle -p "+executionPath + " build");
            Process cmd_run = Runtime.getRuntime().exec("gradle -p "+executionPath + " run");
            System.exit(0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}