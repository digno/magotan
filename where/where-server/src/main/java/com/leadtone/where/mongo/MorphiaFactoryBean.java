package com.leadtone.where.mongo;

import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.config.AbstractFactoryBean;

public class MorphiaFactoryBean extends AbstractFactoryBean<Morphia> {

	private String packgeScanName = null;

	public void setPackgeScanName(String packgeScanName) {
		this.packgeScanName = packgeScanName;
	}

	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		if (packgeScanName == null) {
			throw new IllegalStateException("morphia Datastore property packgeScanName is not set");
		}
	}

	@Override
	public Class<?> getObjectType() {
		return Morphia.class;
	}

	@Override
	protected Morphia createInstance() throws Exception {
		Morphia morphia = new Morphia();
		morphia.mapPackage(packgeScanName);
		return morphia;
	}

}
