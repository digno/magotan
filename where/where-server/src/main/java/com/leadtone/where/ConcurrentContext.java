package com.leadtone.where;

import java.util.concurrent.ConcurrentHashMap;

import com.leadtone.where.server.WhereChannel;

public class ConcurrentContext {

	private static volatile ConcurrentHashMap<String, WhereChannel> map = null;
	private static volatile ConcurrentHashMap<Integer, WhereChannel> cMap  = null;

	public static ConcurrentHashMap<String, WhereChannel> getChannelMapInstance() {
		if (map == null) {
			map = new ConcurrentHashMap<String, WhereChannel>();
		}
		return map;
	}
	
	public static ConcurrentHashMap<Integer, WhereChannel> getAvailableChannelMapInstance() {
		if (cMap == null) {
			cMap = new ConcurrentHashMap<Integer, WhereChannel>();
		}
		return cMap;
	}

}
