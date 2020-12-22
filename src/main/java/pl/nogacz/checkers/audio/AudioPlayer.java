package pl.nogacz.checkers.audio;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {
    java.util.logging.Logger logger =  java.util.logging.Logger.getLogger(this.getClass().getName());
    private static AudioPlayer instance = null;
    private List<AudioClip> listOfAudio;
    private final static String BACKGROUND_MUSIC_FILE_NAME = "background2.wav";
    private final static String GAME_WON_SOUND_EFFECT_FILE_NAME = "gameWon.wav";
    private final static String GAME_LOST_SOUND_EFFECT_FILE_NAME = "gameLost.wav";
    private final static String PIECE_WON_SOUND_EFFECT_FILE_NAME = "pieceWon.wav";
    private final static String PIECE_LOST_SOUND_EFFECT_FILE_NAME = "pieceLost.wav";
    private final static String GAME_START_SOUND_EFFECT_FILE_NAME = "gameStart.wav";
    private final static String MENU_SOUND_EFFECT_FILE_NAME = "menu.wav";

    private AudioPlayer(){
        listOfAudio = new ArrayList<>();
    }
    
    public static AudioPlayer getInstance(){
        if (instance == null) {
            instance = new AudioPlayer();
          }
          return instance;
    }

    private Clip getClip(String name){
        final URL file = AudioPlayer.class.getResource("/sounds/"+name);
        try(AudioInputStream inStream = AudioSystem.getAudioInputStream(file)){
            final Clip clip = AudioSystem.getClip();
            clip.open(inStream);
            clip.setMicrosecondPosition(0);
            return clip;
        } catch(UnsupportedAudioFileException e){
            logger.log(Level.SEVERE, "an exception was thrown in AudioPlayer, getClip()", e);
        } catch(IOException e){
            logger.log(Level.SEVERE, "an exception was thrown in AudioPlayer, getClip()", e);
        } catch(LineUnavailableException e){
            logger.log(Level.SEVERE, "an exception was thrown in AudioPlayer, getClip()", e);
        }
        return null;
    }

    public void playBackgroundMusic(){
        final Clip clip = getClip(BACKGROUND_MUSIC_FILE_NAME);
        listOfAudio.add(new BackgroundMusic(clip));
    }

    private void playSoundEffect(String name){
        final Clip clip = getClip(name);
        listOfAudio.add(new SoundEffect(clip));
    }

    public void playMenuEffect(){
        playSoundEffect(MENU_SOUND_EFFECT_FILE_NAME);
    }

    public void playGameStartEffect(){
        playSoundEffect(GAME_START_SOUND_EFFECT_FILE_NAME);
    }

    public void playPieceWonEffect(){
        playSoundEffect(PIECE_WON_SOUND_EFFECT_FILE_NAME);
    }

    public void playPieceLostEffect(){
        playSoundEffect(PIECE_LOST_SOUND_EFFECT_FILE_NAME);
    }

    public void playGameWonEffect(){
        playSoundEffect(GAME_WON_SOUND_EFFECT_FILE_NAME);
    }

    public void playGameLostEffect(){
        playSoundEffect(GAME_LOST_SOUND_EFFECT_FILE_NAME);
    }
}
