package com.imm.bean.move;

import java.io.IOException;

import com.imm.bean.iMode;
import com.keba.kemro.plc.network.NetworkException;
import com.keba.kemro.plc.service.HmiVariableService;
import com.keba.kemro.plc.variable.KVariable;
import com.keba.kemro.plc.variable.VarNotExistException;
import com.keba.kemro.plc.variable.VartypeException;

public class MoveAirValve implements iMode {
	private String state;//0 Í£Ö¹£¬1¶¯×÷

	private int index;
	
	private KVariable kvAirActive;
	
	public MoveAirValve(int index) {
		this.index = index;
	}

	@Override
	public void init() {
		try {
			kvAirActive = HmiVariableService.getService().getVariable("AirValve" + index + ".do_Air");
		} catch (VartypeException e) {
			e.printStackTrace();
		} catch (VarNotExistException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NetworkException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void refresh() throws NetworkException, IOException, VartypeException {
		HmiVariableService.getService().readValue(kvAirActive);
		if(kvAirActive.getBooleanValue()){
			state = "1";
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
