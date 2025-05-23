package org.example;

import org.example.Field;
import org.example.Publisher;
import org.example.Subscriber;

import javax.swing.*;

public class Game {
    public static void main(String[] args) throws Exception {
        int playerId = (args.length > 0) ? Integer.parseInt(args[0]) : 1;
        Publisher publisher = new Publisher(playerId);
        Subscriber subscriber = new Subscriber(playerId);

        JFrame frame = new JFrame("Pong - Player " + playerId);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        Field field = new Field(playerId, publisher);
        frame.add(field);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Ensure MQTT client disconnects on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try { publisher.disconnect(); } catch (Exception ignored) {}
        }));
    }
}