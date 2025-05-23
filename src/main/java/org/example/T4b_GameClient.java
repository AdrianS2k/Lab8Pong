package org.example;

/**
 * Client (Player 2) that brings together Publisher and Subscriber:
 * connects to the broker, manages callbacks, and provides
 * sendPaddleMove() / sendChat() for the game logic to call.
 * T4b
 * @author Aidan Stutz
 */

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JTabbedPane;

public class T4b_GameClient {
    public static void main(String[] args) throws Exception {
        int playerId = 2; //Default to host

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
