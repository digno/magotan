package com.leadtone.where.protocol.beans;

/**
 * { "version":"1.0","msg_id":"1vafd", "from":"999991", "to":"AAAAAA", "subject":"reg",
 * "content":{ "mobile":"13910766840", "password":"1234"},
 * "creationDate":"20130227121212", status:"0"}
 */

public class WhereMessage {

	private String version = "1";
	private String msg_id;

	private String from;

	private String to;

	private String subject;

	private Content content;
	
	private String createDate;
	
	private String status = "0";

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}



	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

}
