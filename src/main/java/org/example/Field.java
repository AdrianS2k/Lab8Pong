package org.example;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.Timer;
/**
 * The {@code Bar} class represents a paddle in the Pong game.
 * It defines the paddle's position and provides methods for drawing and updating its location.
 * Each paddle has a fixed width and height and is rendered as a black rectangle.
 *
 * This class is used for both the host and client player's paddle.
 * T4b
 * @author Adrian
 * @version 1.0
 */
public class Field extends JPanel implements PropertyChangeListener {
    public static final int FIELDHEIGHT = 500;
    public static final int FIELDWIDTH = 800;

    private final Ball ball;
    private final Bar hostBar;
    private final Bar clientBar;
    private final Player hostPlayer;
    private final Player clientPlayer;

    private final Font scoreFont = new Font("Arial", Font.BOLD, 32);
    private final Font counterFont = new Font("Arial", Font.BOLD, 64);

    private final int playerId;
    private final Publisher publisher;
    private int playerBarY = FIELDHEIGHT / 2;

    private boolean movingUp = false;
    private boolean movingDown = false;
    private int lastSentY = -1;

    private int lastHostScore;
    private int lastClientScore;

    public Field(int playerId, Publisher publisher) {
        this.playerId = playerId;
        this.publisher = publisher;

        this.ball = new Ball(FIELDWIDTH / 2, FIELDHEIGHT / 2);
        this.hostBar = new Bar(FIELDWIDTH / 10, FIELDHEIGHT / 2);
        this.clientBar = new Bar(9 * FIELDWIDTH / 10, FIELDHEIGHT / 2);

        this.hostPlayer = Repository.getInstance().getHost();
        this.clientPlayer = Repository.getInstance().getClient();

        this.lastHostScore   = hostPlayer.getScore();
        this.lastClientScore = clientPlayer.getScore();

        setBackground(Color.GREEN);
        setPreferredSize(new Dimension(FIELDWIDTH, FIELDHEIGHT));
        Repository.getInstance().addPropertyChangeListener(this);

        setFocusable(true);
        requestFocusInWindow();
        setupKeyBindings();
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        hostBar.draw(g);
        clientBar.draw(g);
        ball.draw(g);

        g.setColor(Color.WHITE);
        g.setFont(scoreFont);
        String scoreText = String.format("P1: %d    P2: %d", hostPlayer.getScore(), clientPlayer.getScore());
        int textWidth = g.getFontMetrics().stringWidth(scoreText);
        g.drawString(scoreText, (getWidth() - textWidth) / 2, 50);

        if (ball.isPaused() && ball.getCountdown() > 0) {
            g.setFont(counterFont);
            String text = Integer.toString(ball.getCountdown());
            int textW = g.getFontMetrics().stringWidth(text);
            g.drawString(text, (FIELDWIDTH - textW) / 2, FIELDHEIGHT / 10);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("HostBarPosition".equals(evt.getPropertyName())) {
            hostBar.setY((int) evt.getNewValue());
        } else if ("ClientBarPosition".equals(evt.getPropertyName())) {
            clientBar.setY((int) evt.getNewValue());
        }
        repaint();
    }

    private void setupKeyBindings() {
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent e) {
                int k = e.getKeyCode();
                if ((playerId == 1 && k == KeyEvent.VK_W) || (playerId == 2 && k == KeyEvent.VK_UP)) movingUp = true;
                if ((playerId == 1 && k == KeyEvent.VK_S) || (playerId == 2 && k == KeyEvent.VK_DOWN)) movingDown = true;
            }
            public void keyReleased(java.awt.event.KeyEvent e) {
                int k = e.getKeyCode();
                if ((playerId == 1 && k == KeyEvent.VK_W) || (playerId == 2 && k == KeyEvent.VK_UP)) movingUp = false;
                if ((playerId == 1 && k == KeyEvent.VK_S) || (playerId == 2 && k == KeyEvent.VK_DOWN)) movingDown = false;
            }
        });

        new Timer(10, e -> {
            if (movingUp) movePaddle(-5);
            if (movingDown) movePaddle(5);
            ball.moveAndScore(hostPlayer, clientPlayer);
            ball.checkCollision(hostBar, clientBar);

            if (playerId == 1) {
                int currH = hostPlayer.getScore();
                int currC = clientPlayer.getScore();
                if (currH != lastHostScore || currC != lastClientScore) {
                    try {
                        publisher.sendScore(currH, currC);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    lastHostScore   = currH;
                    lastClientScore = currC;
                }
            }
            repaint();
        }).start();
    }

    private void movePaddle(int deltaY) {
        playerBarY = Math.max(0, Math.min(FIELDHEIGHT - Bar.HEIGHT, playerBarY + deltaY));

        if (playerBarY != lastSentY) {
            try { publisher.sendPaddleMove(playerBarY); } catch (Exception ignored) {}
            lastSentY = playerBarY;
        }
        if (playerId == 1) {
            hostPlayer.setBarPos(playerBarY);
            hostBar.setY(playerBarY);
        } else {
            clientPlayer.setBarPos(playerBarY);
            clientBar.setY(playerBarY);
        }
    }
}