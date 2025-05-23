package org.example;

/**
 * Subscribes to all Pong room MQTT topics (“/game” & “/chat”),
 * and parses each payload and has cases for each event.
 * T4b
 * @author Aidan Stutz
 */

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class T4b_Subscriber implements MqttCallback {

    private static final String BROKER     = "tcp://test.mosquitto.org:1883";
    private static final String ROOT_TOPIC = "cal-poly/csc/309/pong/room1";
    private final List<Consumer<String>> chatListeners = new ArrayList<>();

    private final MqttClient client;

    public T4b_Subscriber(int playerId) throws MqttException {
        client = new MqttClient(BROKER, "player-" + playerId + "-sub");
        client.setCallback(this);
        client.connect();
        client.subscribe(ROOT_TOPIC + "/#");
        System.out.println("Subscriber listening on " + ROOT_TOPIC + "/#");
    }

    public void addChatListener(Consumer<String> listener) {
        chatListeners.add(listener);
    }

    public static void main(String[] args) {
        try {
            int player;
            if (args.length > 0) {
                player = Integer.parseInt(args[0]);
            } else {
                //default to player 1
                System.out.println("No player ID supplied; defaulting to player-1");
                player = 1;
            }

            new T4b_Subscriber(player);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection lost: " + throwable.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        String payload = new String(mqttMessage.getPayload(), java.nio.charset.StandardCharsets.UTF_8);
        String[] parts = payload.split("\\|", 3);
        String type = parts[0];

        switch (type) {
            case "PADDLE_MOVE":
                int pl = Integer.parseInt(parts[1]);
                int y  = Integer.parseInt(parts[2]);
                System.out.printf("Player %d moved paddle to y=%d%n", pl, y);
                if (pl == 1) {
                    T4b_Repository.getInstance().getHost().setBarPos(y);
                } else if (pl == 2) {
                    T4b_Repository.getInstance().getClient().setBarPos(y);
                }
                break;

            case "CHAT":
                pl  = Integer.parseInt(parts[1]);
                String msg = parts[2];
                String display = "Player " + pl + ": " + msg;
                chatListeners.forEach(l -> l.accept(display));
                break;

            case "SCORE":
                int h = Integer.parseInt(parts[1]);
                int c = Integer.parseInt(parts[2]);
                T4b_Repository.getInstance().setScores(h, c);
                break;

            default:
                System.out.println("Unknown type: " + type);
        }
    }
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }

}
