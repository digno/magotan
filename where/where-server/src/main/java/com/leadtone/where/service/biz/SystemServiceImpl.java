package com.leadtone.where.service.biz;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leadtone.where.MsgConstants;
import com.leadtone.where.WhereBiz;
import com.leadtone.where.dao.MessageDao;
import com.leadtone.where.notify.NotificationSender;
import com.leadtone.where.protocol.beans.Content;
import com.leadtone.where.protocol.beans.WhereMessage;
import com.leadtone.where.protocol.converter.ProtocolConverter;
import com.leadtone.where.protocol.converter.ResponseContentHelper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Service
public class SystemServiceImpl {
	private Logger log = Logger.getLogger(SystemServiceImpl.class);

	@Autowired
	private MessageDao messageDao;

	@WhereBiz("tide")
	public Content genTideData(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultContent.setData(resultMap);
		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("tide Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}

	@WhereBiz("get_undeliver_msg")
	public Content getUndeliverMessages(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		Object mobile = contentMap.get("mobile");
		try {
			DBCursor cursor = messageDao.findUndeliverMessages((String) mobile);
			cursor.sort(new BasicDBObject("createDate", -1));
			while (cursor.hasNext()) {
				DBObject d = cursor.next();
				log.info("---------------\r\n" + d.toString());
				WhereMessage message = ProtocolConverter.marshallBasicMsg(d
						.toString());
				NotificationSender.sendNotify(message);
				String id = (String) d.get("msg_id");
				messageDao.removeMessage(id);
			}
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.ERROR_CODE_0,
							"message will deliver.");
		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("tide Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}

}
