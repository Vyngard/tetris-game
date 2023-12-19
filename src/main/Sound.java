package main;

import javax.sound.sampled.*;
import javax.sound.sampled.LineEvent.Type;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class Sound {
    Clip musicClip;
    URL url[] = new URL[10];

    public  Sound() {
        try {
            url[0] = new File("./res/white labyrinth.wav").toURI().toURL();
            url[1] = new File("./res/delete line.wav").toURI().toURL();
            url[2] = new File("./res/game over.wav").toURI().toURL();
            url[3] = new File("./res/rotation.wav").toURI().toURL();
            url[4] = new File("./res/touch floor.wav").toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public void play(int i, boolean music) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url[i]);
            Clip clip = AudioSystem.getClip();

            if (music) {
                musicClip = clip;
            }

            clip.open(audioInputStream);
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == Type.STOP) {
                        clip.close();
                    }
                }
            });

            audioInputStream.close();
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loop() {
        musicClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        musicClip.stop();
        musicClip.close();
    }


}
