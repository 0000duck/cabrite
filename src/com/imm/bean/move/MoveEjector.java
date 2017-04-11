package com.imm.bean.move;

import java.io.IOException;

import com.imm.bean.iMode;
import com.keba.kemro.plc.network.NetworkException;
import com.keba.kemro.plc.service.HmiVariableService;
import com.keba.kemro.plc.variable.KVariable;
import com.keba.kemro.plc.variable.VarNotExistException;
import com.keba.kemro.plc.variable.VartypeException;

public class MoveEjector implements iMode {
	private String state;//0Í£Ö¹£¬1Ç°½ø£¬2ºóÍË
	private String pos;
//	private String speed;
	
	private int index;
	
	private KVariable kvfwd;
	private KVariable kvbwd;
	private KVariable kvPos;
//	private KVariable kvSpeed;
	
	public MoveEjector(int index) {
		this.index = index;
	}
	
	@Override
	public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
		kvfwd = HmiVariableService.getService().getVariable("Ejector" + index + ".sv_bEjectorFwd");
		kvbwd = HmiVariableService.getService().getVariable("Ejector" + index + ".sv_bEjectorBwd");
		kvPos = HmiVariableService.getService().getVariable("Ejector" + index + ".sv_rEjectorPosition");
//			kvSpeed = HmiVariableService.getService().getVariable("Ejector" + index + ".do_CoreOut");
	}

	@Override
	public void refresh() throws VartypeException, NetworkException, IOException {
		final KVariable vars[] = { kvfwd, kvbwd, kvPos};
		HmiVariableService.getService().readValues(vars);
		
		if(kvfwd.getBooleanValue()){
			state = "1";
		} else if (kvbwd.getBooleanValue()) {
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
