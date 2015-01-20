package com.leadtone.where.bean;

import java.util.Date;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.leadtone.where.utils.WhereDateSerializer;

@Entity(noClassnameStored = true)
public class Location extends BaseEntity {

	@Property("mobile")
	private String mobile;

	@Property("aid")
	private String aid;

	@Property("utime")
	@JsonSerialize(using=WhereDateSerializer.class)
	private Date utime;

	@Property("address")
	private String address;

	@Embedded("loc")
	private Geometry loc;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public Date getUtime() {
		return utime;
	}

	public void setUtime(Date utime) {
		this.utime = utime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Geometry getLoc() {
		return loc;
	}

	public void setLoc(Geometry loc) {
		this.loc = loc;
	}
	
	

}
