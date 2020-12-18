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
    private static AudioPlayer instance = null;
    private List<AudioClip> listOfAudio;
    private String BACKGROUND_MUSIC_FILE_NAME = "background2.wav";
    private String GAME_WON_SOUND_EFFECT_FILE_NAME = "gameWon.wav";
    private String GAME_LOST_SOUND_EFFECT_FILE_NAME = "gameLost.wav";
    private String PIECE_WON_SOUND_EFFECT_FILE_NAME = "pieceWon.wav";
    private String PIECE_LOST_SOUND_EFFECT_FILE_NAME = "pieceLost.wav";
    private String GAME_START_SOUND_EFFECT_FILE_NAME = "gameStart.wav";
    private String MENU_SOUND_EFFECT_FILE_NAME = "menu.wav";

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
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        } catch(LineUnavailableException e){
            e.printStackTrace();
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
