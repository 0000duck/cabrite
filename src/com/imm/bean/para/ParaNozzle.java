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

public class ParaNozzle implements iMode {
	private int mode;//射台模式: 1为位置, 2为限位, 3为时间
	private List<ParaNozzleFwd> fwd;
	private List<ParaNozzleBwd> bwd;
	private ParaNozzleContact contact;
	
	private int index;
	
	private KVariable kvMovementMode;
	private KVariable kvFwdStage;
	private KVariable kvBwdStage;
	
	public ParaNozzle(int index) {
		this.index = index;
		fwd = new ArrayList<>();
		bwd = new ArrayList<>();
		contact = new ParaNozzleContact(this.index);
	}
	
	@Override
	public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
		kvMovementMode = HmiVariableService.getService().getVariable("Nozzle" + index + ".sv_MovementMode");
		kvFwdStage = HmiVariableService.getService().getVariable("Nozzle" + index + ".sv_NozzleFwdProfVis.Profile.iNoOfPoints");
		kvBwdStage = HmiVariableService.getService().getVariable("Nozzle" + index + ".sv_NozzleBwdProfVis.Profile.iNoOfPoints");
		contact.init();
	}

	@Override
	public void refresh() throws VarNotExistException, NetworkException, IOException, VartypeException {
		final KVariable vars[] = { kvMovementMode, kvFwdStage, kvBwdStage};
		HmiVariableService.getService().readValues(vars);

		mode = kvMovementMode.getIntValue();
		fwd.clear();
		bwd.clear();
		if (mode == 0) {//位置模式
			int fwdStage = kvFwdStage.getIntValue();
			for(int i = 1; i <= fwdStage; i++) {
				ParaNozzleFwd paraNozzleFwd = new ParaNozzleFwd(this.index, mode, i);
				paraNozzleFwd.init();
				paraNozzleFwd.refresh();
				fwd.add(paraNozzleFwd);			
			}	
			int bwdStage = kvBwdStage.getIntValue();
			for(int i = 1; i <= bwdStage; i++) {
				ParaNozzleBwd paraNozzleBwd = new ParaNozzleBwd(this.index, mode, i);
				paraNozzleBwd.init();
				paraNozzleBwd.refresh();
				bwd.add(paraNozzleBwd);			
			}	
		} else if (mode == 1) {//限位模式
			ParaNozzleFwd paraNozzleFwd = new ParaNozzleFwd(this.index, mode, 1);
			paraNozzleFwd.init();
			paraNozzleFwd.refresh();
			fwd.add(paraNozzleFwd);
			paraNozzleFwd = new ParaNozzleFwd(this.index, 2, 2);			
			paraNozzleFwd.init();
			paraNozzleFwd.refresh();
			fwd.add(paraNozzleFwd);
			ParaNozzleBwd paraNozzleBwd = new ParaNozzleBwd(this.index, mode, 1);
			paraNozzleBwd.init();
			paraNozzleBwd.refresh();
			bwd.add(paraNozzleBwd);
		} else if (mode == 2) {//时间模式
			ParaNozzleFwd paraNozzleFwd = new ParaNozzleFwd(this.index, mode, 2);
			paraNozzleFwd.init();
			paraNozzleFwd.refresh();
			fwd.add(paraNozzleFwd);
			ParaNozzleBwd paraNozzleBwd = new ParaNozzleBwd(this.index, mode, 1);
			paraNozzleBwd.init();
			paraNozzleBwd.refresh();
			bwd.add(paraNozzleBwd);
		}
		
		contact.refresh();
	}
	
	public int getMode() {
		return mode;
	}

	public List<ParaNozzleFwd> getFwd() {
		return fwd;
	}

	public List<ParaNozzleBwd> getBwd() {
		return bwd;
	}

	public ParaNozzleContact getContact() {
		return contact;
	}

	@Override
	public String toString() {
		JSONObject json = JSONObject.fromObject(this);
		return json.toString();
	}
	
	public class ParaNozzleFwd implements iMode {
		String press;
		String vel;
		String pos;
		
		int index, stage;
		int moveMode;
		
		KVariable kvPress;
		KVariable kvVel;
		KVariable kvPos;
		
		public ParaNozzleFwd(int index, int moveMode, int stage) {
			this.index = index;
			this.stage = stage;
			this.moveMode = moveMode;
		}

		@Override
		public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
			if(moveMode == 0) {//位置模式
				kvPress = HmiVariableService.getService().getVariable("Nozzle" + index + ".sv_NozzleFwdProfVis.Profile.Points[" + stage + "].rPressure");
				kvVel = HmiVariableService.getService().getVariable("Nozzle" + index + ".sv_NozzleFwdProfVis.Profile.Points[" + stage + "].rVelocity");
				kvPos = HmiVariableService.getService().getVariable("Nozzle" + index + ".sv_NozzleFwdProfVis.Profile.Points[" + (stage + 1) + "].rStartPos");
			} else {//时间及限位模式
				if(this.stage == 1) {
					kvPress = HmiVariableService.getService().getVariable("Nozzle" + index + ".sv_ConstFwdStage1.Pressure.Output.rOutputValue");
					kvVel = HmiVariableService.getService().getVariable("Nozzle" + index + ".sv_ConstFwdStage1.Velocity.Output.rOutputValue");
				} else if(this.stage == 2) {
					kvPress = HmiVariableService.getService().getVariable("Nozzle" + index + ".sv_ConstFwdStage2.Pressure.Output.rOutputValue");
					kvVel = HmiVariableService.getService().getVariable("Nozzle" + index + ".sv_ConstFwdStage2.Velocity.Output.rOutputValue");
				}
			}
		}

		@Override
		public void refresh() throws NetworkException, IOException, VartypeException {	
			final KVariable vars[] = {kvPress, kvVel};
			HmiVariableService.getService().readValues(vars);
			press = String.format("%1$.2f", kvPress.getDoubleValue()).replace(",", ".");
			vel = String.format("%1$.2f", kvVel.getDoubleValue()).replace(",", ".");
			if(null != kvPos){
				HmiVariableService.getService().readValue(kvPos);
				pos = String.format("%1$.2f", kvPos.getDoubleValue()).replace(",", ".");
			}
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
	
	public class ParaNozzleBwd implements iMode  {
		String press;
		String vel;
		String pos;
		
		int index, stage;
		int moveMode;
		
		KVariable kvPress;
		KVariable kvVel;
		KVariable kvPos;
		
		public ParaNozzleBwd(int index, int moveMode, int stage) {
			this.index = index;
			this.stage = stage;
			this.moveMode = moveMode;
		}

		@Override
		public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
			if(this.moveMode == 0) {//位置模式
				kvPress = HmiVariableService.getService().getVariable("Nozzle" + index + ".sv_NozzleBwdProfVis.Profile.Points[" + stage + "].rPressure");
				kvVel = HmiVariableService.getService().getVariable("Nozzle" + index + ".sv_NozzleBwdProfVis.Profile.Points[" + stage + "].rVelocity");
				kvPos = HmiVariableService.getService().getVariable("Nozzle" + index + ".sv_NozzleBwdProfVis.Profile.Points[" + (stage + 1) + "].rStartPos");
			} else { //限位模式，时间模式
				kvPress = HmiVariableService.getService().getVariable("Nozzle" + index + ".sv_ConstBwd.Pressure.Output.rOutputValue");
				kvVel = HmiVariableService.getService().getVariable("Nozzle" + index + ".sv_ConstBwd.Velocity.Output.rOutputValue");
			}
		}
		
		@Override
		public void refresh() throws NetworkException, IOException, VartypeException {
			final KVariable vars[] = {kvPress, kvVel};
			HmiVariableService.getService().readValues(vars);
			press = String.format("%1$.2f", kvPress.getDoubleValue());
			vel = String.format("%1$.2f", kvVel.getDoubleValue());
			if(null != kvPos){
				HmiVariableService.getService().readValue(kvPos);
				pos = String.format("%1$.2f", kvPos.getDoubleValue());
			}
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
	
	public class ParaNozzleContact implements iMode  {
		String press;
		String vel;
		String time;
		
		int index;
		
		KVariable kvPress;
		KVariable kvVel;
		KVariable kvTime;
		
		public ParaNozzleContact(int index) {
			this.index = index;
		}

		@Override
		public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
			kvPress = HmiVariableService.getService().getVariable("Nozzle" + index + ".sv_ConstFwdContactF.Pressure.Output.rOutputValue");
			kvVel = HmiVariableService.getService().getVariable("Nozzle" + index + ".sv_ConstFwdContactF.Velocity.Output.rOutputValue");
			kvTime = HmiVariableService.getService().getVariable("Nozzle" + index + ".sv_dNozzleFwdContactFSet");
		}
		
		@Override
		public void refresh() throws NetworkException, IOException, VartypeException {			
			final KVariable vars[] = {kvPress, kvVel, kvTime};
			HmiVariableService.getService().readValues(vars);
			press = String.format("%1$.2f", kvPress.getDoubleValue());
			vel = String.format("%1$.2f", kvVel.getDoubleValue());
			time = kvTime.getIntValue()+"";
		}

		public String getPress() {
			return press;
		}

		public String getVel() {
			return vel;
		}

		public String getTime() {
			return time;
		}

	}
	
	public static void main(String[] args) {
		ParaNozzle paraNozzle = new ParaNozzle(1);
		System.out.println(paraNozzle);
	}
}
