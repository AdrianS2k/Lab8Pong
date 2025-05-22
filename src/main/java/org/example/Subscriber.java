package org.example;

import org.eclipse.paho.client.mqttv3.*;

public class Subscriber implements MqttCallback {

    private static final String BROKER     = "tcp://test.mosquitto.org:1883";
    private static final String ROOT_TOPIC = "cal-poly/csc/309/pong/room1";
    //private final static String CLIENT_ID = "jgs-subscriber";

    private final MqttClient client;

    public Subscriber(int playerId) throws MqttException {
        client = new MqttClient(BROKER, "player-" + playerId + "-sub");
        client.setCallback(this);
        client.connect();
        client.subscribe(ROOT_TOPIC + "/#");
        System.out.println("Subscriber listening on " + ROOT_TOPIC + "/#");
    }

    public static void main(String[] args) {
        try {
//            MqttClient client = new MqttClient(BROKER, CLIENT_ID);
//            Subscriber subscriber = new Subscriber();
//            client.setCallback(subscriber);
//            client.connect();
//            System.out.println("Connected to BROKER: " + BROKER);
//            client.subscribe(TOPIC);
//            System.out.println("Subscribed to TOPIC: " + TOPIC);
            int player;
            if (args.length > 0) {
                player = Integer.parseInt(args[0]);
            } else {
                //default to player 1
                System.out.println("No player ID supplied; defaulting to player-1");
                player = 1;
            }

            new Subscriber(player);

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
                    Repository.getInstance().getHost().setBarPos(y);
                } else if (pl == 2) {
                    Repository.getInstance().getClient().setBarPos(y);
                }
                break;

            case "CHAT":
                pl  = Integer.parseInt(parts[1]);
                String msg = parts[2];
                System.out.printf("Chat[%d]: %s%n", pl, msg);
                //display in chat UI
                break;

            default:
                System.out.println("Unknown type: " + type);
        }
    }
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        //System.out.println("Delivered complete: " + iMqttDeliveryToken.getMessageId());
    }

}