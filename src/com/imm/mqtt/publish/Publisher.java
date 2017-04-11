package com.imm.mqtt.publish;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public class Publisher {
	public static final String BROKER_URL = "tcp://10.1.222.99:1883";
	private MqttClient client;
	public Publisher() {
		// We have to generate a unique Client id.
		String clientId = Utils.getMacAddress() + "-pub";
		try {
			client = new MqttClient(BROKER_URL, clientId);
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void start() {
		try {
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(false);
			options.setWill(client.getTopic("/v1.0/recorder/1/LWT"), "I'm gone :(".getBytes(), 0, false);

			client.connect(options);
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void publish(String strTopic, String data) throws MqttPersistenceException, MqttException{
		if (!client.isConnected())
			this.start();
		final MqttTopic topic = client.getTopic(strTopic);
		topic.publish(new MqttMessage(data.getBytes()));
	}
}
