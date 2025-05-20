package org.example;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class Publisher {

    private final static String BROKER = "tcp://test.mosquitto.org:1883";
    private final static String TOPIC = "cal-poly/csc/309";
    private final static String CLIENT_ID = "jgs-publisher";

    public static void main(String[] args) {
        try {
            MqttClient client = new MqttClient(BROKER, CLIENT_ID);
            client.connect();
            System.out.println("Connected to BROKER: " + BROKER);
            int counter = 0;
            while (true) {
                String content = "this is message " + counter;
                MqttMessage message = new MqttMessage(content.getBytes());
                message.setQos(2);
                if (client.isConnected())
                    client.publish(TOPIC, message);
                counter++;
                System.out.println("Message published: " + content);
                Thread.sleep(5000);
            }
        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
