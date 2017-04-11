package com.imm.bean.state;

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

public class State implements iMode {
	public static String TOPIC = "/v1.0/recorder/1/update/state";
	
	private String rtc;
	private String oiltemp;
	private String prodtimeact;
	private String prodtimetotal;
	
	private List<StateInj> nozzles;
	private List<StateMold> molds;
	
	private KVariable kvRTC;
	private KVariable kvOilTemp;
	private KVariable kvProdTimeAct;
	private KVariable kvProdTimeTotal;
	
	public State() {
		nozzles = new ArrayList<StateInj>();
		nozzles.add(new StateInj(1));
		molds = new ArrayList<StateMold>();
//		molds.add(new StateMold(1));
	}
	
	@Override
	public void init() {
		try {
			kvRTC = HmiVariableService.getService().getVariable("EasyNet.sv_dRTC"); 
			kvOilTemp = HmiVariableService.getService().getVariable("OilMaintenance1.ti_OilTemp"); 
			kvProdTimeAct = HmiVariableService.getService().getVariable("OperationMode1.sv_rProdTimeAct"); 
			kvProdTimeTotal = HmiVariableService.getService().getVariable("OperationMode1.sv_rProdTimeTotal"); 
			for(StateInj inj:nozzles) {
				inj.init();
			}
			for(StateMold mold:molds) {
				mold.init();
			}
			this.refresh();
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
	public void refresh() throws VarNotExistException, NetworkException, IOException, VartypeException {
		final KVariable vars[] = { kvRTC, kvOilTemp, kvProdTimeAct, kvProdTimeTotal};
		HmiVariableService.getService().readValues(vars);
			
		rtc = kvRTC.getDateValue().getTime()+"";
		oiltemp = String.format("%1$.1f", kvOilTemp.getDoubleValue());
		prodtimeact = kvProdTimeAct.getIntValue()+"";
		prodtimetotal = kvProdTimeTotal.getIntValue()+"";
		for(StateInj inj:nozzles) {
			inj.refresh();
		}
		for(StateMold mold:molds) {
			mold.refresh();
		}
	}

	public String getRtc() {
		return rtc;
	}

	public void setRtc(String rtc) {
		this.rtc = rtc;
	}

	public String getOiltemp() {
		return oiltemp;
	}

	public void setOiltemp(String oiltemp) {
		this.oiltemp = oiltemp;
	}

	public String getProdtimeact() {
		return prodtimeact;
	}

	public void setProdtimeact(String prodtimeact) {
		this.prodtimeact = prodtimeact;
	}

	public String getProdtimetotal() {
		return prodtimetotal;
	}

	public void setProdtimetotal(String prodtimetotal) {
		this.prodtimetotal = prodtimetotal;
	}
	
	public List<StateInj> getNozzles() {
		return nozzles;
	}

	public void setNozzles(List<StateInj> nozzles) {
		this.nozzles = nozzles;
	}

	public List<StateMold> getMolds() {
		return molds;
	}

	public void setMolds(List<StateMold> molds) {
		this.molds = molds;
	}

	@Override
	public String toString() {
		JSONObject json = JSONObject.fromObject(this);
		return json.toString();
	}

	public static void main(String[] args) {
		State state = new State();
		System.out.println(state);
	}
}
