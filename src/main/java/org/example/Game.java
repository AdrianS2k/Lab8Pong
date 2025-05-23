package org.example;


import javax.swing.*;

public class Game {
    public static void main(String[] args) throws Exception {
        int playerId = 1; // Default to host

        if (args.length > 0) {
            playerId = Integer.parseInt(args[0]);
        }

        Publisher pub = new Publisher(playerId);

        JFrame frame = new JFrame("Pong Test - Player " + playerId);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        Field field = new Field(playerId, pub);
        Decorator decoratedField = new Decorator(field);

        // Use a JLayeredPane for overlay
        JLayeredPane layeredPane = new JLayeredPane();
        field.setBounds(0, 0, Field.FIELDWIDTH, Field.FIELDHEIGHT);
        decoratedField.setBounds(0, 0, Field.FIELDWIDTH, Field.FIELDHEIGHT);

        layeredPane.setPreferredSize(new java.awt.Dimension(Field.FIELDWIDTH, Field.FIELDHEIGHT));
        layeredPane.add(field, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(decoratedField, JLayeredPane.PALETTE_LAYER);

        frame.setContentPane(layeredPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Ensure MQTT client disconnects on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try { pub.disconnect(); } catch (Exception ignored) {}
        }));
    }
}