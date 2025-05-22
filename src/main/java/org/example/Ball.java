package org.example;

import java.awt.*;

public class Ball {

    private double x;
    private double y;
    private double dx = 3.0;
    private double dy = 2.0;
    private static final int SIZE = 10; // constant in uppercase

    private boolean paused = false;
    private int countdown = 0;

    public Ball(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g){
        g.setColor(Color.BLUE);
        g.fillOval((int)x, (int)y, SIZE, SIZE);
    }

    public boolean moveAndScore(Player host, Player client){
        if (paused) return false;
        x += dx;
        y += dy;

        if (y <= 0 || y >= Field.FIELDHEIGHT - SIZE) dy *= -1;
        if (x < 0){
            client.addScore(1);
            pauseAndReset();
            dx = -dx * 1.1;
            return true;
        }
        if (x > Field.FIELDWIDTH - SIZE) {
            host.addScore(1);
            pauseAndReset();
            dx = -dx * 1.1;
            return true;
        }
        return false;
    }

    public void checkCollision(Bar hostBar, Bar clientBar){
        Rectangle ballRect = new Rectangle((int)x, (int)y, SIZE, SIZE);
        Rectangle hostRect = new Rectangle(hostBar.getX(), hostBar.getY(), Bar.WIDTH, Bar.HEIGHT);
        Rectangle clientRect = new Rectangle(clientBar.getX(), clientBar.getY(), Bar.WIDTH, Bar.HEIGHT);

        if (ballRect.intersects(hostRect) || ballRect.intersects(clientRect)) {
            dx *= -1;
        }
    }

    public void reset(){
        x = (Field.FIELDWIDTH - SIZE) / 2;
        y = (Field.FIELDHEIGHT - SIZE) / 2;
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

