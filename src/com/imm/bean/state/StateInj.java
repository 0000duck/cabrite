package com.imm.bean.state;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.imm.bean.iMode;
import com.keba.kemro.plc.network.NetworkException;
import com.keba.kemro.plc.service.HmiVariableService;
import com.keba.kemro.plc.variable.KVariable;
import com.keba.kemro.plc.variable.VarNotExistException;
import com.keba.kemro.plc.variable.VartypeException;

public class StateInj implements iMode {
	private String hoppertemp;
	private List<String> temp;
	
	private int index;
	
//	private KVariable kvHopperTemp;
	private KVariable kvTempZone;
	
	public StateInj(int index) {
		this.index = index;
		temp = new ArrayList<String>();
	}
	
	@Override
	public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
//		kvHopperTemp = HmiVariableService.getService().getVariable("HeatingNozzle" + index + ".ti_TempMaterial"); 
		kvTempZone = HmiVariableService.getService().getVariable("HeatingNozzle" + index + ".sv_iNumberOfZones");
	}

	@Override
	public void refresh() throws NetworkException, IOException, VartypeException, VarNotExistException {
//		HmiVariableService.getService().readValue(kvHopperTemp);
//		hoppertemp = kvHopperTemp.getStringValue();
		HmiVariableService.getService().readValue(kvTempZone);
		int tempZone = kvTempZone.getIntValue();
		temp.clear();
		for(int i = 1; i <= tempZone; i++){
			KVariable var = HmiVariableService.getService().getVariable("HeatingNozzle" + index + ".ti_InTemp" + i);
			HmiVariableService.getService().readValue(var);
			temp.add(String.format("%1$.2f", var.getDoubleValue()).replace(",", "."));
		}		
	}

	public String getHoppertemp() {
		return hoppertemp;
	}

	public void setHoppertemp(String hoppertemp) {
		this.hoppertemp = hoppertemp;
	}

	public List<String> getTemp() {
		return temp;
	}

	public void setTemp(List<String> temp) {
		this.temp = temp;
	}
}
