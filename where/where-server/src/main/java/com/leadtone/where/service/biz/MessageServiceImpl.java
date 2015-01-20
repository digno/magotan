package com.leadtone.where.service.biz;

import java.util.List;

import org.apache.log4j.Logger;
import org.mongodb.morphia.query.QueryResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leadtone.where.bean.Activity;
import com.leadtone.where.bean.ActivityUser;
import com.leadtone.where.dao.ActivityDao;
import com.leadtone.where.dao.MessageDao;
import com.leadtone.where.notify.NotificationSender;
import com.leadtone.where.protocol.beans.WhereMessage;
import com.leadtone.where.protocol.converter.ProtocolConverter;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;


@Service
public class MessageServiceImpl {

	private Logger log = Logger.getLogger(MessageServiceImpl.class);
	
	@Autowired
	private ActivityDao activityDao; 
	
	@Autowired
	private MessageDao messageDao; 
	
	
	public List<ActivityUser> findActivityMembers(String aid) {
		Activity result = activityDao.findOne("aid", aid);
		return result!=null ? result.getMembers() : null;
	}

	public void saveUndeliverMessage(String mobile, String message) {
		WhereMessage baseMessage = ProtocolConverter.marshallBasicMsg(message);
		messageDao.saveMessage(mobile, baseMessage);
	}
	
	public void saveUndeliverMessage(WhereMessage message) {
		messageDao.saveMessage(message);
	}
	
	public void saveDeliveredMessage(String mobile, String message) {
		WhereMessage baseMessage = ProtocolConverter.marshallBasicMsg(message);
		messageDao.saveMessage(mobile, baseMessage);
	}
	
	
	
}
