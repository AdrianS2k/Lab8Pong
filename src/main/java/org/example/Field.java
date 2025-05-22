package org.example;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Field extends JPanel implements PropertyChangeListener {
    public int FIELDSIZE = 500;
    public int ballLocation = 250;
    private Ball ball;
    private Bar hostBar;
    private Bar clientBar;

    public Field(){
        this.ball = new Ball(150, 150);
        this.hostBar = new Bar(20, 100);
        this.clientBar = new Bar(560, 100);

        setPreferredSize(new Dimension(600, 400));
        setBackground(Color.GREEN);

        // Repository.getInstance().addPropertyChangeListener(this);

    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        hostBar.draw(g);
        clientBar.draw(g);
        ball.draw(g);
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
