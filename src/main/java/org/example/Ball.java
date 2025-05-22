package org.example;

import org.w3c.dom.css.Rect;

import java.awt.*;

public class Ball {

    private int x;
    private int y;
    private double dx = 3.0;
    private double dy = 2.0;
    private int SIZE = 10;

    private boolean paused = false;
    private int countdown = 0;

    public Ball(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g){
        g.setColor(Color.BLUE);
        g.fillOval(x, y, SIZE, SIZE);
    }

    public boolean moveAndScore(Player host, Player client){
        if (paused) return false;
        x += (int) dx;
        y += (int) dy;

        if (y <= 0 || y >= Field.FIELDHEIGHT - SIZE) dy *= -1;
        if (x < 0){
            client.addScore(1);
            pauseAndReset();
            dx *= -1;
            dx *= 1.1;
            return true;
        }
        if (x > Field.FIELDWIDTH - SIZE) {
            host.addScore(1);
            pauseAndReset();
            dx *= -1;
            dx *= 1.1;
            return true;
        }
        return false;
    }
    public void checkCollision(Bar hostbar, Bar clientbar){
        Rectangle ballRect = new Rectangle(x, y , SIZE, SIZE);
        Rectangle hostRect = new Rectangle(hostbar.getX(), hostbar.getY(), 10, 50);
        Rectangle clientRect = new Rectangle(clientbar.getX(), clientbar.getY(), 10, 50);

        if (ballRect.intersects(hostRect) || ballRect.intersects(clientRect)) {
            dx *= -1;
        }
    }
    public void reset(){
        x = Field.FIELDWIDTH / 2 - SIZE / 2;
        y = Field.FIELDHEIGHT / 2 - SIZE / 2;

    }
    public void pauseAndReset(){
        paused = true;
        reset();
        countdown = 3;
        new javax.swing.Timer(1500, new java.awt.event.ActionListener(){
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e){
                countdown --;
                if (countdown <= 0){
                    paused = false;
                    ((javax.swing.Timer) e.getSource()).stop();
                }
            }
        }).start();

    }
    public int getCountdown(){
        return countdown;
    }
    public boolean isPaused(){
        return paused;
    }

}
