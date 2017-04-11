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

public class ParaHeating implements iMode {
	private List<ParaHeatingZone> heating;
	
	private int index;
	
	private KVariable kvHeatingZones;
	
	public ParaHeating(int index) {
		this.index = index;
		heating = new ArrayList<>();
	}
	
	@Override
	public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
		kvHeatingZones = HmiVariableService.getService().getVariable("HeatingNozzle" + index + ".sv_iNumberOfZones");
	}

	@Override
	public void refresh() throws VarNotExistException, NetworkException, IOException, VartypeException {
		final KVariable vars[] = {kvHeatingZones};
		HmiVariableService.getService().readValues(vars);

		int heatingzonenum = kvHeatingZones.getIntValue();
		heating.clear();
		for (int i = 0; i < heatingzonenum; i++) {
			ParaHeatingZone paraHeatingZone = new ParaHeatingZone(index, i);
			paraHeatingZone.init();
			paraHeatingZone.refresh();
			heating.add(paraHeatingZone);
		}
	}
	
	public List<ParaHeatingZone> getHeating() {
		return heating;
	}

	@Override
	public String toString() {
		JSONObject json = JSONObject.fromObject(this);
		return json.toString();
	}
	
	public class ParaHeatingZone implements iMode {
		private String set;
		private String up;
		private String low;
		
		int index, zone;
		
		KVariable kvSet;
		KVariable kvUp;
		KVariable kvLow;
		
		public ParaHeatingZone(int index, int zone) {
			this.index = index;
			this.zone = zone + 1;
		}

		@Override
		public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
			kvSet = HmiVariableService.getService().getVariable("HeatingNozzle" + index + ".sv_ZoneRetain" + zone + ".rSetValVis");
			kvUp = HmiVariableService.getService().getVariable("HeatingNozzle" + index + ".sv_ZoneRetain" + zone + ".rUpperTolVis");
			kvLow = HmiVariableService.getService().getVariable("HeatingNozzle" + index + ".sv_ZoneRetain" + zone + ".rLowerTolVis");
		}
		
		@Override
		public void refresh() throws NetworkException, IOException, VartypeException {
			final KVariable vars[] = {kvSet, kvUp, kvLow};
			HmiVariableService.getService().readValues(vars);
			
			set = String.format("%1$.2f", kvSet.getDoubleValue()).replace(",", ".");
			up = String.format("%1$.2f", kvUp.getDoubleValue()).replace(",", ".");
			low = String.format("%1$.2f", kvLow.getDoubleValue()).replace(",", ".");
		}

		public String getSet() {
			return set;
		}

		public String getUp() {
			return up;
		}

		public String getLow() {
			return low;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(new ParaHeating(1));
	}
}



