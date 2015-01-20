package com.leadtone.where.bean;

import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.utils.IndexDirection;

@Entity(noClassnameStored = true)
public class User extends BaseEntity{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String nickname;

	@Indexed(value=IndexDirection.ASC, name="idx_user_mobile", unique=true, dropDups=true)  
	private String mobile;

	private String gender;

	private String password;
	
	private Date ctime;

	private String picture;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	
}
