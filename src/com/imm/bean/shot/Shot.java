package com.imm.bean.shot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.imm.bean.iMode;
import com.keba.kemro.plc.network.NetworkException;
import com.keba.kemro.plc.service.HmiVariableService;
import com.keba.kemro.plc.variable.KVariable;
import com.keba.kemro.plc.variable.VarNotExistException;
import com.keba.kemro.plc.variable.VartypeException;

import net.sf.json.JSONObject;

public class Shot implements iMode{
	public static String TOPIC = "/v1.0/recorder/1/add/shot";
	
	private String shottime;//注射时间（系统时间）
	private String shotcounter;//当前模数
	private String cycletime;//周期时间
	private List<ShotInj> injections;
	
	private KVariable kvarShotTimeAct; 
	private KVariable kvarShotCounter; 
	private KVariable kvarCycleTime;
	
	public Shot() {
		injections = new ArrayList<ShotInj>();
		injections.add(new ShotInj(1));
	}

	@Override
	public void init() {
		try {
			kvarShotTimeAct = HmiVariableService.getService().getVariable("system.sv_dShotTimeAct"); 
			kvarShotCounter = HmiVariableService.getService().getVariable("system.sv_iShotCounterAct");
			kvarCycleTime = HmiVariableService.getService().getVariable("system.sv_dLastCycleTime");
			for(ShotInj inj:injections){
				inj.init();
			}
			this.refresh();
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
	public void refresh() throws NetworkException, IOException, VartypeException {
		final KVariable vars[] = { kvarShotTimeAct, kvarShotCounter, kvarCycleTime};
		HmiVariableService.getService().readValues(vars);
			
		shottime = kvarShotTimeAct.getDateValue().getTime()+"";
		shotcounter = kvarShotCounter.getIntValue()+"";
		cycletime = kvarCycleTime.getIntValue()+"";
				
		for(ShotInj injShot: injections){
			injShot.refresh();
		}
	}
	
	public String getShottime() {
		return shottime;
	}

	public void setShottime(String shottime) {
		this.shottime = shottime;
	}

	public String getShotcounter() {
		return shotcounter;
	}

	public void setShotcounter(String shotcounter) {
		this.shotcounter = shotcounter;
	}

	public String getCycletime() {
		return cycletime;
	}

	public void setCycletime(String cycleTime) {
		this.cycletime = cycleTime;
	}

	public List<ShotInj> getInjections() {
		return injections;
	}

	public void setInjections(List<ShotInj> injections) {
		this.injections = injections;
	}

	@Override
	public String toString() {
		JSONObject json = JSONObject.fromObject(this);
		return json.toString();
	}
	
	public static void main(String[] args) {
		Shot shot = new Shot();
		System.out.println(shot);
	}
}
