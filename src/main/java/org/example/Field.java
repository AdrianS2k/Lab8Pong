package org.example;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Field extends JPanel implements PropertyChangeListener {
    public static final int FIELDHEIGHT = 500;
    public static final int FIELDWIDTH = 800;
    private Ball ball;
    private Bar hostBar;
    private Bar clientBar;


    public Field(){
        this.ball = new Ball(FIELDWIDTH/2, FIELDHEIGHT/2);

        this.hostBar = new Bar(FIELDWIDTH/10, FIELDHEIGHT/2);

        this.clientBar = new Bar(9 * FIELDWIDTH/10, FIELDHEIGHT/2);

        setBackground(Color.GREEN);

        setPreferredSize(new Dimension(FIELDWIDTH, FIELDHEIGHT));
        new Timer(10, e -> {
            ball.move();
            ball.checkCollision(hostBar, clientBar);
            repaint();
        }).start();

        // Repository.getInstance().addPropertyChangeListener(this);

    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        g.setColor(Color.GREEN);
        g.fillRect(0, 0, getWidth(), getHeight());


        hostBar.draw(g);
        clientBar.draw(g);
        ball.draw(g);

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        if (ball.isPaused() && ball.getCountdown() > 0){
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 64));

            String text = Integer.toString(ball.getCountdown());
            int textwidth = g.getFontMetrics().stringWidth(text);
            g.drawString(text, (FIELDWIDTH / 2) - (textwidth/2), FIELDHEIGHT / 10 );
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if ("HostBarPosition".equals(evt.getPropertyName())){
            hostBar.setY((int) evt.getNewValue());
        } else if ("ClientBarPosition".equals(evt.getPropertyName())){
            clientBar.setY((int) evt.getNewValue());
        }
        repaint();
    }
}
