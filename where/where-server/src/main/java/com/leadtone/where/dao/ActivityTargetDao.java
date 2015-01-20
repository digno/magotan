package com.leadtone.where.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;

import com.leadtone.where.bean.ActivityTarget;
import com.mongodb.WriteResult;

public class ActivityTargetDao extends BasicDAO<ActivityTarget, Datastore> {

	private Logger log = Logger.getLogger(ActivityTargetDao.class);

	protected ActivityTargetDao(Datastore ds) {
		super(ds);
	}

	public List<ActivityTarget> getTargetByAid(String aid) {
		Query<ActivityTarget> q = getDs().createQuery(ActivityTarget.class)
				.order("-utime").filter("aid", aid);
		QueryResults<ActivityTarget> qr = find(q);
		DaoLogHelper.logSimpleExplain(q);
		return qr.asList();
	}
	
	public ActivityTarget getLatestTargetByAid(String aid) {
		Query<ActivityTarget> q = getDs().createQuery(ActivityTarget.class)
				.order("-utime").filter("aid", aid).limit(1);
		ActivityTarget qr = findOne(q);
		DaoLogHelper.logSimpleExplain(q);
		return qr;
	}

	public boolean removeTargetByAid(String aid) {
		Query<ActivityTarget> q = getDs().createQuery(ActivityTarget.class)
				.filter("aid", aid);
		WriteResult qr = deleteByQuery(q);
		return qr.getN() > 0 ? true : false;
	}

}
