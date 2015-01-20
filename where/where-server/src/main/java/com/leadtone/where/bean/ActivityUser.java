package com.leadtone.where.bean;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class ActivityUser implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String mobile;
	
	private String nickname;
	
	private String picture;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	
}
