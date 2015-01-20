package com.leadtone.where.bean;

import java.util.Date;
import java.util.List;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

@Entity(noClassnameStored = true)
public class Activity extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Property("title")
	private String title;
	
	@Property("aid")
	private String aid;
	
	@Property("owner")
	private String owner;
	
	@Property("ctime")
	private Date ctime;
	
	@Property("expire")
	private Date expire;
	
	@Property("content")
	private String content;
	
	@Embedded("members")  
	private List<ActivityUser> members;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Date getExpire() {
		return expire;
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<ActivityUser> getMembers() {
		return members;
	}

	public void setMembers(List<ActivityUser> members) {
		this.members = members;
	}

	

}
