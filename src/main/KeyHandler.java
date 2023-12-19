// get what key has been pressed

package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public static boolean upPressed, downPressed, rightPressed, leftPressed, pausePressed;


    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        // check what key has been pressed and change the flag of related key (WASD keys)
        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        // using space key for pausing the game
        if (code == KeyEvent.VK_SPACE) {
            if (pausePressed) {
                pausePressed = false;
                GamePanel.music.play(0 ,true); // start the background music
                GamePanel.music.loop();
            } else {
                pausePressed = true;
                GamePanel.music.stop(); // stop the background music
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
