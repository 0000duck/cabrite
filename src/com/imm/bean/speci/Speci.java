package com.imm.bean.speci;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.imm.bean.iMode;
import com.keba.kemro.plc.network.NetworkException;
import com.keba.kemro.plc.variable.VartypeException;

import net.sf.json.JSONObject;

public class Speci implements iMode {
	public static String TOPIC = "/v1.0/recorder/1/update/speci";
	
	private List<SpeciMold> molds;
	private List<SpeciEjector> ejectors;
	private List<SpeciNozzle> nozzles;
	private List<SpeciInj> injestions;
	
	public Speci() {
		molds = new ArrayList<SpeciMold>();
		molds.add(new SpeciMold(1));
		ejectors = new ArrayList<SpeciEjector>();
		ejectors.add(new SpeciEjector(1));
		nozzles = new ArrayList<SpeciNozzle>();
		nozzles.add(new SpeciNozzle(1));
		injestions = new ArrayList<SpeciInj>();
		injestions.add(new SpeciInj(1));
	}
	
	@Override
	public void init() throws VartypeException, NetworkException, IOException {
		for(SpeciMold mold : molds){
			mold.init();
		}
		for(SpeciEjector ejector : ejectors){
			ejector.init();
		}
		for(SpeciNozzle nozzle : nozzles){
			nozzle.init();
		}
		for(SpeciInj inj : injestions){
			inj.init();
		}
		this.refresh();
	}

	@Override
	public void refresh() throws VartypeException, NetworkException, IOException {
		for(SpeciMold mold : molds){
			mold.refresh();
		}
		for(SpeciEjector ejector : ejectors){
			ejector.refresh();
		}
		for(SpeciNozzle nozzle : nozzles){
			nozzle.refresh();
		}
		for(SpeciInj inj : injestions){
			inj.refresh();
		}
	}

	public List<SpeciMold> getMolds() {
		return molds;
	}

	public void setMolds(List<SpeciMold> molds) {
		this.molds = molds;
	}

	public List<SpeciEjector> getEjectors() {
		return ejectors;
	}

	public void setEjectors(List<SpeciEjector> ejectors) {
		this.ejectors = ejectors;
	}

	public List<SpeciNozzle> getNozzles() {
		return nozzles;
	}

	public void setNozzles(List<SpeciNozzle> nozzles) {
		this.nozzles = nozzles;
	}

	public List<SpeciInj> getInjestions() {
		return injestions;
	}

	public void setInjestions(List<SpeciInj> injestions) {
		this.injestions = injestions;
	}

	@Override
	public String toString() {
		JSONObject json = JSONObject.fromObject(this);
		return json.toString();
	}

	public static void main(String[] args) {
		Speci move = new Speci();
		System.out.println(move);
	}
}
