package com.leadtone.riders.test;

import org.apache.commons.lang3.RandomStringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leadtone.where.protocol.beans.Content;
import com.leadtone.where.protocol.beans.WhereMessage;

public class TestJackson {

	private static ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
	
	private static String jsonStr = "{\"msg_id\":\"1vafd\",\"from\":\"999991\",\"to\":\"AAAAAA\",\"subject\":\"msg\",\"content\":{\"type\":\"test\",\"params\":{\"length\":\"11\",\"body\":\"hello world\"}},\"createDate\":\"20130227121212\",\"status\":\"0\"}";

	public static void main(String[] args) {
		
//		try {
//			WhereMessage r = mapper.readValue(jsonStr, WhereMessage.class);
//			Content rc = r.getContent();
//			System.out.println(rc.getType());
//			System.out.println(rc.getData());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
		System.out.println("aaa"+RandomStringUtils.randomNumeric(10));
		
	}
	
	
}
