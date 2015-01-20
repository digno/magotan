package com.leadtone.where.service.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leadtone.where.MsgConstants;
import com.leadtone.where.WhereBiz;
import com.leadtone.where.bean.User;
import com.leadtone.where.dao.UserDao;
import com.leadtone.where.protocol.beans.Content;
import com.leadtone.where.protocol.converter.ResponseContentHelper;
import com.leadtone.where.utils.DateProvider;

@Service
public class UserServiceImpl {

	private Logger log = Logger.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDao userDao;
	

	private DateProvider dateProvider = DateProvider.DEFAULT;

	@WhereBiz("login")
	public Content authUser(HashMap<String, Object> contentMap) {
		Content resultContent = null;
		try {
			String mobile = (String) contentMap.get("mobile");
			String pwd = (String) contentMap.get("password");
			
			User existUser = userDao.findByMobile(mobile);

			if (existUser != null && !"".endsWith(existUser.getMobile())) {
				if(existUser.getPassword().equals(pwd)){
					resultContent = new Content();
					HashMap<String, Object> resultMap = new HashMap<String, Object>();
					HashMap<String, String> temp = boxingUserData(existUser);
					resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
					resultMap.put(MsgConstants.USER, temp);
					resultContent.setData(resultMap);
				}else{
					resultContent = ResponseContentHelper
							.genSimpleResponseContentWithoutType(
									MsgConstants.ERROR_CODE_3, "incorrect password!");
				}
				
			} else {
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_2, "no such user!");
			}

		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("AuthUser Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}

	@WhereBiz("register")
	public Content register(HashMap<String, Object> contentMap) {
		Content resultContent = null;
		try {
			String mobile = (String) contentMap.get("mobile");
			if (StringUtils.isEmpty(mobile)) {
				return ResponseContentHelper.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_3,"mobile is null.");
			}
			User saveUser = new User();
			BeanUtils.populate(saveUser, contentMap);
			if (userDao.exists("mobile", saveUser.getMobile())) {
				resultContent = ResponseContentHelper.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_2,"user already registered!");
			} else {
				saveUser.setCtime(dateProvider.getDate());
				userDao.save(saveUser);
				resultContent = ResponseContentHelper.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_0,"user register successed!");
			}
		} catch (Exception e) {
			resultContent = ResponseContentHelper.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("AuthUser Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}

	@WhereBiz("modifyUser")
	public Content modifyUser(HashMap<String, Object> contentMap) {

		Content resultContent = new Content();
		try {
			String mobile = (String) contentMap.get("mobile");
			User user = userDao.findOne("mobile", mobile);
			if (user != null){
				BeanUtils.populate(user, contentMap);
				if (userDao.updateUser(mobile,user)){
					resultContent = ResponseContentHelper
							.genSimpleResponseContentWithoutType(
									MsgConstants.ERROR_CODE_0, "update user success!");
				} else {
					resultContent = ResponseContentHelper
							.genSimpleResponseContentWithoutType(
									MsgConstants.ERROR_CODE_1, "update user failed!");
				};
				
			} else{
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_2, "no such user!");
			}
			
		} catch (Exception e) {
			resultContent = ResponseContentHelper.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("getUserDetail Error : " + e.getMessage());
			e.printStackTrace();
			log.error(e);
		}
		return resultContent;
	}
	
//	@WhereBiz("getUserList")
	@Deprecated
	public Content getUserList(HashMap<String, Object> contentMap) {

		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Iterable<User> users = userDao.find();
			List<HashMap<String,String>>  userList = new ArrayList<HashMap<String,String>>();
			for (User user : users){
				HashMap<String, String> temp = boxingUserData(user);
				userList.add(temp);
			}
			
			
			resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
			resultMap.put(MsgConstants.USERS, userList);
			resultContent.setData(resultMap);
			
		} catch (Exception e) {
			resultContent = ResponseContentHelper.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("getUserList Error : " + e.getMessage());
			e.printStackTrace();
			log.error(e);
		}
		return resultContent;
	}

	
	@WhereBiz("getUserDetail")
	public Content getUserDetail(HashMap<String, Object> contentMap) {

		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
//			Long userId  =  Long.valueOf(contentMap.get("id").toString());
			User user = userDao.findOne("mobile", contentMap.get("mobile"));
			if (user != null){
				HashMap<String, String> userMap = boxingUserData(user);
				resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
				resultMap.put(MsgConstants.USER, userMap);
				resultContent.setData(resultMap);
			} else{
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_2, "no such user!");
			}
			
		} catch (Exception e) {
			resultContent = ResponseContentHelper.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("getUserDetail Error : " + e.getMessage());
			e.printStackTrace();
			log.error(e);
		}
		return resultContent;
	}
	
	private HashMap<String, String> boxingUserData(User user) {
		HashMap<String,String> temp = new HashMap<String,String>();
		temp.put("id", String.valueOf(user.getId()));
		temp.put("nickname", user.getNickname());
		temp.put("mobile", user.getMobile());
		temp.put("picture", user.getPicture());
		if(user.getCtime()!=null){
			temp.put("ctime", DateFormatUtils.format(user.getCtime(), "yyyy-MM-dd hh:mm:ss"));
		}
		temp.put("sex",  String.valueOf(user.getGender()));
		return temp;
	}
	
	
	
	
	
	
	public Content addFriend(HashMap<String, Object> contentMap) {
		Content resultContent = null;
		try {

		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("AuthUser Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}

	public Content removeFriend(HashMap<String, Object> contentMap) {
		Content resultContent = null;
		try {

		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("AuthUser Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}


	

	public static void main(String[] args) {

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("mobile", "a");
		map.put("password", "b");

		User user = new User();
		user.setNickname("xiaoq");
		user.setMobile("129");
		try {
			BeanUtils.populate(user, map);
			System.out.println(user.getMobile());
			System.out.println(user.getPassword());
			System.out.println(user.getNickname());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
