package pl.nogacz.checkers.audio;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {
    private List<AudioClip> listOfAudio;

    public AudioPlayer(){
        listOfAudio = new ArrayList<>();
    }

    public Clip getClip(String name){
        final URL file = AudioPlayer.class.getResource("/sounds/"+name);
        try(AudioInputStream inStream = AudioSystem.getAudioInputStream(file)){
            final Clip clip = AudioSystem.getClip();
            clip.open(inStream);
            clip.setMicrosecondPosition(0);
            return clip;
        } catch(UnsupportedAudioFileException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        } catch(LineUnavailableException e){
            e.printStackTrace();
        }
        return null;
    }

    public void playBackgroundMusic(String name){
        final Clip clip = getClip(name);
        listOfAudio.add(new BackgroundMusic(clip));
    }

    public void playSoundEffect(String name){
        final Clip clip = getClip(name);
        listOfAudio.add(new SoundEffect(clip));
    }
}
