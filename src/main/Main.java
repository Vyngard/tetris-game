package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame("Simple Tetris");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //discarding windows properly
        window.setResizable(false); // user cannot change the window size

        // add game panel to our window
        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack(); // the size of the main.GamePanel will be as the same size of the window

        window.setLocationRelativeTo(null); // windows show at the center of screen
        window.setVisible(true);

        gp.launchGame();


    }
}