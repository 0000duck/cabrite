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

public class ParaEjector implements iMode {
	private List<ParaEjectorFwd> fwd;
	private List<ParaEjectorBwd> bwd;
	
	private int index;
	
	private KVariable kvFwdStage;
	private KVariable kvBwdStage;
	
	public ParaEjector(int index) {
		this.index = index;
		fwd = new ArrayList<>();
		bwd = new ArrayList<>();
	}
	
	@Override
	public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
		kvFwdStage = HmiVariableService.getService().getVariable("Ejector" + index + ".sv_EjectorFwdVisRel.Profile.iNoOfPoints");
		kvBwdStage = HmiVariableService.getService().getVariable("Ejector" + index + ".sv_EjectorBwdVisRel.Profile.iNoOfPoints");
	}

	@Override
	public void refresh() throws VarNotExistException, NetworkException, IOException, VartypeException {
		final KVariable vars[] = {kvFwdStage, kvBwdStage};
		HmiVariableService.getService().readValues(vars);

		int fwdStage = kvFwdStage.getIntValue();
		fwd.clear();
		for (int i = 0; i < fwdStage; i++) {
			ParaEjectorFwd paraEjectorFwd = new ParaEjectorFwd(index, i);
			paraEjectorFwd.init();
			paraEjectorFwd.refresh();
			fwd.add(paraEjectorFwd);
		}

		int bwdStage = kvBwdStage.getIntValue();
		bwd.clear();
		for (int i = 0; i < bwdStage; i++) {
			ParaEjectorBwd paraEjectorBwd = new ParaEjectorBwd(index, i);
			paraEjectorBwd.init();
			paraEjectorBwd.refresh();
			bwd.add(paraEjectorBwd);
		}
	}
	
	public List<ParaEjectorFwd> getFwd() {
		return fwd;
	}

	public List<ParaEjectorBwd> getBwd() {
		return bwd;
	}

	@Override
	public String toString() {
		JSONObject json = JSONObject.fromObject(this);
		return json.toString();
	}
	
	public class ParaEjectorFwd implements iMode {
		String press;
		String vel;
		String pos;
		
		int index, stage;
		
		KVariable kvPress;
		KVariable kvVel;
		KVariable kvPos;
		
		public ParaEjectorFwd(int index, int stage) {
			this.index = index;
			this.stage = stage + 1;
		}

		@Override
		public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
			kvPress = HmiVariableService.getService().getVariable("Ejector" + index + ".sv_EjectorFwdVisRel.Profile.Points[" + stage + "].rPressure");
			kvVel = HmiVariableService.getService().getVariable("Ejector" + index + ".sv_EjectorFwdVisRel.Profile.Points[" + stage + "].rVelocity");
			kvPos = HmiVariableService.getService().getVariable("Ejector" + index + ".sv_EjectorFwdVisRel.Profile.Points[" + (stage + 1) + "].rStartPos");
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
	
	public class ParaEjectorBwd implements iMode  {
		String press;
		String vel;
		String pos;
		
		int index, stage;
		
		KVariable kvPress;
		KVariable kvVel;
		KVariable kvPos;
		
		public ParaEjectorBwd(int index, int stage) {
			this.index = index;
			this.stage = stage + 1;
		}

		@Override
		public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
			kvPress = HmiVariableService.getService().getVariable("Ejector" + index + ".sv_EjectorBwdVisRel.Profile.Points[" + stage + "].rPressure");
			kvVel = HmiVariableService.getService().getVariable("Ejector" + index + ".sv_EjectorBwdVisRel.Profile.Points[" + stage + "].rVelocity");
			kvPos = HmiVariableService.getService().getVariable("Ejector" + index + ".sv_EjectorBwdVisRel.Profile.Points[" + (stage + 1) + "].rStartPos");
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
