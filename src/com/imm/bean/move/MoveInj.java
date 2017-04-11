package com.imm.bean.move;

import java.io.IOException;

import com.imm.bean.iMode;
import com.keba.kemro.plc.network.NetworkException;
import com.keba.kemro.plc.service.HmiVariableService;
import com.keba.kemro.plc.variable.KVariable;
import com.keba.kemro.plc.variable.VarNotExistException;
import com.keba.kemro.plc.variable.VartypeException;

import net.sf.json.JSONObject;

public class MoveInj implements iMode {
	private String state;//0 Í£Ö¹£¬1Éä½º£¬2±£Ñ¹£¬3ËÉÍË£¬4³é½º
	private String pos; //ÂÝ¸ËÎ»ÖÃ
	private String screwvel;//ÂÝ¸ËÒÆ¶¯ËÙ¶È
	private String screwcircspeed;//ÂÝ¸Ë×ªËÙ
	private String actinjpress;//Éä½ºÑ¹Á¦
	
	private int index;
	
	private KVariable kvInjActive;
	private KVariable kvholdActive;
	private KVariable kvDecompActive;
	private KVariable kvPlastActive;
	private KVariable kvPos;
	private KVariable kvScrewVel;
	private KVariable kvScrewCircSpeed;
	private KVariable kvActInjPress;
	
	public MoveInj(int index) {
		this.index = index;
	}
	
	@Override
	public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
		kvInjActive = HmiVariableService.getService().getVariable("Injection" + index + ".sv_bInjectionActive");
		kvholdActive = HmiVariableService.getService().getVariable("Injection" + index + ".sv_bHoldActive");
		kvDecompActive = HmiVariableService.getService().getVariable("Injection" + index + ".sv_bDecompActive");
		kvPlastActive = HmiVariableService.getService().getVariable("Injection" + index + ".sv_bPlastActive");
		
		kvPos = HmiVariableService.getService().getVariable("Injection" + index + ".sv_rScrewPosition");
		kvScrewVel = HmiVariableService.getService().getVariable("Injection" + index + ".sv_rActScrewCircSpeed");
		kvScrewCircSpeed = HmiVariableService.getService().getVariable("Injection" + index + ".sv_rCushion");
		kvActInjPress = HmiVariableService.getService().getVariable("Injection" + index + ".sv_rActPressure");
	}

	@Override
	public void refresh() throws NetworkException, IOException, VartypeException {
		final KVariable vars[] = { kvInjActive, kvholdActive, kvDecompActive, kvPlastActive,
				kvPos, kvScrewVel, kvScrewCircSpeed, kvActInjPress};
		HmiVariableService.getService().readValues(vars);
		
		boolean injActive = kvInjActive.getBooleanValue();
		boolean holdActive = kvholdActive.getBooleanValue();
		boolean decompActive = kvDecompActive.getBooleanValue();
		boolean plastActive = kvPlastActive.getBooleanValue();
		if (injActive) {
			state = "1";
		} else if (holdActive) {
			state = "2";
		} else if (decompActive) {
			state = "3";
		} else if (plastActive) {
			state = "4";
		} else {
			state = "0";
		}
		
		pos = String.format("%1$.2f", kvPos.getDoubleValue()).replace(",", ".");
		screwvel = String.format("%1$.2f", kvScrewVel.getDoubleValue()).replace(",", ".");
		screwcircspeed = String.format("%1$.2f", kvScrewCircSpeed.getDoubleValue()).replace(",", ".");
		actinjpress = String.format("%1$.2f", kvActInjPress.getDoubleValue()).replace(",", ".");
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getScrewvel() {
		return screwvel;
	}

	public void setScrewvel(String screwvel) {
		this.screwvel = screwvel;
	}

	public String getScrewcircspeed() {
		return screwcircspeed;
	}

	public void setScrewcircspeed(String screwcircspeed) {
		this.screwcircspeed = screwcircspeed;
	}

	public String getActinjpress() {
		return actinjpress;
	}

	public void setActinjpress(String actinjpress) {
		this.actinjpress = actinjpress;
	}
	
	@Override
	public String toString() {
		JSONObject json = JSONObject.fromObject(this);
		return json.toString();
	}

	public static void main(String[] args) {
		MoveInj moveInj = new MoveInj(1);
		System.out.println(moveInj);
	}
}
