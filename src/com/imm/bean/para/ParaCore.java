package com.imm.bean.para;

import java.io.IOException;

import com.imm.bean.iMode;
import com.keba.kemro.plc.network.NetworkException;
import com.keba.kemro.plc.service.HmiVariableService;
import com.keba.kemro.plc.variable.KVariable;
import com.keba.kemro.plc.variable.VarNotExistException;
import com.keba.kemro.plc.variable.VartypeException;

import net.sf.json.JSONObject;

public class ParaCore implements iMode {
	private ParaCoreIn in;
	private ParaCoreOut out;
	
	private int index;
	
	public ParaCore(int index) {
		this.index = index;
		in = new ParaCoreIn(this.index);
		out = new ParaCoreOut(this.index);
	}
	
	@Override
	public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
		in.init();
		out.init();
	}

	@Override
	public void refresh() throws VarNotExistException, NetworkException, IOException, VartypeException {
		in.refresh();
		out.refresh();
	}
	
	public ParaCoreIn getIn() {
		return in;
	}

	public ParaCoreOut getOut() {
		return out;
	}

	public class ParaCoreIn implements iMode {
		int activemode;//���ӽ�����ģʽ��0.�رա�1.��ģǰ��2.��ģ�С�3.��ģ��
		String activepos;//���ӽ�����λ��
		int movemode; //���Ӷ���ģʽ��1.�رա�2.��λ��3.ʱ��
		String press;//ѹ��
		String vel;//����
		String time;//ʱ��
		
		int index;
		
		KVariable kvActiveMode;
		KVariable kvActivePos;
		KVariable kvMoveMode;
		KVariable kvPress;
		KVariable kvVel;
		KVariable kvTime;
		
		public ParaCoreIn(int index) {
			this.index = index;
		}

		@Override
		public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
			kvActiveMode = HmiVariableService.getService().getVariable("CentralCoordination1.sv_CoreData[" + index + "].InMode");
			kvActivePos = HmiVariableService.getService().getVariable("CentralCoordination1.sv_CoreData[" + index + "].rCoreInPosition");
			kvMoveMode = HmiVariableService.getService().getVariable("Core" + index + ".sv_CoreMode.CoreControlIn");
			kvPress = HmiVariableService.getService().getVariable("Core" + index + ".sv_CoreOutput.NormalIn.Pressure.Output.rOutputValue");
			kvVel = HmiVariableService.getService().getVariable("Core" + index + ".sv_CoreOutput.NormalIn.Velocity.Output.rOutputValue");
			kvTime = HmiVariableService.getService().getVariable("Core" + index + ".sv_CoreSetTimes.MoveIn.dSetMoveTime");
		}

		@Override
		public void refresh() throws NetworkException, IOException, VartypeException {
			final KVariable vars[] = {kvActiveMode, kvActivePos, kvMoveMode, kvPress, kvVel, kvTime};
			HmiVariableService.getService().readValues(vars);
			
			activemode = kvActiveMode.getIntValue();
			activepos = String.format("%1$.2f", kvActivePos.getDoubleValue()).replace(",", ".");
			movemode = kvMoveMode.getIntValue();
			press = String.format("%1$.2f", kvPress.getDoubleValue()).replace(",", ".");
			vel = String.format("%1$.2f", kvVel.getDoubleValue()).replace(",", ".");
			time = kvTime.getIntValue()+"";
		}

		public int getActivemode() {
			return activemode;
		}

		public String getActivepos() {
			return activepos;
		}

		public int getMovemode() {
			return movemode;
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
	
	public class ParaCoreOut implements iMode  {
		int activemode;//����������ģʽ��0.�رա�1.��ģǰ��2.��ģ�С�3.��ģ��
		String activepos;//����������λ��
		int movemode; //�����˶���ģʽ��1.�رա�2.��λ��3.ʱ��
		String press;//ѹ��
		String vel;//����
		String time;//ʱ��
		
		int index;
		
		KVariable kvActiveMode;
		KVariable kvActivePos;
		KVariable kvMoveMode;
		KVariable kvPress;
		KVariable kvVel;
		KVariable kvTime;
		
		public ParaCoreOut(int index) {
			this.index = index;
		}

		@Override
		public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
			kvActiveMode = HmiVariableService.getService().getVariable("CentralCoordination1.sv_CoreData[" + index + "].OutMode");
			kvActivePos = HmiVariableService.getService().getVariable("CentralCoordination1.sv_CoreData[" + index + "].rCoreOutPosition");
			kvMoveMode = HmiVariableService.getService().getVariable("Core" + index + ".sv_CoreMode.CoreControlOut");
			kvPress = HmiVariableService.getService().getVariable("Core" + index + ".sv_CoreOutput.NormalOut.Pressure.Output.rOutputValue");
			kvVel = HmiVariableService.getService().getVariable("Core" + index + ".sv_CoreOutput.NormalOut.Velocity.Output.rOutputValue");
			kvTime = HmiVariableService.getService().getVariable("Core" + index + ".sv_CoreSetTimes.MoveOut.dSetMoveTime");
		}

		@Override
		public void refresh() throws NetworkException, IOException, VartypeException {
			final KVariable vars[] = {kvActiveMode, kvActivePos, kvMoveMode, kvPress, kvVel, kvTime};
			HmiVariableService.getService().readValues(vars);
			
			activemode = kvActiveMode.getIntValue();
			activepos = String.format("%1$.2f", kvActivePos.getDoubleValue());
			movemode = kvMoveMode.getIntValue();
			press = String.format("%1$.2f", kvPress.getDoubleValue());
			vel = String.format("%1$.2f", kvVel.getDoubleValue());
			time =  kvTime.getIntValue()+"";
		}

		public int getActivemode() {
			return activemode;
		}

		public String getActivepos() {
			return activepos;
		}

		public int getMovemode() {
			return movemode;
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
	
	@Override
	public String toString() {
		JSONObject json = JSONObject.fromObject(this);
		return json.toString();
	}	
	
	public static void main(String[] args) {
		ParaCore core = new ParaCore(1);
		System.out.println(core);
	}
}
