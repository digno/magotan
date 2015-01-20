package com.leadtone.where.server;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leadtone.mig.tools.date.DateUtils;
import com.leadtone.where.protocol.beans.WhereMessage;

public class ResponseHelper {

	private static Logger log = Logger.getLogger(ResponseHelper.class);

	private static ObjectMapper mapper = new ObjectMapper();// can reuse, share globally
	
	// 异步消息无需转化 FROM TO
	public static String genAsyncResponse(WhereMessage message) {
		String result = "";
		try {
			long timeStamp = System.currentTimeMillis();
			message.setMsg_id(genMessageId(message.getFrom(), message.getTo(), timeStamp));
			message.setCreateDate(DateUtils.getGdadcTimeStamp());
			result = mapper.writeValueAsString(message);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		log.info("Async response to client : " + result);
		return result;
	}

	// 同步消息需要将FROM TO 信息互换
	public static String genSyncResponse(WhereMessage message) {
		WhereMessage newMessage = swapFromTo(message);
		String result = "";
		try {
			long timeStamp = System.currentTimeMillis();
			newMessage.setMsg_id(genMessageId(message.getFrom(), message.getTo(), timeStamp));
			newMessage.setCreateDate(DateUtils.getGdadcTimeStamp());
			result = mapper.writeValueAsString(newMessage);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		log.info("Sync response to client : " + result);
		return result;
	}
	
	
	
	private static WhereMessage swapFromTo(WhereMessage message){
		String from = message.getFrom();
		String to = message.getTo();
		WhereMessage swapedMsg = message;
		swapedMsg.setFrom(to);
		swapedMsg.setTo(from);
		return swapedMsg;
	}
	
	private static String genMessageId(String from,String to,long timeStamp ){
		return from+ to+ timeStamp;
//		return from+"-"+to+"-"+timeStamp;
	}
}
