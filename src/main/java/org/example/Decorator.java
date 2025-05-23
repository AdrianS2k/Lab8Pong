package org.example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Decorator class that overlays a celebration when a player reaches a score of 3.
 * Wraps a Field and listens for score changes.
 */
public class Decorator extends JPanel implements PropertyChangeListener {
    private final Field field;
    private boolean showCelebration = false;
    private String winnerText = "";
    private Timer celebrationTimer; // Add this

    public Decorator(Field field) {
        this.field = field;
        setLayout(new BorderLayout());
        add(field, BorderLayout.CENTER);

        // Listen for score changes
        Repository.getInstance().addPropertyChangeListener(this);
        setOpaque(false); // Decorator should be transparent
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); // Paints children (including Field)

        if (showCelebration) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(255, 215, 0, 180));
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 72));
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(winnerText);
            int x = (getWidth() - textWidth) / 2;
            int y = getHeight() / 2;
            g2.drawString(winnerText, x, y);

            g2.setFont(new Font("Arial", Font.BOLD, 36));
            String sub = "🎉 Congratulations! 🎉";
            int subWidth = g2.getFontMetrics().stringWidth(sub);
            g2.drawString(sub, (getWidth() - subWidth) / 2, y + 60);

            g2.dispose();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (showCelebration) return;

        if ("HostScore".equals(evt.getPropertyName()) || "ClientScore".equals(evt.getPropertyName())) {
            Player.Host host = Repository.getInstance().getHost();
            Player.Client client = Repository.getInstance().getClient();

            if (host.getScore() >= 3) {
                showCelebration = true;
                winnerText = "Player 1 Wins!";
                repaint();
                startCelebrationTimer();
            } else if (client.getScore() >= 3) {
                showCelebration = true;
                winnerText = "Player 2 Wins!";
                repaint();
                startCelebrationTimer();
            }
        }
    }

    private void startCelebrationTimer() {
        if (celebrationTimer != null && celebrationTimer.isRunning()) {
            celebrationTimer.stop();
        }
        celebrationTimer = new Timer(3000, e -> { // 3000 ms = 3 seconds
            showCelebration = false;
            repaint();
        });
        celebrationTimer.setRepeats(false);
        celebrationTimer.start();
    }

    public boolean isCelebrating() {
        return showCelebration;
    }
}