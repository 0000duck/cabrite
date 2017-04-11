package com.imm.bean.speci;

import java.io.IOException;

import com.imm.bean.iMode;
import com.keba.kemro.plc.network.NetworkException;
import com.keba.kemro.plc.service.HmiVariableService;
import com.keba.kemro.plc.variable.KVariable;
import com.keba.kemro.plc.variable.VarNotExistException;
import com.keba.kemro.plc.variable.VartypeException;

public class SpeciInj implements iMode {
	private String cylinderdiameter;// 油缸直径
	private String pistonroddiameter;// 活塞杆直径
	private String numcylinders;// 油缸数量
	private String usesmallsize;// 活塞杆在前进侧
	private String maxspeedfwd;// 最大前进速度
	private String maxspeedbwd;// 最大后退速度
	private String maxrotationplast;// 最大储料转速
	private String screwdiameter;//螺杆直径

	private int index;

	private KVariable kvCylinderDiameter;
	private KVariable kvPistonrodDiameter;
	private KVariable kvNumCylinders;
	private KVariable kvUserSmallSize;
	private KVariable kvMaxSpeedFwd;
	private KVariable kvMaxSpeedBwd;
	private KVariable kvMaxRotationPlast;
	private KVariable kvMaxScrewDiameter;

	public SpeciInj(int index) {
		this.index = index;
	}

	@Override
	public void init() {
		try {
			kvCylinderDiameter = HmiVariableService.getService()
					.getVariable("Injection" + index + ".sv_CylinderData.rCylinderDiameter");
			kvPistonrodDiameter = HmiVariableService.getService()
					.getVariable("Injection" + index + ".sv_CylinderData.rPistonRodDiameter");
			kvNumCylinders = HmiVariableService.getService()
					.getVariable("Injection" + index + ".sv_CylinderData.iNumCylinders");
			kvUserSmallSize = HmiVariableService.getService()
					.getVariable("Injection" + index + ".sv_CylinderData.bUseSmallSize");
			kvMaxSpeedFwd = HmiVariableService.getService().getVariable("Injection" + index + ".sv_rMaxSpeedFwdSpec");
			kvMaxSpeedBwd = HmiVariableService.getService().getVariable("Injection" + index + ".sv_rMaxSpeedBwd");
			kvMaxRotationPlast = HmiVariableService.getService().getVariable("Injection" + index + ".sv_rMaxRotationPlast");
			kvMaxScrewDiameter = HmiVariableService.getService().getVariable("Injection" + index + ".sv_rScrewDiameter");
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
	public void refresh() throws VartypeException, NetworkException, IOException {
		final KVariable vars[] = { kvCylinderDiameter, kvPistonrodDiameter, kvNumCylinders, kvUserSmallSize,
				kvMaxSpeedFwd, kvMaxSpeedBwd };
		HmiVariableService.getService().readValues(vars);

		cylinderdiameter = String.format("%1$.2f", kvCylinderDiameter.getDoubleValue()).replace(",", ".");
		pistonroddiameter = String.format("%1$.2f", kvPistonrodDiameter.getDoubleValue()).replace(",", ".");
		numcylinders = kvNumCylinders.getIntValue()+"";
		usesmallsize = kvUserSmallSize.getBooleanValue()?"1":"0";;
		maxspeedfwd = String.format("%1$.2f", kvMaxSpeedFwd.getDoubleValue()).replace(",", ".");
		maxspeedbwd = String.format("%1$.2f", kvMaxSpeedBwd.getDoubleValue()).replace(",", ".");
		maxrotationplast = String.format("%1$.2f", kvMaxRotationPlast.getDoubleValue()).replace(",", ".");
		screwdiameter = String.format("%1$.2f", kvMaxScrewDiameter.getDoubleValue()).replace(",", ".");
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

	public String getMaxrotationplast() {
		return maxrotationplast;
	}

	public String getScrewdiameter() {
		return screwdiameter;
	}
}
