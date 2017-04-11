package com.imm.bean.move;

import java.io.IOException;

import com.imm.bean.iMode;
import com.keba.kemro.plc.network.NetworkException;
import com.keba.kemro.plc.service.HmiVariableService;
import com.keba.kemro.plc.variable.KVariable;
import com.keba.kemro.plc.variable.VarNotExistException;
import com.keba.kemro.plc.variable.VartypeException;

public class MoveMold implements iMode {
	private String state;//0停止，1合模，2开模
	private String pos;
//	private String speed;
	
	private int index;
	
	private KVariable kvClose;
	private KVariable kvOpen;
	private KVariable kvPos;
//	private KVariable kvSpeed;
	
	public MoveMold(int index) {
		this.index = index;
	}
	
	@Override
	public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
		kvClose = HmiVariableService.getService().getVariable("Mold" + index + ".sv_bCloseActive");
		kvOpen = HmiVariableService.getService().getVariable("Mold" + index + ".sv_bOpenActive");
		kvPos = HmiVariableService.getService().getVariable("Mold" + index + ".sv_rMoldPosition");
//			kvSpeed = HmiVariableService.getService().getVariable("Mold" + index + ".do_CoreOut");
	}

	@Override
	public void refresh() throws NetworkException, IOException, VartypeException {
		final KVariable vars[] = { kvClose, kvOpen, kvPos};
		HmiVariableService.getService().readValues(vars);
		
		if(kvClose.getBooleanValue()){
			state = "1";
		} else if (kvOpen.getBooleanValue()) {
			state = "2";
		}else{
			state = "0";
		}
		pos = String.format("%1$.2f", kvPos.getDoubleValue()).replace(",", ".");
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

}
