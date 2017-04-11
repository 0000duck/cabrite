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

public class StateMold implements iMode {
	private List<String> temp;
	private int index;
	private KVariable kvMoldTempZone;
//	private List<KVariable> kvMoldTemp; 
	public StateMold(int index) {
		this.index = index;
		temp = new ArrayList<String>();
//		kvMoldTemp = new ArrayList<KVariable>();
	}
	
	@Override
	public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
		kvMoldTempZone = HmiVariableService.getService().getVariable("HeatingMold" + index + ".sv_iNumberOfZones");
	}

	@Override
	public void refresh() throws NetworkException, IOException, VartypeException, VarNotExistException {
		HmiVariableService.getService().readValue(kvMoldTempZone);
		int tempZone = kvMoldTempZone.getIntValue();
		temp.clear();
		for(int i = 1; i <= tempZone; i++){
			KVariable var = HmiVariableService.getService().getVariable("HeatingMold" + index + ".ti_InTemp" + i);
			HmiVariableService.getService().readValue(var);
			temp.add(String.format("%1$.2f", var.getDoubleValue()).replace(",", "."));
		}
	}

	public List<String> getTemp() {
		return temp;
	}

	public void setTemp(List<String> temp) {
		this.temp = temp;
	}
}
