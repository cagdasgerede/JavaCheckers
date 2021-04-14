package pl.nogacz.checkers.achievements;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;


public class Achievement {

    private String AchName;
    private boolean isAchUnlocked;

    public Achievement(){}

    public Achievement(String AchName, boolean isAchUnlocked){
        this.AchName = AchName;
        this.isAchUnlocked = isAchUnlocked;
    }

    public void checkAchievement(Achievement [] achievements, int countOfMoves, long countOfTime){}

    public String getAchName(){
        return AchName;
    }

    public boolean getisAchUnlocked(){
        return isAchUnlocked;
    }

    public void UnlockAch(){
        this.isAchUnlocked = true;
    }

    public void LockAch(){
        this.isAchUnlocked = false;
    }

    public void showAchievement(){
        try{
            JPanel panel = new JPanel();
            BufferedImage image = ImageIO.read(new File( "./src/main/resources/achievements/" + AchName + ".png" ));  
            JLabel label = new JLabel(new ImageIcon(image));
            panel.add(label);

            JFrame frame = new JFrame("");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            frame.add(panel); 
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }catch(Exception e){};
        
    }

    public static void generateAchievements(int countOfMoves, long countOfTime){
        Achievement [] allAchievements = new Achievement[4];
        allAchievements[0] = new AllUnlockedAch("AllUnlockedAch",false);
        allAchievements[1] = new LongGameAch("LongGameAch",false);
        allAchievements[2] = new NumberOfMovesAch("NumberOfMovesAch",false);
        allAchievements[3] = new ShortGameAch("ShortGameAch",false);

        readSaveFile(allAchievements);

            for(int i=1 ; i<4 ; i++)
                allAchievements[i].checkAchievement(allAchievements, countOfMoves, countOfTime);
        
        allAchievements[0].checkAchievement(allAchievements, countOfMoves, countOfTime);

        writeSaveFile(allAchievements);
        
    }

    public static void readSaveFile(Achievement [] achievements){
        try{
            File f = new File("Achievements_Save.txt");
            String [] lineList = new String [5];

            if(f.exists()){
                BufferedReader br = new BufferedReader(new FileReader(f));
                String line="";

                for(int i=0; ((line = br.readLine()) != null) ; i++){
                    lineList[i]=line;
                }

                String fileContents="";

                for(int i=0; i<4; i++){
                    fileContents = fileContents + lineList[i];
                    if( lineList[i].substring( lineList[i].indexOf(':') + 1 ).equals("Unlocked") ){
                        achievements[i].UnlockAch();
                    }
                }

                if( !(""+fileContents.hashCode()).equals( lineList[5] ) ){
                    for (Achievement ach : achievements) {
                        ach.LockAch();
                    }

                    f.delete();
                    br.close();
                    throw new Exception("Save file corrupted.");
                }

                 br.close();

            }
        }catch(Exception e){};
    }

    public static void writeSaveFile(Achievement [] achievements){
        try{    
            File f = new File("Achievements_Save.txt");
            f.delete();
            f.createNewFile();

            FileOutputStream fos = new FileOutputStream(f);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            String fileContents="";

            for( Achievement ach : achievements){
                bw.write(ach.getAchName()+":");
                fileContents = fileContents + ach.getAchName()+":";

                if(ach.getisAchUnlocked()){
                    bw.write("Unlocked");
                    fileContents = fileContents + "Unlocked";
                }else{
                    bw.write("Locked");
                    fileContents = fileContents + "Locked";
                }

		        bw.newLine();
            }

            bw.write(""+fileContents.hashCode());

            bw.close();

        }catch(Exception e){};
    }

}
