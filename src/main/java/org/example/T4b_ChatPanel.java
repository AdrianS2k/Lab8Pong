package org.example;

/**
 * Adds a chat tab to the top of the window, provides a text field
 * and send button for sending new chat messages between players
 * T4b
 * @author Aidan Stutz
 */

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.eclipse.paho.client.mqttv3.MqttException;

public class T4b_ChatPanel extends JPanel {
    private final JTextArea chatArea = new JTextArea();
    private final JTextField inputField = new JTextField();
    private final T4b_Publisher publisher;

    public T4b_ChatPanel(T4b_Publisher publisher) {
        this.publisher = publisher;
        setLayout(new BorderLayout());

        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel inputPane = new JPanel(new BorderLayout());
        JButton sendBtn = new JButton("Send");
        sendBtn.addActionListener(e -> sendChat());
        inputPane.add(inputField, BorderLayout.CENTER);
        inputPane.add(sendBtn, BorderLayout.EAST);
        add(inputPane, BorderLayout.SOUTH);
    }

    private void sendChat() {
        String text = inputField.getText().trim();
        if (text.isEmpty()) return;
        try {
            publisher.sendChat(text);
            inputField.setText("");
        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

    public void appendMessage(String msg) {
        chatArea.append(msg + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }
}
