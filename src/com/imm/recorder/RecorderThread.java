package com.imm.recorder;

import java.io.IOException;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.imm.bean.move.Move;
import com.imm.bean.shot.Shot;
import com.imm.mqtt.publish.Publisher;
import com.imm.mqtt.subscribe.SubscribeCallback;
import com.imm.mqtt.subscribe.Subscriber;
import com.keba.kemro.plc.event.ValueChangedEvent;
import com.keba.kemro.plc.event.ValueChangedListener;
import com.keba.kemro.plc.network.NetworkException;
import com.keba.kemro.plc.service.HmiVariableService;
import com.keba.kemro.plc.variable.KVariable;
import com.keba.kemro.plc.variable.VarNotExistException;
import com.keba.kemro.plc.variable.VartypeException;

public class RecorderThread extends Thread {
	public static Publisher publisher;
	Subscriber subscriber;
	public RecorderThread() {
		publisher = new Publisher();
		publisher.start();
		subscriber = new Subscriber();
		subscriber.start();
		try {
			Shot shot = new Shot();
			shot.init();
			KVariable kvarShotCounter = HmiVariableService.getService().getVariable("system.sv_iShotCounterAct");
			kvarShotCounter.addListener(new ValueChangedListener() {
				@Override
				public void valueChanged(ValueChangedEvent arg0) {
					try {
						shot.refresh();
					} catch (VartypeException | NetworkException | IOException e1) {
						e1.printStackTrace();
					}
					try {
						publisher.publish(Shot.TOPIC, shot.toString());
					} catch (MqttException e) {
						e.printStackTrace();
					}
					System.out.println(Shot.TOPIC + " = " + shot);
				}
			});
		} catch (VartypeException | VarNotExistException | IOException | NetworkException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		int i = 0;
		Move move = new Move();
		move.init();
		while(true){
			try {
				if(i >= 60){
					try {
						SubscribeCallback.state.refresh();
					} catch (NetworkException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					publisher.publish(com.imm.bean.state.State.TOPIC, SubscribeCallback.state.toString());
					i = 0;
				}
				
				if(SubscribeCallback.moveActive > 0){
					move.refresh();
					publisher.publish(Move.TOPIC, move.toString());
					SubscribeCallback.moveActive--;
					System.out.println(SubscribeCallback.moveActive);
				}
				i++;
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (VarNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MqttPersistenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (VartypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
