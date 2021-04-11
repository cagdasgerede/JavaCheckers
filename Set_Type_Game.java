import java.awt.*;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class Set_Type_Game extends JFrame implements ActionListener {

    public enum GameType{
        HARD, MEDIUM, EASY
    }

    public GameType gt; 

    public Set_Type_Game(){
        setSize(500,500);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Difficulty Choice");
		setLayout(new BorderLayout());
        
		JPanel button_panel = new JPanel();
		button_panel.setLayout(new  GridLayout(3,1));
		button_panel.setBackground(Color.BLACK);
		
		JButton easy = new JButton("EASY");
		JButton medium = new JButton("MEDIUM");
		JButton hard = new JButton("HARD");

        easy.addActionListener(this);
		medium.addActionListener(this);
		hard.addActionListener(this);
		
		easy.setBackground(Color.DARK_GRAY);
		medium.setBackground(Color.DARK_GRAY);
		hard.setBackground(Color.DARK_GRAY);

		easy.setForeground(Color.GREEN);
		medium.setForeground(Color.YELLOW);
		hard.setForeground(Color.red);

		easy.setFont(new Font("Times New Roman", Font.BOLD, 40));
		medium.setFont(new Font("Times New Roman", Font.BOLD, 40));
		hard.setFont(new Font("Times New Roman", Font.BOLD, 40));

        button_panel.add(easy);
		button_panel.add(medium);
		button_panel.add(hard);
		
		add(button_panel, BorderLayout.CENTER);

		setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {
		String action_command = e.getActionCommand();
		switch(action_command){
			case "EASY":
				gt = GameType.EASY;
				break;
			case "MEDIUM":
				gt = GameType.MEDIUM;
				break;
			case "HARD":
				gt = GameType.HARD;
				break;
		}
	}

}