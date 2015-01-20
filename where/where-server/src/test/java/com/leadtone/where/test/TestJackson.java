package com.leadtone.where.test;

import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leadtone.where.protocol.beans.Content;
import com.leadtone.where.protocol.beans.WhereMessage;

public class TestJackson {

	private static ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
	
	private static String jsonStr = "{\"msg_id\":\"1vafd\",\"from\":\"999991\",\"to\":\"AAAAAA\",\"subject\":\"msg\",\"content\":{\"type\":\"test\",\"data\":{\"length\":\"11\",\"body\":\"hello world\"}},\"createDate\":\"20130227121212\",\"status\":\"0\"}";

	public static void main(String[] args) {
		
		try {
			WhereMessage r = mapper.readValue(jsonStr, WhereMessage.class);
			Content rc = r.getContent();
			System.out.println(rc.getType());
			System.out.println(rc.getData());
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		HashMap<String, Object> contentMap = new HashMap<String,Object>();
		
		Object a = contentMap.get("a");
		System.out.println((String)a);
		
	}
	
	
}
