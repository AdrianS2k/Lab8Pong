package org.example;

import javax.swing.*;

public class Game {
    public static void main(String[] args) throws Exception {
        final int playerId = (args.length>0) ? Integer.parseInt(args[0]) : 1;
        Publisher pub = new Publisher(playerId);
        Subscriber sub = new Subscriber(playerId);

//        if (args.length > 0) {
//            playerId = Integer.parseInt(args[0]);
//        }

        //JFrame frame = new JFrame("Pong Test - Player " + playerId);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setResizable(false);

        Field field = new Field(playerId, pub);
//        frame.add(field);
//
//        frame.pack();
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);

        ChatPanel chatPanel = new ChatPanel(pub);
        sub.addChatListener(chatPanel::appendMessage);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Play", field);
        tabs.addTab("Chat", chatPanel);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Pong - Player " + playerId);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(tabs);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
