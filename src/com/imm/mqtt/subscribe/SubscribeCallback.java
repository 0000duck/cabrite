package com.imm.mqtt.subscribe;

import java.io.IOException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.imm.bean.para.Para;
import com.imm.bean.speci.Speci;
import com.imm.bean.state.State;
import com.imm.recorder.RecorderThread;
import com.keba.kemro.plc.network.NetworkException;
import com.keba.kemro.plc.variable.VarNotExistException;
import com.keba.kemro.plc.variable.VartypeException;

public class SubscribeCallback implements MqttCallback{
	public static Speci speci;
	public static State state;
	public static Para para;
	public static int moveActive;
	
	public SubscribeCallback() {
		speci = new Speci();
		state = new State();
		para = new Para();
		try {
			speci.init();
			state.init();
			para.init();
		} catch (VartypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VarNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NetworkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void connectionLost(Throwable cause) {
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("Message arrived. Topic: " + topic + "  Message: " + message.toString());
        if ("/v1.0/recorder/1/get/speci".equals(topic)) {
        	speci.refresh();
        	RecorderThread.publisher.publish(Speci.TOPIC, speci.toString());
        	System.out.println(speci);
        } else if ("/v1.0/recorder/1/get/para".equals(topic)) {
        	para.refresh();
        	RecorderThread.publisher.publish(Para.TOPIC, para.toString());
        	System.out.println(para);
        } else if ("/v1.0/recorder/1/get/state".equals(topic)) {
        	state.refresh();
        	RecorderThread.publisher.publish(State.TOPIC, state.toString());
        	System.out.println(state);
        } else if ("/v1.0/recorder/1/get/move".equals(topic)) {
        	moveActive = 60;
        }
        
	}

}
