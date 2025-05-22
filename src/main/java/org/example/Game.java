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
        frame.add(field);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
