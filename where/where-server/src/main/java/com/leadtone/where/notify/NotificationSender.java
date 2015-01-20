package com.leadtone.where.notify;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

import com.leadtone.mig.tools.date.DateUtils;
import com.leadtone.where.MsgConstants;
import com.leadtone.where.protocol.beans.Content;
import com.leadtone.where.protocol.beans.WhereMessage;

public class NotificationSender {

	

	private static WhereMessage genBasicNotification(){
		WhereMessage message = new WhereMessage();
		message.setMsg_id(UUID.randomUUID().toString());
		message.setCreateDate(DateUtils.getGdadcTimeStamp());
		message.setStatus(MsgConstants.STATUS);
		message.setVersion(MsgConstants.VERSION);
		message.setFrom(MsgConstants.SERVER);
		return message;
	}
	
	
	public static void sendNotify(String subject, List<String> to, Content content) {
		LinkedBlockingQueue<WhereMessage> inbox = WhereContext.getNotificationQueue();
		for (String t : to){
			WhereMessage message = genBasicNotification();
			message.setSubject(subject);
			message.setTo(t);
			message.setContent(content);
			inbox.add(message);
		}
	}
	
	public static void sendNotify(WhereMessage message) {
		LinkedBlockingQueue<WhereMessage> inbox = WhereContext.getNotificationQueue();
		inbox.add(message);
	}
}
