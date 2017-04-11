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

public class ParaInj implements iMode {
	private List<ParaInjInj> inj;
	private ParaInjCutoff cutoff;
	private List<ParaInjHold> hold;
	private List<ParaInjPlast> plast;
	private List<ParaInjDec> dec;
	
	private int index;
	
	private KVariable kvInjStage;
	private KVariable kvHoldStage;
	private KVariable kvPlastStage;
	
	public ParaInj(int index) {
		this.index = index;
		inj = new ArrayList<>();
		cutoff = new ParaInjCutoff(this.index);
		hold = new ArrayList<>();
		plast = new ArrayList<>();
		dec = new ArrayList<>();
		dec.add(new ParaInjDec(this.index, 0));//前松退
		dec.add(new ParaInjDec(this.index, 1));//后松退
	}
	
	@Override
	public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
		kvInjStage = HmiVariableService.getService().getVariable("Injection" + index + ".sv_InjectProfVis.Profile.iNoOfPoints");
		cutoff.init();
		kvHoldStage = HmiVariableService.getService().getVariable("Injection" + index + ".sv_HoldProfVis.Profile.iNoOfPoints");
		kvPlastStage = HmiVariableService.getService().getVariable("Injection" + index + ".sv_HoldProfVis.Profile.iNoOfPoints");
		for(ParaInjDec paraInjDec : dec) {
			paraInjDec.init();
		}
	}

	@Override
	public void refresh() throws VarNotExistException, NetworkException, IOException, VartypeException {
		final KVariable vars[] = {kvInjStage, kvHoldStage, kvPlastStage};
		HmiVariableService.getService().readValues(vars);
		
		int injStage = kvInjStage.getIntValue();
		inj.clear();
		for(int i = 0; i < injStage; i++) {
			ParaInjInj paraInjInj = new ParaInjInj(this.index, i);
			paraInjInj.init();
			paraInjInj.refresh();
			inj.add(paraInjInj);			
		}
		cutoff.refresh();
		int holdStage = kvHoldStage.getIntValue();
		hold.clear();
		for(int i = 0; i < holdStage; i++) {
			ParaInjHold paraInjHold = new ParaInjHold(this.index, i);
			paraInjHold.init();
			paraInjHold.refresh();
			hold.add(paraInjHold);			
		}		
		int plastStage = kvPlastStage.getIntValue();
		plast.clear();
		for(int i = 0; i < plastStage; i++) {
			ParaInjPlast paraInjPlast = new ParaInjPlast(this.index, i);
			paraInjPlast.init();
			paraInjPlast.refresh();
			plast.add(paraInjPlast);			
		}			
		for(ParaInjDec paraInjDec : dec) {
			paraInjDec.refresh();
		}
	}

	public List<ParaInjInj> getInj() {
		return inj;
	}

	public ParaInjCutoff getCutoff() {
		return cutoff;
	}

	public List<ParaInjHold> getHold() {
		return hold;
	}

	public List<ParaInjPlast> getPlast() {
		return plast;
	}

	public List<ParaInjDec> getDec() {
		return dec;
	}

	public class ParaInjInj implements iMode {
		String press;//压力
		String vel;//流量
		String pos;//位置
		
		int index, stage;
		
		KVariable kvPress;
		KVariable kvVel;
		KVariable kvPos;
		
		public ParaInjInj(int index, int stage) {
			this.index = index;
			this.stage = stage + 1;
		}

		@Override
		public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
			kvPress = HmiVariableService.getService().getVariable("Injection" + index + ".sv_InjectProfVis.Profile.Points[" + stage + "].rPressure");
			kvVel = HmiVariableService.getService().getVariable("Injection" + index + ".sv_InjectProfVis.Profile.Points[" + stage + "].rVelocity");
			kvPos = HmiVariableService.getService().getVariable("Injection" + index + ".sv_InjectProfVis.Profile.Points[" + (stage + 1) + "].rStartPos");
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
	
	public class ParaInjCutoff implements iMode  {
		boolean posmode;//位置转保压模式激活
		String pos;//转保压位置
		boolean timemode; //时间转保压模式激活
		String time;//转保压时间
		boolean pressmode;//压力转保压模式
		String press;//转保压压力
		
		int index;
		
		KVariable kvPosMode;
		KVariable kvPos;
		KVariable kvTimeMode;
		KVariable kvTime;
		KVariable kvPressMode;
		KVariable kvPress;
		
		public ParaInjCutoff(int index) {
			this.index = index;
		}

		@Override
		public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
			kvPosMode = HmiVariableService.getService().getVariable("Injection" + index + ".sv_CutOffParams.bUsePosition");
			kvPos = HmiVariableService.getService().getVariable("Injection" + index + ".sv_CutOffParams.rPositionThreshold");
			kvTimeMode = HmiVariableService.getService().getVariable("Injection" + index + ".sv_CutOffParams.bUseTimer");
			kvTime = HmiVariableService.getService().getVariable("Injection" + index + ".sv_CutOffParams.dTimeThreshold");
			kvPressMode = HmiVariableService.getService().getVariable("Injection" + index + ".sv_CutOffParams.bUseInjectPressure");
			kvPress = HmiVariableService.getService().getVariable("Injection" + index + ".sv_CutOffParams.rInjectPressureThreshold");
			
		}

		@Override
		public void refresh() throws NetworkException, IOException, VartypeException {
			final KVariable vars[] = {kvPosMode, kvPos, kvTimeMode, kvTime, kvPressMode, kvPress};
			HmiVariableService.getService().readValues(vars);
			
			posmode = kvPosMode.getBooleanValue();
			pos = String.format("%1$.2f", kvPos.getDoubleValue()).replace(",", ".");
			timemode = kvTimeMode.getBooleanValue();
			time = kvTime.getIntValue()+"";
			pressmode = kvPressMode.getBooleanValue();	
			press = String.format("%1$.2f", kvPress.getDoubleValue()).replace(",", ".");
		}

		public boolean getPosmode() {
			return posmode;
		}

		public String getPos() {
			return pos;
		}

		public boolean getTimemode() {
			return timemode;
		}

		public String getTime() {
			return time;
		}

		public boolean getPressmode() {
			return pressmode;
		}

		public String getPress() {
			return press;
		}
	}
	
	public class ParaInjHold implements iMode {
		String press;//压力
		String vel;//流量
		String time;//时间
		
		int index, stage;
		
		KVariable kvPress;
		KVariable kvVel;
		KVariable kvTime;
		
		public ParaInjHold(int index, int stage) {
			this.index = index;
			this.stage = stage + 1;
		}

		@Override
		public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
			kvPress = HmiVariableService.getService().getVariable("Injection" + index + ".sv_HoldProfVis.Profile.Points[" + stage + "].rPressure");
			kvVel = HmiVariableService.getService().getVariable("Injection" + index + ".sv_HoldProfVis.Profile.Points[" + stage + "].rVelocity");
			kvTime = HmiVariableService.getService().getVariable("Injection" + index + ".sv_HoldProfVis.Profile.Points[" + (stage + 1) + "].rStartPos");
		}

		@Override
		public void refresh() throws NetworkException, IOException, VartypeException {
			final KVariable vars[] = {kvPress, kvVel, kvTime};
			HmiVariableService.getService().readValues(vars);
		
			press = String.format("%1$.2f", kvPress.getDoubleValue()).replace(",", ".");
			vel = String.format("%1$.2f", kvVel.getDoubleValue()).replace(",", ".");
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
	
	public class ParaInjPlast implements iMode {
		String press;//压力
		String rot;//转速
		String backpress;//背压
		String pos;//位置
		
		int index, stage;
		
		KVariable kvPress;
		KVariable kvRot;
		KVariable kvBackPress;
		KVariable kvPos;
		
		public ParaInjPlast(int index, int stage) {
			this.index = index;
			this.stage = stage + 1;
		}

		@Override
		public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
			kvPress = HmiVariableService.getService().getVariable("Injection" + index + ".sv_PlastProfVis.Profile.Points[" + stage + "].rPressure");
			kvRot = HmiVariableService.getService().getVariable("Injection" + index + ".sv_PlastProfVis.Profile.Points[" + stage + "].rRotation");
			kvBackPress = HmiVariableService.getService().getVariable("Injection" + index + ".sv_PlastProfVis.Profile.Points[" + stage + "].rBackPressure");
			kvPos = HmiVariableService.getService().getVariable("Injection" + index + ".sv_PlastProfVis.Profile.Points[" + stage + "].rStartPos");
		}

		@Override
		public void refresh() throws NetworkException, IOException, VartypeException {
			final KVariable vars[] = {kvPress, kvRot, kvBackPress, kvPos};
			HmiVariableService.getService().readValues(vars);
			
			press = String.format("%1$.2f", kvPress.getDoubleValue()).replace(",", ".");
			rot = String.format("%1$.2f", kvRot.getDoubleValue()).replace(",", ".");
			backpress = String.format("%1$.2f", kvBackPress.getDoubleValue()).replace(",", ".");
			pos = String.format("%1$.2f", kvPos.getDoubleValue()).replace(",", ".");
		}

		public String getPress() {
			return press;
		}

		public String getRot() {
			return rot;
		}

		public String getBackpress() {
			return backpress;
		}

		public String getPos() {
			return pos;
		}
	}
	
	public class ParaInjDec implements iMode {
		int mod;//松退模式
		String press;//压力
		String vel;//流量
		String pos;//位置
		String time;//时间
		
		int index, stage;
		
		KVariable kvMod;
		KVariable kvPress;
		KVariable kvVel;
		KVariable kvPos;
		KVariable kvTime;
		
		public ParaInjDec(int index, int stage) {
			this.index = index;
			this.stage = stage + 1;
		}

		@Override
		public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
			if (this.stage == 1) {
				kvMod = HmiVariableService.getService().getVariable("Injection" + index + ".sv_DecompBefPlastSettings.Mode");
				kvPress = HmiVariableService.getService().getVariable("Injection" + index + ".sv_DecompBefPlastSettings.ConstOutput.Pressure.Output.rOutputValue");
				kvVel = HmiVariableService.getService().getVariable("Injection" + index + ".sv_DecompBefPlastSettings.ConstOutput.Velocity.Output.rOutputValue");
				kvPos = HmiVariableService.getService().getVariable("Injection" + index + ".sv_DecompBefPlastSettings.rDecompPos");
				kvTime = HmiVariableService.getService().getVariable("Injection" + index + ".sv_DecompBefPlastSettings.dDecompTime");
			} else if (this.stage == 2) {
				kvMod = HmiVariableService.getService().getVariable("Injection" + index + ".sv_DecompAftPlastSettings.Mode");
				kvPress = HmiVariableService.getService().getVariable("Injection" + index + ".sv_DecompAftPlastSettings.ConstOutput.Pressure.Output.rOutputValue");
				kvVel = HmiVariableService.getService().getVariable("Injection" + index + ".sv_DecompAftPlastSettings.ConstOutput.Velocity.Output.rOutputValue");
				kvPos = HmiVariableService.getService().getVariable("Injection" + index + ".sv_DecompAftPlastSettings.rDecompPos");
				kvTime = HmiVariableService.getService().getVariable("Injection" + index + ".sv_DecompAftPlastSettings.dDecompTime");
			}
		}

		@Override
		public void refresh() throws NetworkException, IOException, VartypeException {
			final KVariable vars[] = {kvMod, kvPress, kvVel, kvPos, kvTime};
			HmiVariableService.getService().readValues(vars);
			
			mod = kvMod.getIntValue();			

			press = String.format("%1$.2f", kvPress.getDoubleValue()).replace(",", ".");
			vel = String.format("%1$.2f", kvVel.getDoubleValue()).replace(",", ".");
			pos = String.format("%1$.2f", kvPos.getDoubleValue()).replace(",", ".");
			time = kvTime.getIntValue()+"";
		}

		public int getMod() {
			return mod;
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

		public String getTime() {
			return time;
		}
	}
	
	@Override
	public String toString() {
		JSONObject json = JSONObject.fromObject(this);
		return json.toString();
	}	
	
	public static void main(String[] args) {
		ParaInj inj = new ParaInj(1);
		System.out.println(inj);
	}
}
