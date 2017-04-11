package com.imm.bean.para;

import java.io.IOException;

import com.imm.bean.iMode;
import com.keba.kemro.plc.network.NetworkException;
import com.keba.kemro.plc.service.HmiVariableService;
import com.keba.kemro.plc.variable.KVariable;
import com.keba.kemro.plc.variable.VarNotExistException;
import com.keba.kemro.plc.variable.VartypeException;

import net.sf.json.JSONObject;

public class ParaAirValve implements iMode {
	private String mode;//吹气模式
	private String pos;//位置
	private String delay;//延时
	private String time;//动作时间

	private int index;
	
	private KVariable kvMode;
	private KVariable kvPos;
	private KVariable kvDelay;
	private KVariable kvTime;
	
	public ParaAirValve(int index) {
		this.index = index;
	}

	@Override
	public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
		kvMode = HmiVariableService.getService().getVariable("AirValve" + index + ".sv_AirValveMode");
		kvPos = HmiVariableService.getService().getVariable("AirValve" + index + ".sv_rStartPosition");
		kvDelay = HmiVariableService.getService().getVariable("AirValve" + index + ".sv_AirValveTimesSet.dSetDelayTime");
		kvTime = HmiVariableService.getService().getVariable("AirValve" + index + ".sv_AirValveTimesSet.dSetMoveTime");
	}

	@Override
	public void refresh() throws NetworkException, IOException, VartypeException {
		final KVariable vars[] = {kvMode, kvPos, kvDelay, kvTime };
		HmiVariableService.getService().readValues(vars);
		
		mode = kvMode.getStringValue();
		pos = String.format("%1$.2f", kvPos.getDoubleValue()).replace(",", ".");
		delay = String.format("%1$.2f", kvDelay.getDoubleValue()).replace(",", ".");
		time = kvTime.getIntValue()+"";
	}

	public String getMode() {
		return mode;
	}

	public String getPos() {
		return pos;
	}

	public String getDelay() {
		return delay;
	}

	public String getTime() {
		return time;
	}
	
	@Override
	public String toString() {
		JSONObject json = JSONObject.fromObject(this);
		return json.toString();
	}	
}
