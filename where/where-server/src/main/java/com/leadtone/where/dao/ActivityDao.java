package com.leadtone.where.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.leadtone.where.bean.Activity;
import com.leadtone.where.bean.ActivityUser;
import com.mongodb.WriteResult;

public class ActivityDao extends BasicDAO<Activity, Datastore> {

	private Logger log = Logger.getLogger(ActivityDao.class);

	protected ActivityDao(Datastore ds) {
		super(ds);
	}

	public QueryResults<Activity> findActiviytLikeTitle(String title) {
		Query<Activity> q = getDs().createQuery(Activity.class);
		q.field("title").containsIgnoreCase(title);
		QueryResults<Activity> results = find(q);
		DaoLogHelper.logSimpleExplain(q);
		return results;
	}

	public Activity findActiviytByAid(String aid) {
		Query<Activity> q = getDs().createQuery(Activity.class);
		q.field("aid").equal(aid);
		QueryResults<Activity> results = find(q);
		DaoLogHelper.logSimpleExplain(q);
		return results.get();
	}

	public QueryResults<Activity> findActivitiesByMobile(String mobile) {
		Query<Activity> q = getDs().createQuery(Activity.class);
		q.field("members.mobile").equal(mobile);
		QueryResults<Activity> results = find(q);
		DaoLogHelper.logSimpleExplain(q);
		return results;
	}

	public UpdateResults addActivityMember(String aid, ActivityUser user) {
		Query<Activity> q = getDs().createQuery(Activity.class);
		q.field("aid").equal(aid);
		UpdateOperations<Activity> ops = getDs().createUpdateOperations(
				Activity.class);
		ops.add("members", user, false);
		UpdateResults results = update(q, ops);

		return results;
	}

	public boolean removeActivityMember(String aid, String user) {
		boolean result = false;
		Query<Activity> q = getDs().createQuery(Activity.class);
		q.field("aid").equal(aid);
		UpdateOperations<Activity> ops = getDs().createUpdateOperations(
				Activity.class);
		Activity a = q.get();
		if (a != null) {
			List<ActivityUser> am = a.getMembers();
			// 不可用list.remove();
			for (Iterator<ActivityUser> it = am.iterator(); it.hasNext();) {
				if (user.equals(it.next().getMobile())) {
					it.remove();
				}
			}
			ops.set("members", am);
			UpdateResults results = update(q, ops);
			result = results.getUpdatedExisting();
		} else {
			log.info("can not find Activity with aid : " + aid);
		}
		return result;
	}

	public boolean removeActivity(String aid) {
		Query<Activity> q = getDs().createQuery(Activity.class);
		q.field("aid").equal(aid);
		WriteResult results = deleteByQuery(q);
		return results.getN() > 0 ? true : false;
	}

	// public QueryResults<Activity> findActiviytLikeMember(String title){
	// Query<Activity> q = getDs().createQuery(Activity.class);
	// q.field("activity_title").contains(title);
	// QueryResults<Activity> results = find(q);
	// return results;
	// }
	//

	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("Test1");
		list.add("Test2");
		list.add("Test3");
		list.add("Test4");
		list.add("Test5");

		for (Iterator<String> it = list.iterator(); it.hasNext();) {
			if (it.next().equals("Test3")) {
				it.remove();
			}
		}

		for (String s : list) {
			System.out.println(s);
		}

	}
}