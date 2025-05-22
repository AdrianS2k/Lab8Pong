package org.example;


import javax.swing.JFrame;

public class Game {
    public static void main(String[] args) throws Exception {
        int playerId = 2; // Default to host

        if (args.length > 0) {
            playerId = Integer.parseInt(args[0]);
        }

        Publisher pub = new Publisher(playerId);
        
        JFrame frame = new JFrame("Pong Test - Player " + playerId);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        Field field = new Field(playerId, pub);
        Decorator decoratedField = new Decorator(field);
        frame.add(decoratedField);
        
        frame.add(field);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Ensure MQTT client disconnects on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try { pub.disconnect(); } catch (Exception ignored) {}
        }));
    }
}