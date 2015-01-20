package com.leadtone.where.protocol.beans;

import java.util.HashMap;

public class Content {

	private String type;

	private HashMap<String, Object> data;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public HashMap<String, Object> getData() {
		return data;
	}

	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}

}
