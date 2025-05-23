package org.example;

import java.awt.*;
/**
 * The {@code Bar} class represents a paddle in the Pong game.
 * It defines the paddle's position and provides methods for drawing and updating its location.
 * Each paddle has a fixed width and height and is rendered as a black rectangle.
 *
 * This class is used for both the host and client player's paddle.
 *T4b
 * @author Adrian
 * @version 1.0
 */
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
