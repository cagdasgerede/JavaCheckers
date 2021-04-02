package pl.nogacz.checkers.audio;
import pl.nogacz.checkers.application.Resources;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;
/*
    Sound effects gotten from https://freesound.org/ 
*/ 

public class Audio {
    public AudioNode background;
    public AudioNode placementWhite;
    public AudioNode winSong;
    public AudioNode loseSong;
    public AudioNode drawSong;



    public Audio(){
            background=new AudioNode("background.wav");
            placementWhite=new AudioNode("placementWhite.wav");
            winSong=new AudioNode("WinSong.wav");
            loseSong=new AudioNode("LossSong.wav");
            drawSong=new AudioNode("TieSong.wav");


    }
    public class AudioNode{

    public Clip clip;
    public String fileName;
    public AudioNode(String fileName){
        this.fileName=fileName;
        
    }
    public void play(){
        setFile(fileName);
    }
    public void stop(){
        clip.stop();
        
    }
    public void resume(){
        play();
    }
    public void setFile(String fileName){
        boolean loop=false;
        if(fileName.equals("background.wav"))
            loop=true;
        try{
            File file = new File(Resources.getPath("songs/" + fileName).substring(5));
            AudioInputStream sound =  AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(sound);
            clip.setFramePosition(0);

            if(loop)
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            else
                clip.start();

        }
        catch(IOException e){
             System.out.println("Song File is not found");
        }
        catch(UnsupportedAudioFileException e){
            System.out.println("UnsupportedAudioFileException");
        }
        catch(Exception e){
            System.out.println("Different Exception");
        }
    } 
    
    }
}
