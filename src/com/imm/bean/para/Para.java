package com.imm.bean.para;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.imm.bean.iMode;
import com.keba.kemro.plc.network.NetworkException;
import com.keba.kemro.plc.service.HmiVariableService;
import com.keba.kemro.plc.variable.KVariable;
import com.keba.kemro.plc.variable.VarNotExistException;
import com.keba.kemro.plc.variable.VartypeException;

import net.sf.json.JSONObject;

public class Para implements iMode {
	public static String TOPIC = "/v1.0/recorder/1/update/para";
	
	private String rtc;//当前时间
	private String molddata;//模具型号
	private List<ParaMold> molds;//模具参数
	private List<ParaEjector> ejectors;//顶针参数
	private List<ParaCore> cores;//中子参数
	private List<ParaAirValve> airvalves;//气阀参数
	private List<ParaInj> injestions;//射台参数
	private List<ParaNozzle> nozzles;//射台参数
	private List<ParaHeating> heatings;//射台参数

	private KVariable kvRTC;
	private KVariable kvMoldData;
	
	public Para() {
		molds = new ArrayList<ParaMold>();
		molds.add(new ParaMold(1));
		ejectors = new ArrayList<ParaEjector>();
		ejectors.add(new ParaEjector(1));
		cores = new ArrayList<ParaCore>();
		cores.add(new ParaCore(1));
		airvalves = new ArrayList<ParaAirValve>();
//		airvalves.add(new MoveAirValve(1));
		injestions = new ArrayList<ParaInj>();
		injestions.add(new ParaInj(1));
		nozzles = new ArrayList<>();
		nozzles.add(new ParaNozzle(1));
		heatings = new ArrayList<>();
		heatings.add(new ParaHeating(1));
	}
	
	@Override
	public void init() throws VartypeException, VarNotExistException, IOException, NetworkException {
		kvRTC = HmiVariableService.getService().getVariable("EasyNet.sv_dRTC");
		kvMoldData = HmiVariableService.getService().getVariable("system.sv_sMoldData");
		for (ParaMold mold : molds) {
			mold.init();
		}
		for (ParaEjector ejector : ejectors) {
			ejector.init();
		}
		for (ParaCore core : cores) {
			core.init();
		}
		for (ParaAirValve airValve : airvalves) {
			airValve.init();
		}
		for (ParaInj inj : injestions) {
			inj.init();
		}
		for (ParaNozzle nozzle : nozzles) {
			nozzle.init();
		}
		for (ParaHeating heating : heatings) {
			heating.init();
		}
		this.refresh();
	}

	@Override
	public void refresh() throws VarNotExistException, VartypeException, NetworkException, IOException {
		final KVariable vars[] = { kvRTC, kvMoldData };
		HmiVariableService.getService().readValues(vars);

		rtc = kvRTC.getDateValue().getTime()+"";
		molddata = kvMoldData.getStringValue();
		for (ParaMold mold : molds) {
			mold.refresh();
		}
		for (ParaEjector ejector : ejectors) {
			ejector.refresh();
		}
		for (ParaCore core : cores) {
			core.refresh();
		}
		for (ParaAirValve airValve : airvalves) {
			airValve.refresh();
		}
		for (ParaInj inj : injestions) {
			inj.refresh();
		}
		for (ParaNozzle nozzle : nozzles) {
			nozzle.refresh();
		}
		for (ParaHeating heating : heatings) {
			heating.refresh();
		}
	}

	public String getRtc() {
		return rtc;
	}

	public String getMolddata() {
		return molddata;
	}

	public List<ParaMold> getMolds() {
		return molds;
	}

	public List<ParaEjector> getEjectors() {
		return ejectors;
	}

	public List<ParaCore> getCores() {
		return cores;
	}

	public List<ParaAirValve> getAirvalves() {
		return airvalves;
	}

	public List<ParaInj> getInjestions() {
		return injestions;
	}

	public List<ParaNozzle> getNozzles() {
		return nozzles;
	}

	public List<ParaHeating> getHeatings() {
		return heatings;
	}

	@Override
	public String toString() {
		JSONObject json = JSONObject.fromObject(this);
		return json.toString();
	}

	public static void main(String[] args) {
		Para move = new Para();
		System.out.println(move);
	}
}
