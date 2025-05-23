package org.example;


import javax.swing.*;
import org.eclipse.paho.client.mqttv3.MqttException;

public class GameHost {
    public static void main(String[] args) throws Exception {
        int playerId = 1; // Default to host

        if (args.length > 0) {
            playerId = Integer.parseInt(args[0]);
        }

        Publisher pub = new Publisher(playerId);
        Subscriber sub = new Subscriber(playerId);

        JFrame frame = new JFrame("Pong Test - Player " + playerId);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        Field field = new Field(playerId, pub);
        Decorator decoratedField = new Decorator(field);

        ChatPanel chatPanel = new ChatPanel(pub);
        sub.addChatListener(chatPanel::appendMessage);

        // Use a JLayeredPane for overlay
        JLayeredPane layeredPane = new JLayeredPane();
        field.setBounds(0, 0, Field.FIELDWIDTH, Field.FIELDHEIGHT);
        decoratedField.setBounds(0, 0, Field.FIELDWIDTH, Field.FIELDHEIGHT);

        layeredPane.setPreferredSize(new java.awt.Dimension(Field.FIELDWIDTH, Field.FIELDHEIGHT));
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