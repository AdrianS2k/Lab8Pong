package org.example;

/**
 * Publishes paddle movement and chat comms to the MQTT broker under
 * distinct “/game” and “/chat” topics for a two player Pong match.
 *
 * @author Aidan Stutz
 */

import org.eclipse.paho.client.mqttv3.*;
import java.nio.charset.StandardCharsets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Publisher {

    private static final String BROKER      = "tcp://test.mosquitto.org:1883";
    private static final String ROOT_TOPIC  = "cal-poly/csc/309/pong/room1";
    private static final String GAME_TOPIC  = ROOT_TOPIC + "/game";
    private static final String CHAT_TOPIC  = ROOT_TOPIC + "/chat";
    //private final static String CLIENT_ID = "jgs-publisher";

    private final MqttClient client;
    private final int playerId;

    public Publisher(int playerId) throws MqttException {
        this.playerId = playerId;
        this.client = new MqttClient(BROKER, "player-" + playerId + "-pub");

        MqttConnectOptions opts = new MqttConnectOptions();
        opts.setCleanSession(true);
        opts.setAutomaticReconnect(true);

        client.connect(opts);
        System.out.println("Publisher connected as player " + playerId);
    }

    public void sendPaddleMove(int y) throws MqttException {
        String payload = String.join("|",
                "PADDLE_MOVE",
                Integer.toString(playerId),
                Integer.toString(y)
        );
        publish(GAME_TOPIC, payload, 1);
    }

    public void sendChat(String text) throws MqttException {
        if (!client.isConnected()) {
            client.reconnect();
        }
        String payload = String.join("|",
                "CHAT",
                Integer.toString(playerId),
                text.replace("|"," ")
        );
        publish(CHAT_TOPIC, payload, 1);
    }

    public void sendScore(int hostScore, int clientScore) throws MqttException {
        String payload = String.join("|",
                "SCORE",
                Integer.toString(hostScore),
                Integer.toString(clientScore)
        );
        publish(GAME_TOPIC, payload, 1);
    }

    private void publish(String topic, String payload, int qos) throws MqttException {
        MqttMessage m = new MqttMessage(payload.getBytes(StandardCharsets.UTF_8));
        m.setQos(qos);
        client.publish(topic, m);
    }

    public void disconnect() throws MqttException {
        client.disconnect();
    }

    public static void main(String[] args) throws Exception {
        try {
            int player;
            if (args.length > 0) {
                player = Integer.parseInt(args[0]);
            } else {
                System.out.println("No player ID supplied; defaulting to player-1");
                player = 1;
            }

            Publisher pub = new Publisher(player);

            //demo loop
            for (int y = 0; y < 100; y += 25) {
                pub.sendPaddleMove(y);
                System.out.printf(">>> sent PADDLE_MOVE|%d|%d%n", player, y);
                Thread.sleep(300);
            }
            pub.sendChat("Ready to play!");
            System.out.printf(">>> sent CHAT|%d|Ready to play!%n", player);

            pub.disconnect();
        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
