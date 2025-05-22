package org.example;

import javax.swing.*;

public class Game {
    public static void main(String[] args) {
        // Create the window
        JFrame frame = new JFrame("Pong Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // Add your Field panel
        Field field = new Field();
        frame.add(field);

        frame.pack(); // Sizes the window based on preferred size of Field
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setVisible(true);
    }
}
