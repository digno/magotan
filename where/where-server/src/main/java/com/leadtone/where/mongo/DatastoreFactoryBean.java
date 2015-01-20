package com.leadtone.where.mongo;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import com.mongodb.MongoClient;

public class DatastoreFactoryBean extends AbstractFactoryBean<Datastore> {

	private MongoClient mongoClient;
	private Morphia morphia;
	private String dbName;

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public MongoClient getMongoClient() {
		return mongoClient;
	}

	public void setMongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	public Morphia getMorphia() {
		return morphia;
	}

	public void setMorphia(Morphia morphia) {
		this.morphia = morphia;
	}

	@Override
	public Class<?> getObjectType() {
		return Datastore.class;
	}

	@Override
	protected Datastore createInstance() throws Exception {
		return morphia.createDatastore(mongoClient, dbName);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		if (mongoClient == null) {
			throw new IllegalStateException("mongoClient is not set");
		}
		if (morphia == null) {
			throw new IllegalStateException("morphia is not set");
		}
	}

}
