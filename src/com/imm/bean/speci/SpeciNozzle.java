package com.imm.bean.speci;

import java.io.IOException;

import com.imm.bean.iMode;
import com.keba.kemro.plc.network.NetworkException;
import com.keba.kemro.plc.service.HmiVariableService;
import com.keba.kemro.plc.variable.KVariable;
import com.keba.kemro.plc.variable.VarNotExistException;
import com.keba.kemro.plc.variable.VartypeException;

public class SpeciNozzle implements iMode {
	private String cylinderdiameter;// �͸�ֱ��
	private String pistonroddiameter;// ������ֱ��
	private String numcylinders;// �͸�����
	private String usesmallsize;// ��������ǰ����
	private String maxspeedfwd;// ���ǰ���ٶ�
	private String maxspeedbwd;// �������ٶ�

	private int index;

	private KVariable kvCylinderDiameter;
	private KVariable kvPistonrodDiameter;
	private KVariable kvNumCylinders;
	private KVariable kvUserSmallSize;
	private KVariable kvMaxSpeedFwd;
	private KVariable kvMaxSpeedBwd;

	public SpeciNozzle(int index) {
		this.index = index;
	}

	@Override
	public void init() {
		try {
			kvCylinderDiameter = HmiVariableService.getService()
					.getVariable("Nozzle" + index + ".sv_CylinderData.rCylinderDiameter");
			kvPistonrodDiameter = HmiVariableService.getService()
					.getVariable("Nozzle" + index + ".sv_CylinderData.rPistonRodDiameter");
			kvNumCylinders = HmiVariableService.getService()
					.getVariable("Nozzle" + index + ".sv_CylinderData.iNumCylinders");
			kvUserSmallSize = HmiVariableService.getService()
					.getVariable("Nozzle" + index + ".sv_CylinderData.bUseSmallSize");
			kvMaxSpeedFwd = HmiVariableService.getService().getVariable("Nozzle" + index + ".sv_rMaxSpeedFwd");
			kvMaxSpeedBwd = HmiVariableService.getService().getVariable("Nozzle" + index + ".sv_rMaxSpeedBwd");
		} catch (VartypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VarNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NetworkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void refresh() throws NetworkException, IOException, VartypeException {
		final KVariable vars[] = { kvCylinderDiameter, kvPistonrodDiameter, kvNumCylinders, kvUserSmallSize,
				kvMaxSpeedFwd, kvMaxSpeedBwd };
		HmiVariableService.getService().readValues(vars);		

		cylinderdiameter = String.format("%1$.2f", kvCylinderDiameter.getDoubleValue()).replace(",", ".");
		pistonroddiameter = String.format("%1$.2f", kvPistonrodDiameter.getDoubleValue()).replace(",", ".");
		numcylinders = kvNumCylinders.getIntValue()+"";
		usesmallsize = kvUserSmallSize.getBooleanValue()?"1":"0";
		maxspeedfwd = String.format("%1$.2f", kvMaxSpeedFwd.getDoubleValue()).replace(",", ".");
		maxspeedbwd = String.format("%1$.2f", kvMaxSpeedBwd.getDoubleValue()).replace(",", ".");
	}

	public String getCylinderdiameter() {
		return cylinderdiameter;
	}

	public String getPistonroddiameter() {
		return pistonroddiameter;
	}

	public String getNumcylinders() {
		return numcylinders;
	}

	public String getUsesmallsize() {
		return usesmallsize;
	}

	public String getMaxspeedfwd() {
		return maxspeedfwd;
	}

	public String getMaxspeedbwd() {
		return maxspeedbwd;
	}
}
