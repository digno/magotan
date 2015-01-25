package com.leadtone.where.test.mongodb;

import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.leadtone.where.InitEnvironment;
import com.leadtone.where.dao.ActivityDao;
import com.leadtone.where.mongo.MongoClientFactoryBean;
import com.leadtone.where.service.biz.ActivityServiceImpl;
import com.leadtone.where.service.biz.MessageServiceImpl;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class TestCRUDWithJavaDriver {

	private Logger log = Logger.getLogger(TestCRUDWithJavaDriver.class);

	private ApplicationContext context;
	private MongoClient mongoClient;

	@Autowired
	private ActivityDao activityDao;
	
	public DB init() {
		context = InitEnvironment.initAppclicatContext();
		InitEnvironment.initLog4j();
		DB db = null;
		try {
			MongoClientFactoryBean mongoFactory = context.getBean(MongoClientFactoryBean.class);
			mongoClient = mongoFactory.getObject();
			db = mongoClient.getDB("mydb");
		} catch (Exception e) {
			log.error("can not connection DB .");
			// System.out.println("can not connection DB .");
		}
		return db;
	}

	
	public void initContext() {

		context = InitEnvironment.initAppclicatContext();
		InitEnvironment.initLog4j();

	}
	public void testGetCollections(DB db) {
		Set<String> colls = db.getCollectionNames();
		for (String s : colls) {
			log.info(s);
			// System.out.println(s);
		}
	}

	public void testInsertDataToCollections(DB db, String colName) {
		DBCollection dbc = db.getCollection(colName);
		BasicDBObject bdb = new BasicDBObject("name", "mongodb");
		bdb.append("version", "2.6.5");
		bdb.append("type", "database");
		bdb.append("count", 1);
		HashMap<String, Object> infoMap = new HashMap<String, Object>();
		infoMap.put("x", 203);
		infoMap.put("y", 102);
		bdb.append("info", new BasicDBObject(infoMap));
		dbc.insert(bdb);
		DBObject dbo = dbc.findOne();
		Object id = dbo.get("_id");
		log.info(id);
		log.info(dbo);
		// System.out.println(id);
		// System.out.println(dbo);
	}

	public void testDropCollections(DB db, String colName) {
		DBCollection dbc = db.getCollection(colName);
		dbc.drop();
		testGetCollections(db);
	}

	
	
	public static void main(String[] args) {

		String colName = "testCols";
		TestCRUDWithJavaDriver t = new TestCRUDWithJavaDriver();
		t.initContext();
//		DB db = t.init();
//
//		t.testGetCollections(db);
//		t.testInsertDataToCollections(db, colName);
//		t.testDropCollections(db, colName);
		
//		ActivityServiceImpl a = t.context.getBean(ActivityServiceImpl.class);
//		a.testInsert(null);
		String message = "{\"version\":\"1.0\",\"msg_id\":\"server15091422106396158\",\"from\":\"server\",\"to\":\"1509\",\"subject\":\"activity\",\"content\":{\"type\":\"exit_activity\",\"data\":{\"result\":\"0\",\"msg\":\"leave Activity no 485552 successed!\"}},\"createDate\":\"20150124213316\",\"status\":\"0\"}";
		MessageServiceImpl a = t.context.getBean(MessageServiceImpl.class);
		a.saveUndeliverMessage("13910766840",message);
		
	}

}
