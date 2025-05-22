package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Field extends JPanel implements PropertyChangeListener {
    public static final int FIELDHEIGHT = 500;
    public static final int FIELDWIDTH = 800;
    private Ball ball;
    private Bar hostBar;
    private Bar clientBar;

    private Player hostPlayer;
    private Player clientPlayer;

    private final Font SCOREFONT = new Font("Arial", Font.BOLD, 32);
    private final Font COUNTERFONT = new Font("Arial", Font.BOLD, 64);

    private final int playerId;
    private final Publisher publisher;
    private int playerBarY = FIELDHEIGHT / 2;

    private boolean movingUp = false;
    private boolean movingDown = false;
    private int lastSentY = -1;



    public Field(int playerID, Publisher publisher){
        this.playerId = playerID;
        this.publisher = publisher;

        this.ball = new Ball(FIELDWIDTH/2, FIELDHEIGHT/2);

        this.hostBar = new Bar(FIELDWIDTH/10, FIELDHEIGHT/2);

        this.clientBar = new Bar(9 * FIELDWIDTH/10, FIELDHEIGHT/2);

        this.hostPlayer = Repository.getInstance().getHost();
        this.clientPlayer = Repository.getInstance().getClient();

        setBackground(Color.GREEN);
        setPreferredSize(new Dimension(FIELDWIDTH, FIELDHEIGHT));


        Repository.getInstance().addPropertyChangeListener(this);
        setFocusable(true);
        requestFocusInWindow();
        setupKeyBindings();

    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        g.setColor(Color.GREEN);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.WHITE);
        g.setFont(SCOREFONT);
        String scoreText = String.format("Player 1: %d      Player 2: %d",
                hostPlayer.getScore(), clientPlayer.getScore());
        int textWidth = g.getFontMetrics().stringWidth(scoreText);
        g.drawString(scoreText, (getWidth() - textWidth) /2, 50);

        hostBar.draw(g);
        clientBar.draw(g);
        ball.draw(g);

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        if (ball.isPaused() && ball.getCountdown() > 0){
            g.setColor(Color.BLACK );
            g.setFont(COUNTERFONT);

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

    private void setupKeyBindings() {
        // Key listener (low-level control)
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                int key = e.getKeyCode();
                if ((playerId == 1 && key == java.awt.event.KeyEvent.VK_W) ||
                        (playerId == 2 && key == java.awt.event.KeyEvent.VK_UP)) {
                    movingUp = true;
                }
                if ((playerId == 1 && key == java.awt.event.KeyEvent.VK_S) ||
                        (playerId == 2 && key == java.awt.event.KeyEvent.VK_DOWN)) {
                    movingDown = true;
                }
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                int key = e.getKeyCode();
                if ((playerId == 1 && key == java.awt.event.KeyEvent.VK_W) ||
                        (playerId == 2 && key == java.awt.event.KeyEvent.VK_UP)) {
                    movingUp = false;
                }
                if ((playerId == 1 && key == java.awt.event.KeyEvent.VK_S) ||
                        (playerId == 2 && key == java.awt.event.KeyEvent.VK_DOWN)) {
                    movingDown = false;
                }
            }
        });

        // Make sure key events are received
        setFocusable(true);
        requestFocusInWindow();

        // Game loop: paddle move + ball move + repaint
        new Timer(10, e -> {
            if (movingUp) movePaddle(-5);
            if (movingDown) movePaddle(5);

            ball.moveAndScore(hostPlayer, clientPlayer);
            ball.checkCollision(hostBar, clientBar);
            repaint();
        }).start();
    }


    private void movePaddle(int deltaY) {
        playerBarY = Math.max(0, Math.min(FIELDHEIGHT - 50, playerBarY + deltaY));

        if (playerBarY != lastSentY) {
            try {
                publisher.sendPaddleMove(playerBarY);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            lastSentY = playerBarY;
        }

        if (playerId == 1) {
            hostPlayer.setBarPos(playerBarY);
        } else {
            clientPlayer.setBarPos(playerBarY);
        }
    }

}
