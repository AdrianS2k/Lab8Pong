package org.example;

import org.w3c.dom.css.Rect;

import java.awt.*;

public class Ball {

    private int x;
    private int y;
    private int dx = 3;
    private int dy = 2;
    private int SIZE = 10;

    public Ball(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g){
        g.setColor(Color.BLACK);
        g.fillOval(x, y, SIZE, SIZE);
    }

    public void move(){
        x += dx;
        y += dy;

        if (y <= 0 || y >= 400 - SIZE) dy *= -1;
    }
    public void checkCollision(Bar hostbar, Bar clientbar){
        Rectangle ballRect = new Rectangle(x, y , SIZE, SIZE);
        Rectangle hostRect = new Rectangle(hostbar.getX(), hostbar.getY(), 10, 50);
        Rectangle clientRect = new Rectangle(clientbar.getX(), clientbar.getY(), 10, 50);

        if (ballRect.intersects(hostRect) || ballRect.intersects(clientRect)) {
            dx *= -1;
        }
    }
}
