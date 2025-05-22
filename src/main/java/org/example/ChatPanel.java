package org.example;

import javax.swing.*;
import java.awt.*;
import org.eclipse.paho.client.mqttv3.MqttException;

public class ChatPanel extends JPanel {
    private final JTextArea chatArea = new JTextArea();
    private final JTextField inputField = new JTextField();
    private final Publisher publisher;

    public ChatPanel(Publisher publisher) {
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
            //appendMessage("Me: " + text);
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
