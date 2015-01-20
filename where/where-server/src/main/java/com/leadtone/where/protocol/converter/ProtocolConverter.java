package com.leadtone.where.protocol.converter;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leadtone.where.protocol.beans.Content;
import com.leadtone.where.protocol.beans.WhereMessage;
import com.leadtone.where.utils.ReflectionUtils;

public class ProtocolConverter {

	private static Logger log = Logger.getLogger(ProtocolConverter.class);

	// can reuse, share globally
	private static ObjectMapper mapper = new ObjectMapper();

	public static WhereMessage marshallBasicMsg(String requestStr) {
		try {
			WhereMessage msg = mapper.readValue(requestStr,WhereMessage.class);
			return msg;
		} catch (Exception e) {
			log.error("marshall request string to WhereMessage error !");
			e.printStackTrace();
		}
		return null;
	}

	public static String unmarshallMsg(WhereMessage message){
		try {
			return mapper.writeValueAsString(message);
		} catch (Exception e) {
			log.error("unmarshall WhereMessage to response string error ! ");
			e.printStackTrace();
		}
		return null;
	}

	public static String unmarshallMsgComplicated(WhereMessage message) {
		StringWriter writer = new StringWriter();
		JsonGenerator gen = null;
		try {
			gen = new JsonFactory().createGenerator(writer);
			mapper.writeValue(gen, message);
			String json = writer.toString();
			return json;
		} catch (Exception e) {
			log.error("unmarshallMsg error!");
			e.printStackTrace();
		} finally {
			try {
				gen.close();
				writer.close();
			} catch (Exception e) {
				log.error("WTF , close gen&writer error!");
			}
		}
		return null;
	}

	public static HashMap<String , Object> convertWhereMessageToMap(WhereMessage message){
		HashMap<String,Object> map = new HashMap<String,Object>();
		List<String> fieldNames = ReflectionUtils.getDeclaredFieldNames(WhereMessage.class);
		for(String key : fieldNames){
			Object value = ReflectionUtils.getFieldValue(message, key);
			if(key.equals("content")){
				HashMap<String,Object> contentMap = new HashMap<String,Object>();
				List<String> contentFieldNames = ReflectionUtils.getDeclaredFieldNames(Content.class);
				for(String contentKey : contentFieldNames){
					contentMap.put(contentKey, ReflectionUtils.getFieldValue(value, contentKey));
				}
				value = contentMap;
			}
			map.put(key, value);
		}
		return map.isEmpty() ?  null : map ;
	}
	
	
}
