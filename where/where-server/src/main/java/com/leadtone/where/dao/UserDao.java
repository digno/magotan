package com.leadtone.where.dao;

import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.leadtone.where.bean.User;

public class UserDao extends BasicDAO<User, Datastore> {

	public UserDao(Datastore ds) {
		super(ds);
	}

	public User findByMobile(String mobile) {

		return findOne("mobile", mobile);
	}
	
	public boolean updateUser(String mobile, User user){
		boolean result = false;
		Query<User> q = getDs().find(User.class, "mobile", mobile);
		UpdateOperations<User> ops = getDs().createUpdateOperations(User.class);
		if (StringUtils.isNotBlank(user.getNickname())){
			ops.set("nickname", user.getNickname());
		}
		if (StringUtils.isNotBlank(user.getGender())){
			ops.set("gender", user.getGender());
		}
		if (StringUtils.isNotBlank(user.getPassword())){
			ops.set("password", user.getPassword());
		}
		if (StringUtils.isNotBlank(user.getPicture())){
			ops.set("picture", user.getPicture());
		}
		UpdateResults updateResult = update(q, ops);
		result = updateResult.getUpdatedExisting();
		return result;
		
	}

}