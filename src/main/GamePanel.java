package main;

import javax.swing.*;
import java.awt.*;
import java.security.Key;

public class GamePanel extends JPanel implements Runnable {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    final int FPS = 60;
    Thread gameThread; // to run game loop (60 FPS)
    PlayManager pm;
    public static Sound music = new Sound();
    public static Sound soundEffect = new Sound();

    public GamePanel() {
        // Panel Setting
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        this.setBackground(Color.black);
        this.setLayout(null);

        // Key Listener
        this.addKeyListener(new KeyHandler());
        this.setFocusable(true);

        // Others
        pm = new PlayManager();
    }

    // launching our game
    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start(); // .start will automatically calls run() method

        music.play(0, true);
        music.loop();
    }

    @Override
    public void run() {
        // Game Loop
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }

        }

    }

    // update object position, score, etc. that needs updating
    private void update() {
        if (!KeyHandler.pausePressed && !pm.gameOver) {
            pm.update();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        pm.draw(g2);
    }
}
