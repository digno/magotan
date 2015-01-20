package com.leadtone.where.notify;

public class WhereNotification {

	
	
	public WhereNotification(String to) {
		super();
		this.to = to;
	}
	private String to;
	private Object content;
	
	
	
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	
	
	
}
