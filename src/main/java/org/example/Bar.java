package org.example;

import java.awt.*;

public class Bar {
    public static final int WIDTH = 10;
    public static final int HEIGHT = 50;

    private int x;
    private int y;

    public Bar(int x, int y){ this.x = x; this.y = y; }

    public void draw(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(x, y, WIDTH, HEIGHT);
    }

    public void setY(int y){ this.y = y; }
    public int getY(){ return y; }
    public int getX(){ return x; }
}
