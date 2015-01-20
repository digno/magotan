package com.leadtone.where.mongo;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.mongodb.MongoClient;

public class MongoClientFactoryBean implements FactoryBean<MongoClient>,
		InitializingBean, DisposableBean {

	private MongoClient mongoClient;
	private String host;
	private int port;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public MongoClient getObject() throws Exception {
		return mongoClient;
	}

	@Override
	public Class<?> getObjectType() {
		return MongoClient.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// MongoClientOptions options = MongoClientOptions.builder().build();
		this.mongoClient = new MongoClient(host, port);

	}

	@Override
	public void destroy() throws Exception {
		mongoClient.close();
	}

}
