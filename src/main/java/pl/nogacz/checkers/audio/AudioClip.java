package pl.nogacz.checkers.audio;

import javax.sound.sampled.Clip;

public abstract class AudioClip {
    private final Clip clip;

    public AudioClip(Clip clip){
        this.clip = clip;
        clip.start();
    }

    public boolean isFinished(){
        return !clip.isRunning();
    }

    public void clean(){
        clip.close();
    }
}
