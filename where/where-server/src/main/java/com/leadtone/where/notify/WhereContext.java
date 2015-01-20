package com.leadtone.where.notify;

import java.util.concurrent.LinkedBlockingQueue;

import com.leadtone.where.protocol.beans.WhereMessage;

public class WhereContext {

	/**
	 * 这里类似邮箱的概念，queue 中存放的 类似于邮件 String 表示收件人，object 表示邮件的内容，如果封装概念就强了一些。类似
	 * LinkedBlockingQueue<Mail> , Mail 有 envelope,content 将 mailBox 暴露在外
	 * ，有消息即发送。
	 */
	private static LinkedBlockingQueue<Object> notificationBox = null;
	private static LinkedBlockingQueue<WhereMessage> notifyQueue = null;

	public static LinkedBlockingQueue<Object> getNotificationBox() {
		if (notificationBox == null) {
			notificationBox = new LinkedBlockingQueue<Object>();
		}
		return notificationBox;
	}
	
	public static LinkedBlockingQueue<WhereMessage> getNotificationQueue() {
		if (notifyQueue == null) {
			notifyQueue = new LinkedBlockingQueue<WhereMessage>();
		}
		return notifyQueue;
	}

}
