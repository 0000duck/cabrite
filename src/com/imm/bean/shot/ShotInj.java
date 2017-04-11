package com.imm.bean.shot;

import java.io.IOException;

import com.imm.bean.iMode;
import com.keba.kemro.plc.network.NetworkException;
import com.keba.kemro.plc.service.HmiVariableService;
import com.keba.kemro.plc.variable.KVariable;
import com.keba.kemro.plc.variable.VarNotExistException;
import com.keba.kemro.plc.variable.VartypeException;

import net.sf.json.JSONObject;

public class ShotInj implements iMode{
	private String cutoffpress;// ת��ѹѹ��
	private String cutoffpos;// ת��ѹλ��
	private String cushion;// ��ѹ��Сλ��
	private String holdpos;// ��ѹ����λ��(�佺�յ�λ��)
	private String injpeakvel;// ���ٷ�ֵ
	private String injpeakpress;//��ѹ��ֵ
	private String injtime;// ע��ʱ��
	private String plastendpos;// �����յ�λ��
	private String plasttime;// ����ʱ��

	private KVariable kvarCutOffPress;
	private KVariable kvarCutOffPos;
	private KVariable kvarCushion;
	private KVariable kvarHoldPos;
	private KVariable kvarInjPeakVel;
	private KVariable kvarInjPeakPress;
	private KVariable kvarInjTime;
	private KVariable kvarPlastEndPos;
	private KVariable kvarPlastTime;
	
	private int index;
	
	public ShotInj(int index) {
		this.index = index;
	}
	
	@Override
	public void init() {
		try {
			kvarCutOffPress = HmiVariableService.getService().getVariable("Injection" + index + ".sv_rCutOffPressure");
			kvarCutOffPos = HmiVariableService.getService().getVariable("Injection" + index + ".sv_rCutOffPosition");
			kvarCushion = HmiVariableService.getService().getVariable("Injection" + index + ".sv_rCushion");
			kvarHoldPos = HmiVariableService.getService().getVariable("Injection" + index + ".sv_rHoldPosition");
			kvarInjPeakVel = HmiVariableService.getService().getVariable("Injection" + index + ".sv_rInjPeakVelocity");
			kvarInjPeakPress = HmiVariableService.getService().getVariable("Injection" + index + ".sv_rInjPeakPressure");
			kvarInjTime = HmiVariableService.getService().getVariable("Injection" + index + ".sv_InjectTimesAct.dActMoveTime");
			kvarPlastEndPos = HmiVariableService.getService().getVariable("Injection" + index + ".sv_rPlastEndPosition");
			kvarPlastTime = HmiVariableService.getService().getVariable("Injection" + index + ".sv_PlastTimesAct.dActMoveTime");
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
		final KVariable vars[] = { kvarCutOffPress, kvarCutOffPos, kvarCushion, kvarHoldPos,
				kvarInjPeakVel, kvarInjPeakPress, kvarInjTime, kvarPlastEndPos, kvarPlastTime};
		HmiVariableService.getService().readValues(vars);
		cutoffpress = String.format("%1$.2f", kvarCutOffPress.getDoubleValue()).replace(',', '.');
		cutoffpos = String.format("%1$.2f", kvarCutOffPos.getDoubleValue()).replace(',', '.');
		cushion = String.format("%1$.2f", kvarCushion.getDoubleValue()).replace(',', '.');
		holdpos = String.format("%1$.2f", kvarHoldPos.getDoubleValue()).replace(',', '.');
		injpeakvel = String.format("%1$.2f", kvarInjPeakVel.getDoubleValue()).replace(',', '.');
		injpeakpress = String.format("%1$.2f", kvarInjPeakPress.getDoubleValue()).replace(',', '.');
		injtime = kvarInjTime.getIntValue()+"";
		plastendpos = String.format("%1$.2f", kvarPlastEndPos.getDoubleValue()).replace(',', '.');
		plasttime = String.format("%1$d", kvarPlastTime.getIntValue()).replace(',', '.');
	}

	public String getCutoffpress() {
		return cutoffpress;
	}

	public void setCutoffpress(String cutoffpress) {
		this.cutoffpress = cutoffpress;
	}

	public String getCutoffpos() {
		return cutoffpos;
	}

	public void setCutoffpos(String cutoffpos) {
		this.cutoffpos = cutoffpos;
	}

	public String getCushion() {
		return cushion;
	}

	public void setCushion(String cushion) {
		this.cushion = cushion;
	}

	public String getHoldpos() {
		return holdpos;
	}

	public void setHoldpos(String holdpos) {
		this.holdpos = holdpos;
	}

	public String getInjpeakvel() {
		return injpeakvel;
	}

	public void setInjpeakvel(String injpeakvel) {
		this.injpeakvel = injpeakvel;
	}

	public String getInjpeakpress() {
		return injpeakpress;
	}

	public void setInjpeakpress(String injpeakpress) {
		this.injpeakpress = injpeakpress;
	}

	public String getInjtime() {
		return injtime;
	}

	public void setInjtime(String injtime) {
		this.injtime = injtime;
	}

	public String getPlastendpos() {
		return plastendpos;
	}

	public void setPlastendpos(String plastendpos) {
		this.plastendpos = plastendpos;
	}

	public String getPlasttime() {
		return plasttime;
	}

	public void setPlasttime(String plasttime) {
		this.plasttime = plasttime;
	}

	@Override
	public String toString() {
		JSONObject json = JSONObject.fromObject(this);
		return json.toString();
	}
	
	public static void main(String[] args) {
		ShotInj injShot = new ShotInj(1);
		System.out.println(injShot);
	}
}
