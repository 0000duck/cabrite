package com.imm.bean.move;

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

public class Move implements iMode {
	public static String TOPIC = "/v1.0/recorder/1/update/move";
	
	private String rtc;//当前时间
	private String press;
	private String vel;
	private String actsyspress;
	private List<MoveMold> molds;
	private List<MoveEjector> ejectors;
	private List<MoveCore> cores;
	private List<MoveAirValve> airvalves;
	private List<MoveInj> injestions;

	private KVariable kvRTC;
	private KVariable kvPress;
	private KVariable kvVel;
	private KVariable kvActSysPress;
	
	public Move() {
		molds = new ArrayList<MoveMold>();
		molds.add(new MoveMold(1));
		ejectors = new ArrayList<MoveEjector>();
		ejectors.add(new MoveEjector(1));
		cores = new ArrayList<MoveCore>();
		cores.add(new MoveCore(1));
		airvalves = new ArrayList<MoveAirValve>();
//		airvalves.add(new MoveAirValve(1));
		injestions = new ArrayList<MoveInj>();
		injestions.add(new MoveInj(1));
	}
	
	@Override
	public void init() {
		try {
			kvRTC = HmiVariableService.getService().getVariable("EasyNet.sv_dRTC");
			kvPress = HmiVariableService.getService().getVariable("Pump1.sv_rPressure");
			kvVel = HmiVariableService.getService().getVariable("Pump1.sv_rVelocity");
			kvActSysPress = HmiVariableService.getService().getVariable("Injection1.sv_rActSysPressure");
			for(MoveMold mold : molds){
				mold.init();
			}
			for(MoveEjector ejector : ejectors){
				ejector.init();
			}
			for(MoveCore core : cores){
				core.init();
			}
			for(MoveAirValve airValve : airvalves){
				airValve.init();
			}
			for(MoveInj inj : injestions){
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
	public void refresh() throws VartypeException {
		try {
			final KVariable vars[] = {kvRTC, kvPress, kvVel, kvActSysPress};
			HmiVariableService.getService().readValues(vars);
			
			rtc = kvRTC.getDateValue().getTime()+"";
			press = String.format("%1$.2f", kvPress.getDoubleValue()).replace(",", ".");
			vel = String.format("%1$.2f", kvVel.getDoubleValue()).replace(",", ".");
			actsyspress = String.format("%1$.2f", kvActSysPress.getDoubleValue()).replace(",", ".");
			for(MoveMold mold : molds){
				mold.refresh();
			}
			for(MoveEjector ejector : ejectors){
				ejector.refresh();
			}
			for(MoveCore core : cores){
				core.refresh();
			}
			for(MoveAirValve airValve : airvalves){
				airValve.refresh();
			}
			for(MoveInj inj : injestions){
				inj.refresh();
			}
		} catch (NetworkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getPress() {
		return press;
	}

	public void setPress(String press) {
		this.press = press;
	}

	public String getVel() {
		return vel;
	}

	public void setVel(String vel) {
		this.vel = vel;
	}

	public String getActsyspress() {
		return actsyspress;
	}

	public void setActsyspress(String actsyspress) {
		this.actsyspress = actsyspress;
	}

	public List<MoveMold> getMolds() {
		return molds;
	}

	public void setMolds(List<MoveMold> molds) {
		this.molds = molds;
	}

	public List<MoveEjector> getEjectors() {
		return ejectors;
	}

	public void setEjectors(List<MoveEjector> ejectors) {
		this.ejectors = ejectors;
	}

	public List<MoveCore> getCores() {
		return cores;
	}

	public void setCores(List<MoveCore> cores) {
		this.cores = cores;
	}

	public List<MoveAirValve> getAirvalves() {
		return airvalves;
	}

	public void setAirvalves(List<MoveAirValve> airvalves) {
		this.airvalves = airvalves;
	}

	public List<MoveInj> getInjestions() {
		return injestions;
	}

	public void setInjestions(List<MoveInj> injestions) {
		this.injestions = injestions;
	}

	public String getRtc() {
		return rtc;
	}

	@Override
	public String toString() {
		JSONObject json = JSONObject.fromObject(this);
		return json.toString();
	}

	public static void main(String[] args) {
		Move move = new Move();
		System.out.println(move);
	}
}
