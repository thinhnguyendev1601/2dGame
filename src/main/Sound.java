package main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {
    GamePanel gp;
    Clip clip;
    URL soundURL[] = new URL[30];
    float volume = 0.5f; // Volume from 0.0 to 1.0
    boolean isMuted = false;
    
    public Sound(GamePanel gp) {
        this.gp = gp;
        
        // Load sound files
        soundURL[0] = getClass().getResource("/sound/background.wav");      // Background music
        soundURL[1] = getClass().getResource("/sound/paper.wav");           // Paper sound
        soundURL[2] = getClass().getResource("/sound/chalkOnTheWall.wav");  // Chalk sound
        soundURL[3] = getClass().getResource("/sound/indoorWalk.wav");      // Indoor footsteps
        soundURL[4] = getClass().getResource("/sound/outdoorWalk.wav");     // Outdoor footsteps
        soundURL[5] = getClass().getResource("/sound/radioCrafting.wav");   // Crafting sound
    }
    
    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            
            // Set volume
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    public void play() {
        if (!isMuted) {
            clip.start();
        }
    }
    
    public void loop() {
        if (!isMuted) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    
    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
    
    public void setVolume(float volume) {
        this.volume = Math.max(0.0f, Math.min(1.0f, volume));
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(this.volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
    }
    
    public void toggleMute() {
        isMuted = !isMuted;
        if (isMuted) {
            stop();
        } else {
            play();
        }
    }
    
    public boolean isMuted() {
        return isMuted;
    }
}
