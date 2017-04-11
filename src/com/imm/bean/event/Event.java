package com.imm.bean.event;

import net.sf.json.JSONObject;

public class Event {
	String type;
	String[] para;
	
	public Event() {
		para = new String[4];
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String[] getPara() {
		return para;
	}

	public void setPara(String[] para) {
		this.para = para;
	}

	@Override
	public String toString() {
		JSONObject json = JSONObject.fromObject(this);
		return json.toString();
	}
	
	public static void main(String[] args) {
		Event event = new Event();
		System.out.println(event);
	}
}
