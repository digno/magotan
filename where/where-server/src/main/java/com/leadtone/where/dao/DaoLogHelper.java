package com.leadtone.where.dao;

import java.util.Map;

import org.apache.log4j.Logger;
import org.mongodb.morphia.query.Query;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class DaoLogHelper {

	private static Logger log = Logger.getLogger("where_query_info");

	public static void logSimpleExplain(Query q) {
		if (log.isDebugEnabled()) {
			Map<String, Object> a = q.explain();
			log.debug("{\"collection\":\"" + q.getCollection().getFullName()
					+ "\",\"criteria\":" + q.toString() + ",\"millis\":"
					+ a.get("millis") + ",\"allPlans\":" + a.get("allPlans")
					+ "}");
		}
	}
	
	public static void logSimpleExplain(DBCursor c) {
		if (log.isDebugEnabled()) {
			DBObject a = c.explain();
			log.debug("{\"collection\":\"" + c.getCollection().getFullName()
					+ "\",\"criteria\":" + c.getQuery().toString() + ",\"millis\":"
					+ a.get("millis") + ",\"allPlans\":" + a.get("allPlans")
					+ "}");
		}
	}
}
