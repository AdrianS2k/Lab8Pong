package org.example;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * The {@code Ball} class represents the ball used in the Pong game.
 * It handles movement, collision detection, scoring, and visual rendering of the ball.
 * T4b
 * @author Adrian
 * @version 1.0
 */
public class T4b_Ball {

    private double x;
    private double y;
    private double dx = 3.0;
    private double dy = 2.0;
    private static final int SIZE = 10; // constant in uppercase

    private boolean paused = false;
    private int countdown = 0;

    public T4b_Ball(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g){
        g.setColor(Color.BLUE);
        g.fillOval((int)x, (int)y, SIZE, SIZE);
    }

    public boolean moveAndScore(T4b_Player host, T4b_Player client){
        if (paused) return false;
        x += dx;
        y += dy;

        if (y <= 0 || y >= T4b_Field.FIELDHEIGHT - SIZE) dy *= -1;
        if (x < 0){
            T4b_Repository.getInstance().addClientScore(1);
            pauseAndReset();
            dx = -dx * 1.1;
            return true;
        }
        if (x > T4b_Field.FIELDWIDTH - SIZE) {
            T4b_Repository.getInstance().addHostScore(1);
            pauseAndReset();
            dx = -dx * 1.1;
            return true;
        }
        return false;
    }

    public void checkCollision(T4b_Bar hostBar, T4b_Bar clientBar){
        Rectangle ballRect = new Rectangle((int)x, (int)y, SIZE, SIZE);
        Rectangle hostRect = new Rectangle(hostBar.getX(), hostBar.getY(), T4b_Bar.WIDTH, T4b_Bar.HEIGHT);
        Rectangle clientRect = new Rectangle(clientBar.getX(), clientBar.getY(), T4b_Bar.WIDTH, T4b_Bar.HEIGHT);

        if (ballRect.intersects(hostRect) || ballRect.intersects(clientRect)) {
            dx *= -1;
        }
    }

    public void reset(){
        x = (T4b_Field.FIELDWIDTH - SIZE) / 2;
        y = (T4b_Field.FIELDHEIGHT - SIZE) / 2;
    }

    public void pauseAndReset(){
        paused = true;
        reset();
        countdown = 3;
        new javax.swing.Timer(1500, e -> {
            countdown--;
            if (countdown <= 0){
                paused = false;
                ((javax.swing.Timer)e.getSource()).stop();
            }
        }).start();
    }

    public int getCountdown(){ return countdown; }
    public boolean isPaused(){ return paused; }
}

