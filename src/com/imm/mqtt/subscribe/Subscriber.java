package com.imm.mqtt.subscribe;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import com.imm.mqtt.publish.Utils;

public class Subscriber {
    public static final String BROKER_URL = "tcp://10.1.222.99:1883";

	// We have to generate a unique Client id.
    String clientId = Utils.getMacAddress() + "-sub";
    private MqttClient mqttClient;

    public Subscriber() {
        try {
            mqttClient = new MqttClient(BROKER_URL, clientId);
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void start() {
        try {
            mqttClient.setCallback(new SubscribeCallback());
            mqttClient.connect();
            mqttClient.subscribe("/v1.0/recorder/1/get/+");
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String args) {
        final Subscriber subscriber = new Subscriber();
        subscriber.start();
    }
}
