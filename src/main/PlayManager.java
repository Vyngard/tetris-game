package main;

import mino.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/*
Draws the play area
Manages tetrominoes
Handles Gameplay actions (deleting a line, adding scores, etc.)
 */
public class PlayManager {
    // main.Main play area

    // Since each block is 30X30, then we will have 12 blocks horizontally and 20 block vertically
    final int WIDTH = 360;
    final int HEIGHT = 600;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    // Mino
    Mino currentMino;
    final int MINO_START_X;
    final int MINO_START_Y;
    Mino nextMino;
    final int NEXTMINO_X;
    final int NEXTMINO_Y;
    public static ArrayList<Block> staticBlocks = new ArrayList<Block>();

    // Others
    public static int dropInterval = 60; // mino drops in every 60 frames (1 second)
    boolean gameOver;

    // effect
    boolean effectCounterOn;
    int effectCounter;
    ArrayList<Integer> effectY = new ArrayList<Integer>();

    // score
    int level = 1;
    int lines;
    int score;


    public PlayManager() {
        // main.Main play area frame
        left_x = (GamePanel.WIDTH / 2) - (WIDTH / 2); // 1280/2 - 360/2 = 460
        right_x = left_x + WIDTH;
        top_y = 50; // 50 pixels
        bottom_y = top_y + HEIGHT;

        // starting point of a mino in the screen (around the center)
        MINO_START_X = left_x + (WIDTH / 2) - Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;

        // specifying the next mino position
        NEXTMINO_X = right_x + 175;
        NEXTMINO_Y = top_y + 500;

        // set the starting mino
        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);

        // set the next mino
        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
    }

    private Mino pickMino() {
        // Pick a random Mino
        Mino mino = null;
        int i = new Random().nextInt(7);

        switch (i) {
            case 0:
                mino = new Mino_L1();
                break;
            case 1:
                mino = new Mino_L2();
                break;
            case 2:
                mino = new Mino_Square();
                break;
            case 3:
                mino = new Mino_Bar();
                break;
            case 4:
                mino = new Mino_T();
                break;
            case 5:
                mino = new Mino_Z1();
                break;
            case 6:
                mino = new Mino_Z2();
                break;
        }

        return mino;
    }

    public void update() {

        // check if the currentMino is active
        if (!currentMino.active) {
            // if the current mino is not active, put it into the staticBlocks
            staticBlocks.add(currentMino.b[0]);
            staticBlocks.add(currentMino.b[1]);
            staticBlocks.add(currentMino.b[2]);
            staticBlocks.add(currentMino.b[3]);

            // check if the game is over
            if (currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y) {
                // this means currentMino collided a block and couldn't move at all
                // so its xy are the same with the nextMino
                gameOver = true;
                GamePanel.music.stop(); // stop the background music
                GamePanel.soundEffect.play(2, false); // play sound for game over
            }

            currentMino.deactivating = false;

            // replace the currentMino with nextMino, and pick a new nextMino
            currentMino = nextMino;
            currentMino.setXY(MINO_START_X, MINO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

            // when a mino becomes inactive, check if there is a line to delete
            checkDelete();
        } else {
            currentMino.update();
        }
    }

    private void checkDelete() {
        int x = left_x;
        int y = top_y;
        int blockCount = 0;
        int lineCount = 0;

        while (x < right_x && y < bottom_y) {

            for (int i = 0; i < staticBlocks.size(); i++) {
                if (staticBlocks.get(i).x == x && staticBlocks.get(i).y == y) {
                    // if there is a block in the current position
                    blockCount++;
                }
            }

            x += Block.SIZE;

            if (x == right_x) {

                // if there is a line, we delete it
                if (blockCount == 12) {

                    effectCounterOn = true;
                    effectY.add(y);

                    for (int i = staticBlocks.size() - 1; i >= 0; i--) {
                        // delete the blocks in the current line
                        if (staticBlocks.get(i).y == y) {
                            staticBlocks.remove(i);
                        }
                    }


                    lineCount++;
                    lines++;

                    // Drop Speed: if the line score hits a certain number, increase drop speed, and level up
                    // the
                    if (lines % 10 == 0 && dropInterval > 1) {

                        level++;
                        if (dropInterval > 10) {
                            dropInterval -= 10;
                        } else {
                            dropInterval--;
                        }
                    }

                    // a line has been deleted. so we need to move the blocks above the deleted line down
                    for (int i = 0; i < staticBlocks.size(); i++) {
                        // move the blocks above the deleted line down
                        if (staticBlocks.get(i).y < y) {
                            staticBlocks.get(i).y += Block.SIZE;
                        }
                    }
                }
                blockCount = 0;
                x = left_x;
                y += Block.SIZE;
            }
        }
        // calculate score
        if (lineCount > 0) {
            GamePanel.soundEffect.play(1, false); // play sound for deleting line
            int singleLineScore = 10 * level;
            score += singleLineScore * lineCount;
        }
    }

    public void draw(Graphics2D g2) {
        // Draw play area frame
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x - 4, top_y - 4, WIDTH + 8, HEIGHT + 8);

        // Draw next mino frame
        int x = right_x + 100;
        int y = bottom_y - 200;
        g2.drawRect(x, y, 200, 200);
        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x + 60, y + 60);

        // draw score frame
        g2.drawRect(x, top_y, 250, 300);
        x += 40;
        y = top_y + 90;
        g2.drawString("LEVEL: " + level, x, y);
        y+= 70;
        g2.drawString("LINES: " + lines, x, y);
        y+= 70;
        g2.drawString("SCORE: " + score, x, y);

        // Draw the currentMino
        if (currentMino != null) {
            currentMino.draw(g2);
        }

        // draw the next mino
        nextMino.draw(g2);

        // draw the staticBlocks
        for (int i = 0; i < staticBlocks.size(); i++) {
            staticBlocks.get(i).draw(g2);
        }

        // draw effect
        if (effectCounterOn) {
            effectCounter++;

            g2.setColor(Color.red);
            for (int i = 0; i < effectY.size(); i++) {
                g2.fillRect(left_x, effectY.get(i), WIDTH, Block.SIZE);
            }

            if (effectCounter == 10) {
                effectCounterOn = false;
                effectCounter = 0;
                effectY.clear();
            }
        }

        // draw pause or game over
        g2.setColor(Color.yellow);
        g2.setFont(g2.getFont().deriveFont(50f));
        if (gameOver) {
            x = left_x + 25;
            y = top_y + 320;
            g2.drawString("GAME OVER", x, y);
        } else if (KeyHandler.pausePressed) {
            x = left_x + 70;
            y = top_y + 320;
            g2.drawString("PAUSED", x, y);
        }

        // draw the game title
        x = 25;
        y = top_y + 320;
        g2.setColor(Color.white);
        g2.setFont(new Font("Times New Roman", Font.ITALIC, 60));
        g2.drawString("SIMPLE TETRIS", x, y);

    }

}
