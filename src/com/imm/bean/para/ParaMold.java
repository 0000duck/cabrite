package com.imm.bean.para;

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

public class ParaMold implements iMode {
	private String coolingtime;//¿‰»¥ ±º‰
	private List<ParaMoldClose> close;
	private List<ParaMoldOpen> open;
	
	private int index;
	
	private KVariable kvCoolingtime;
	private KVariable kvCloseStage;
	private KVariable kvOpenStage;
	
	public ParaMold(int index) {
		this.index = index;
		close = new ArrayList<>();
		open = new ArrayList<>();
	}
	
	@Override
	public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
		kvCoolingtime = HmiVariableService.getService().getVariable("CoolingTime" + index + ".sv_dCoolingTime");
		kvCloseStage = HmiVariableService.getService().getVariable("Mold" + index + ".sv_MoldFwdProfVis.Profile.iNoOfPoints");
		kvOpenStage = HmiVariableService.getService().getVariable("Mold" + index + ".sv_MoldBwdProfVis.Profile.iNoOfPoints");
	}

	@Override
	public void refresh() throws VarNotExistException, NetworkException, IOException, VartypeException {
		final KVariable vars[] = { kvCoolingtime, kvCloseStage, kvOpenStage };
		HmiVariableService.getService().readValues(vars);

		coolingtime = kvCoolingtime.getIntValue()+"";
		int closeStage = kvCloseStage.getIntValue();
		close.clear();
		for (int i = 0; i < closeStage; i++) {
			ParaMoldClose paraMoldClose = new ParaMoldClose(index, i);
			paraMoldClose.init();
			paraMoldClose.refresh();
			close.add(paraMoldClose);
		}

		int openStage = kvOpenStage.getIntValue();
		open.clear();
		for (int i = 0; i < openStage; i++) {
			ParaMoldOpen paraMoldOpen = new ParaMoldOpen(index, i);
			paraMoldOpen.init();
			paraMoldOpen.refresh();
			open.add(paraMoldOpen);
		}
	}
	
	public String getCoolingtime() {
		return coolingtime;
	}

	public List<ParaMoldClose> getClose() {
		return close;
	}

	public List<ParaMoldOpen> getOpen() {
		return open;
	}

	@Override
	public String toString() {
		JSONObject json = JSONObject.fromObject(this);
		return json.toString();
	}
	
	public class ParaMoldClose implements iMode {
		String press;
		String vel;
		String pos;
		
		int index, stage;
		
		KVariable kvPress;
		KVariable kvVel;
		KVariable kvPos;
		
		public ParaMoldClose(int index, int stage) {
			this.index = index;
			this.stage = stage + 1;
		}

		@Override
		public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
			kvPress = HmiVariableService.getService().getVariable("Mold" + index + ".sv_MoldFwdProfVis.Profile.Points[" + stage + "].rPressure");
			kvVel = HmiVariableService.getService().getVariable("Mold" + index + ".sv_MoldFwdProfVis.Profile.Points[" + stage + "].rVelocity");
			kvPos = HmiVariableService.getService().getVariable("Mold" + index + ".sv_MoldFwdProfVis.Profile.Points[" + (stage + 1) + "].rStartPos");
		}

		@Override
		public void refresh() throws NetworkException, IOException, VartypeException {
			final KVariable vars[] = {kvPress, kvVel, kvPos};
			HmiVariableService.getService().readValues(vars);
			press = String.format("%1$.2f", kvPress.getDoubleValue()).replace(",", ".");
			vel = String.format("%1$.2f", kvVel.getDoubleValue()).replace(",", ".");
			pos = String.format("%1$.2f", kvPos.getDoubleValue()).replace(",", ".");
		}

		public String getPress() {
			return press;
		}

		public String getVel() {
			return vel;
		}

		public String getPos() {
			return pos;
		}
	}
	
	public class ParaMoldOpen implements iMode  {
		String press;
		String vel;
		String pos;
		
		int index, stage;
		
		KVariable kvPress;
		KVariable kvVel;
		KVariable kvPos;
		
		public ParaMoldOpen(int index, int stage) {
			this.index = index;
			this.stage = stage + 1;
		}

		@Override
		public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
			kvPress = HmiVariableService.getService().getVariable("Mold" + index + ".sv_MoldBwdProfVis.Profile.Points[" + stage + "].rPressure");
			kvVel = HmiVariableService.getService().getVariable("Mold" + index + ".sv_MoldBwdProfVis.Profile.Points[" + stage + "].rVelocity");
			kvPos = HmiVariableService.getService().getVariable("Mold" + index + ".sv_MoldBwdProfVis.Profile.Points[" + (stage + 1) + "].rStartPos");
		}

		@Override
		public void refresh() throws NetworkException, IOException, VartypeException {
			final KVariable vars[] = {kvPress, kvVel, kvPos};
			HmiVariableService.getService().readValues(vars);
			press = String.format("%1$.2f", kvPress.getDoubleValue()).replace(",", ".");
			vel = String.format("%1$.2f", kvVel.getDoubleValue()).replace(",", ".");
			pos = String.format("%1$.2f", kvPos.getDoubleValue()).replace(",", ".");
		}

		public String getPress() {
			return press;
		}

		public String getVel() {
			return vel;
		}

		public String getPos() {
			return pos;
		}
	}
}
