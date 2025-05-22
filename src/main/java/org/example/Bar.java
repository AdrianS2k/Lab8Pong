package org.example;

import java.awt.*;

public class Bar {
    private int x;
    private int y;
    private static final int height = 50;
    private static final int width = 10;

    public Bar(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void draw(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(x, y, width, height);
    }

    public void setY(int y){
        this.y = y;
    }
    public int getY(){
        return this.y;
    }
    public int getX(){
        return this.x;
    }
}
