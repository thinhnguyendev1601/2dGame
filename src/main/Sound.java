package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[30];
    FloatControl fc;
    int volumeScale = 3;
    float volume;

    public Sound()
    {
        soundURL[0] = getClass().getResource("/sound/background.wav");
        soundURL[1] = getClass().getResource("/sound/paper.wav");
        soundURL[2] = getClass().getResource("/sound/chalkOnTheWall.wav");
        soundURL[3] = getClass().getResource("/sound/indoorWalk.wav");
        soundURL[4] = getClass().getResource("/sound/outdoorWalk.wav");
        soundURL[5] = getClass().getResource("/sound/radioCrafting.wav")
    }

    public void setFile(int i)           // Java Sound File Opening Format
    {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN); //pass value for clip // -80f to 6f // 6 is max. -80f = 0
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        checkVolume();
    }

    public void play()
    {
        clip.start();
    }

    public void loop()
    {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop()
    {
        clip.stop();
    }

    public void checkVolume()
    {
        switch (volumeScale)
        {
            case 0 : volume = -80f; break;
            case 1 : volume = -20f; break;
            case 2 : volume = -12f; break;
            case 3 : volume = -5f; break;
            case 4 : volume = 1f; break;
            case 5 : volume = 6f; break;
        }
        fc.setValue(volume);
    }
}
