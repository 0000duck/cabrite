package com.imm.bean.move;

import java.io.IOException;

import com.imm.bean.iMode;
import com.keba.kemro.plc.network.NetworkException;
import com.keba.kemro.plc.service.HmiVariableService;
import com.keba.kemro.plc.variable.KVariable;
import com.keba.kemro.plc.variable.VarNotExistException;
import com.keba.kemro.plc.variable.VartypeException;

public class MoveCore implements iMode {
	private String state;//0Í£Ö¹£¬1Ç°½ø£¬2ºóÍË

	private int index;
	
	private KVariable kvCoreIn;
	private KVariable kvCoreOut;
	
	public MoveCore(int index) {
		this.index = index;
	}

	@Override
	public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
		kvCoreIn = HmiVariableService.getService().getVariable("Core" + index + ".do_CoreIn");
		kvCoreOut = HmiVariableService.getService().getVariable("Core" + index + ".do_CoreOut");
	}

	@Override
	public void refresh() throws NetworkException, IOException, VartypeException {
		final KVariable vars[] = {kvCoreIn, kvCoreOut};
		HmiVariableService.getService().readValues(vars);
		
		if(kvCoreIn.getBooleanValue()){
			state = "1";
		} else if (kvCoreOut.getBooleanValue()) {
			state = "2";
		}else{
			state = "0";
		}
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
