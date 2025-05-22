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

/**
 * Decorator class that overlays a celebration when a player reaches a score of 3.
 * Wraps a Field and listens for score changes.
 */
public class Decorator extends JPanel implements PropertyChangeListener {
    private final Field field;
    private boolean showCelebration = false;
    private String winnerText = "";

    public Decorator(Field field) {
        this.field = field;
        setLayout(new BorderLayout());
        add(field, BorderLayout.CENTER);

        // Listen for score changes
        Repository.getInstance().addPropertyChangeListener(this);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Let the field paint itself
        // (Field is a child component, so it paints automatically)

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
        if ("HostBarPosition".equals(evt.getPropertyName()) || "ClientBarPosition".equals(evt.getPropertyName())) {
            // Ignore bar position changes
            return;
        }
        Player.Host host = Repository.getInstance().getHost();
        Player.Client client = Repository.getInstance().getClient();

        if (host.getScore() >= 3) {
            showCelebration = true;
            winnerText = "Player 1 Wins!";
            repaint();
        } else if (client.getScore() >= 3) {
            showCelebration = true;
            winnerText = "Player 2 Wins!";
            repaint();
        }
    }

    public boolean isCelebrating() {
        return showCelebration;
    }
}