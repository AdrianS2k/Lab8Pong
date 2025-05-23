package org.example;


import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JTabbedPane;
/**
 * The {@code GameHost} class is the entry point for launching the host player in the Pong game.
 * It sets up the game window using Java Swing, initializes the MQTT {@code Publisher} and {@code Subscriber},
 * and manages both gameplay and chat interface components.
 *
 * The host player is assigned player ID 1 by default but can be set via command-line arguments.
 * A {@code JTabbedPane} is used to switch between the game field and chat panel, and a layered pane
 * is used to allow decorative overlays (e.g., score-based effects).
 *
 * This class also attaches a shutdown hook to cleanly disconnect the MQTT publisher on exit.
 * T4b
 * @author Adrian
 * @version 1.0
 */
public class T4b_GameHost {
    public static void main(String[] args) throws Exception {
        int playerId = 1; // Default to host

        if (args.length > 0) {
            playerId = Integer.parseInt(args[0]);
        }

        T4b_Publisher pub = new T4b_Publisher(playerId);
        T4b_Subscriber sub = new T4b_Subscriber(playerId);

        JFrame frame = new JFrame("Pong Test - Player " + playerId);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        T4b_Field field = new T4b_Field(playerId, pub);
        T4b_Decorator decoratedField = new T4b_Decorator(field);

        T4b_ChatPanel chatPanel = new T4b_ChatPanel(pub);
        sub.addChatListener(chatPanel::appendMessage);

        // Use a JLayeredPane for overlay
        JLayeredPane layeredPane = new JLayeredPane();
        field.setBounds(0, 0, T4b_Field.FIELDWIDTH, T4b_Field.FIELDHEIGHT);
        decoratedField.setBounds(0, 0, T4b_Field.FIELDWIDTH, T4b_Field.FIELDHEIGHT);

        layeredPane.setPreferredSize(new java.awt.Dimension(T4b_Field.FIELDWIDTH, T4b_Field.FIELDHEIGHT));
        layeredPane.add(field, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(decoratedField, JLayeredPane.PALETTE_LAYER);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Play", layeredPane);
        tabs.addTab("Chat", chatPanel);

        //frame.setContentPane(layeredPane);
        frame.setContentPane(tabs);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        field.requestFocusInWindow();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try { pub.disconnect(); } catch (Exception ignored) {}
        }));
    }
}