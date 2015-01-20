package com.leadtone.where.service.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leadtone.where.MsgConstants;
import com.leadtone.where.WhereBiz;
import com.leadtone.where.bean.Activity;
import com.leadtone.where.bean.ActivityTarget;
import com.leadtone.where.bean.ActivityUser;
import com.leadtone.where.bean.Geometry;
import com.leadtone.where.dao.ActivityDao;
import com.leadtone.where.dao.ActivityTargetDao;
import com.leadtone.where.notify.NotificationSender;
import com.leadtone.where.protocol.beans.Content;
import com.leadtone.where.protocol.converter.ResponseContentHelper;
import com.leadtone.where.utils.DateProvider;

@Service
public class ActivityServiceImpl {

	private Logger log = Logger.getLogger(ActivityServiceImpl.class);

	@Autowired
	private ActivityDao activityDao;

	@Autowired
	private ActivityTargetDao targetDao;

	private DateProvider dateProvider = DateProvider.DEFAULT;

	private String join_type = "user_join_notify";

	private String join_req = "request_user_join_notify";

	private String leave_type = "user_leave_notify";

	private String end_type = "activity_end_notify";

	@WhereBiz("join_activity_request") // 发起方为待加入的队员
	public Content joinActivityRequest(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		try {
			Object aid = contentMap.get("aid");
			Object mobile = contentMap.get("mobile");
			Object nickname = contentMap.get("nickname");
			Object picture = contentMap.get("picture");
			if (aid != null && mobile != null) {
				ActivityUser au = new ActivityUser();
				au.setMobile(mobile.toString());
				au.setNickname((String)nickname);
				au.setPicture((String)picture);
				Activity activity = activityDao.findActiviytByAid(aid
						.toString());
				String owner = activity.getOwner();
				List<String> to = new ArrayList<String>();
				to.add(owner);
				NotificationSender.sendNotify(MsgConstants.ACTIVITY, to,
						genNotificationContent(join_req, activity, au));

				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_0, "join Activity [ "
										+ aid + " ] request send to " + owner
										+ " successed!");
			} else {
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_4,
								"can not send join activity request, require aid , mobile or nickname.");
			}

		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("joinActivity Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}
	
	@WhereBiz("join_activity_confirm")
	// 发起方为 活动的 队长
	public Content joinActivityConfirm(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		try {
			Object aid = contentMap.get("aid");
			Object mobile = contentMap.get("mobile");
			Object nickname = contentMap.get("nickname");
			Object picture = contentMap.get("picture");
			Object resultCode = contentMap.get("result");
			if (resultCode != null && aid != null
					&& mobile != null ) {
				if (("0").equals(resultCode.toString())) {
					ActivityUser au = new ActivityUser();
					au.setMobile(mobile.toString());
					au.setNickname((String)nickname);
					au.setPicture((String)picture);
					UpdateResults result = activityDao.addActivityMember(
							aid.toString(), au);
					if (result.getUpdatedExisting()) {
						resultContent = ResponseContentHelper
								.genSimpleResponseContentWithoutType(
										MsgConstants.ERROR_CODE_0,
										"join Activity no " + aid
												+ " successed!");
						Activity a = activityDao.findActiviytByAid(aid
								.toString());
						NotificationSender.sendNotify(MsgConstants.ACTIVITY,
								getMembersMobile(a, a.getOwner()), // 通知不发给队长
								genNotificationContent(join_type, a, au));
					}
				}
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_0,
								"confirm join activity request successed.");
			} else {
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_4,
								"can not confirm join activity request , require aid , mobile or nickname.");
			}

		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("joinActivity Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}

	@WhereBiz("join_activity")
	public Content joinActivity(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		try {
			Object aid = contentMap.get("aid");
			Object mobile = contentMap.get("mobile");
			Object nickname = contentMap.get("nickname");
			Object picture = contentMap.get("picture");
			if (aid != null && mobile != null) {
				ActivityUser au = new ActivityUser();
				au.setMobile(mobile.toString());
				au.setNickname((String)nickname);
				au.setPicture((String)picture);
				UpdateResults result = activityDao.addActivityMember(
						aid.toString(), au);
				if (result.getUpdatedExisting()) {
					resultContent = ResponseContentHelper
							.genSimpleResponseContentWithoutType(
									MsgConstants.ERROR_CODE_0,
									"join Activity no " + aid + " successed!");
					Activity a = activityDao.findActiviytByAid(aid.toString());
					NotificationSender.sendNotify(MsgConstants.ACTIVITY,
							getMembersMobile(a, mobile.toString()),
							genNotificationContent(join_type, a, au));
				} else {
					resultContent = ResponseContentHelper
							.genSimpleResponseContentWithoutType(
									MsgConstants.ERROR_CODE_4,
									"no such activity with NO:" + aid);
				}
			} else {
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_4,
								"can not join activity , require aid , mobile or nickname.");
			}

		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("joinActivity Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}

	@WhereBiz("joined_activity")
	public Content joinedActivity(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Object mobile = contentMap.get("mobile");
			if (mobile != null) {
				QueryResults<Activity> results = activityDao
						.findActivitiesByMobile(mobile.toString());
				List<Activity> activitys = results.asList();
				List<HashMap<String, Object>> activityList = new ArrayList<HashMap<String, Object>>();
				resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
				for (Activity activity : activitys) {
					HashMap<String, Object> temp = boxingActivityData(activity);
					activityList.add(temp);
				}
				resultMap.put(MsgConstants.ACTIVITIES, activityList);
				resultContent.setData(resultMap);
			} else {
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_4,
								"can not query joined activities with null field.");
			}
		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("query joined Activity Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}

	@WhereBiz("exit_activity")
	public Content leaveActivity(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		try {
			Object aid = contentMap.get("aid");
			Object mobile = contentMap.get("mobile");
			if (aid != null && mobile != null) {
				ActivityUser au = new ActivityUser();
				au.setMobile(mobile.toString());
				UpdateResults result = activityDao.removeActivityMember(
						aid.toString(), mobile.toString());
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_0, "leave Activity no "
										+ aid + " successed!");
				if (result.getUpdatedExisting()) {
					Activity a = activityDao.findActiviytByAid(aid.toString());
					NotificationSender.sendNotify(MsgConstants.ACTIVITY,
							getMembersMobile(a, mobile.toString()),
							genNotificationContent(leave_type, a, au));
				}
			} else {
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_4,
								"can not leave activity , require aid , mobile .");
			}

		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("leaveActivity Error : ", e);
		}
		return resultContent;
	}

	@WhereBiz("set_target")
	public Content setTarget(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		try {
			String aid = (String) contentMap.get("aid");
			String address = (String) contentMap.get("address");
			ActivityTarget saveLocation = new ActivityTarget();
			saveLocation.setAddress(address);
			saveLocation.setAid(aid);
			saveLocation.setUtime(dateProvider.getDate());
			if (saveLocation.getAid() != null) {
				Geometry geometry = new Geometry();
				BeanUtils.populate(geometry, (Map) contentMap.get("loc"));
				if (geometry.getCoordinates() == null
						|| geometry.getCoordinates().size() != 2) {
					resultContent = ResponseContentHelper
							.genSimpleResponseContentWithoutType(
									MsgConstants.ERROR_CODE_5,
									" set Activity Target failed, require [<longitude> , <latitude>]");
					return resultContent;
				}
				saveLocation.setLoc(geometry);
				Key<ActivityTarget> result = targetDao.save(saveLocation);
				log.info("Key :" + result.getId() + " aid :" + aid + " loc :"
						+ geometry.toString());
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_0, "aid " + aid
										+ " set Activity Target successed!");
			} else {
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_4,
								" set Activity Target  failed, aid.");
			}

		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("geoReport Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}

	@WhereBiz("get_target")
	public Content getTarget(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String aid = (String) contentMap.get("aid");
			List<ActivityTarget> result = targetDao.getTargetByAid(aid);
			resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
			resultMap.put(MsgConstants.LOCS, result);
			resultContent.setData(resultMap);
		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("geoReport Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}

	// find_activity 使用 activity_title 模糊/精确 查询。
	@WhereBiz("find_activity")
	public Content findActivity(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Object title = contentMap.get("title");
			if (title != null) {
				QueryResults<Activity> results = activityDao
						.findActiviytLikeTitle(title.toString());
				List<Activity> activitys = results.asList();
				List<HashMap<String, Object>> activityList = new ArrayList<HashMap<String, Object>>();
				resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
				for (Activity activity : activitys) {
					HashMap<String, Object> temp = boxingActivityData(activity);
					activityList.add(temp);
				}
				resultMap.put(MsgConstants.ACTIVITIES, activityList);
				resultContent.setData(resultMap);
			} else {
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_4,
								"can not query with null field.");
			}

		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("addActivity Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}

	// get_activity 默认是使用 activity_no 查询，也支持 actvitiy_owner , activity_title
	// 等精确查询。
	// 如果以actvitiy_owner , activity_title 查询并有多个结果，则随即返回一条记录。
	@WhereBiz("get_activity")
	public Content getActivity(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Set<String> keys = contentMap.keySet();
			if (keys.size() == 0) {
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_4,
								"can not query with null field.");
				return resultContent;
			}
			Activity activity = null;
			for (String key : keys) {
				String value = (String) contentMap.get(key);
				activity = activityDao.findOne(key, value);
				if (activity != null) {
					break;
				}
			}
			resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
			resultMap = boxingActivityData(activity);
			resultContent.setData(resultMap);
		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("addActivity Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}

	@WhereBiz("add_activity")
	public Content addActivity(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		;
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Activity saveActivity = new Activity();
			BeanUtils.populate(saveActivity, contentMap);
			saveActivity.setCtime(dateProvider.getDate());
			if (StringUtils.isEmpty(saveActivity.getAid())) {
				saveActivity.setAid(RandomStringUtils.randomNumeric(6));
			}
			List<ActivityUser> list = new ArrayList<ActivityUser>();
			ActivityUser au = new ActivityUser();
			au.setMobile(saveActivity.getOwner());
			list.add(au);
			saveActivity.setMembers(list);
			Key<Activity> akey = activityDao.save(saveActivity);
			Object id = akey.getId();
			if (id != null) {
				HashMap<String, Object> activityMap = boxingActivityData(saveActivity);
				resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
				resultMap.put(MsgConstants.USER, activityMap);
				resultContent.setData(activityMap);
			}
		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("addActivity Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}

	@WhereBiz("end_activity")
	public Content endActivity(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		;
		try {
			String aid = (String) contentMap.get("aid");
			Activity a = activityDao.findActiviytByAid(aid.toString());
			boolean result = activityDao.removeActivity(aid);
			boolean targetResult = targetDao.removeTargetByAid(aid);
			if (result) {
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_0,
								"end activity successed.");
				if (a != null) {
					NotificationSender.sendNotify(MsgConstants.ACTIVITY,
							getMembersMobile(a, ""),
							genSimpleNotificationContent(end_type, a));
				}
			} else {
				log.error("delete activity failed! remove Activity is "
						+ result + " , remove ActivityTarget is "
						+ targetResult);
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_2, "Activity " + aid
										+ " not exists.");
			}
		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("endActivity Error : ", e);
		}

		return resultContent;
	}

	@WhereBiz("list_activities")
	public Content getActivitiesList(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			QueryResults<Activity> activities = activityDao.find();
			List<HashMap<String, Object>> activityList = new ArrayList<HashMap<String, Object>>();
			for (Activity activity : activities) {
				HashMap<String, Object> temp = boxingActivityData(activity);
				activityList.add(temp);

			}
			resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
			resultMap.put(MsgConstants.ACTIVITIES, activityList);
			resultContent.setData(resultMap);
		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("getActivitiesList Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}

	private HashMap<String, String> boxingActivityUserData(ActivityUser user) {
		HashMap<String, String> temp = new HashMap<String, String>();
		temp.put("mobile", user.getMobile());
		temp.put("nickname", user.getNickname());
		temp.put("picture", user.getPicture());
		return temp;
	}

	private HashMap<String, Object> boxingActivityData(Activity activity) {
		HashMap<String, Object> temp = new HashMap<String, Object>();
		temp.put("aid", activity.getAid());
		temp.put("expire", activity.getExpire());
		temp.put("title", activity.getTitle());
		temp.put("content", activity.getContent());
		temp.put("owner", activity.getOwner());
		if (activity.getCtime() != null) {
			temp.put("ctime", DateFormatUtils.format(activity.getCtime(),
					"yyyy-MM-dd hh:mm:ss"));
		}
		List<ActivityUser> users = activity.getMembers();
		List<HashMap<String, String>> usersList = new ArrayList<HashMap<String, String>>();
		if (users != null) {
			for (ActivityUser user : users) {
				HashMap<String, String> t = boxingActivityUserData(user);
				usersList.add(t);
			}
		}
		temp.put("members", usersList);
		return temp;
	}

	private List<String> getMembersMobile(Activity a, String self) {

		List<String> to = new ArrayList<String>();
		for (ActivityUser m : a.getMembers()) {
			if (!self.equals(m.getMobile())) {
				to.add(m.getMobile());
			}
		}
		return to;
	}

	private Content genNotificationContent(String type, Activity a,
			ActivityUser au) {
		Content content = new Content();
		content.setType(type);
		HashMap<String, Object> temp = new HashMap<String, Object>();
		temp.put("mobile", au.getMobile());
		if (au.getNickname() != null) {
			temp.put("nickname", au.getNickname());
		}
		if (au.getPicture() != null) {
			temp.put("picture", au.getPicture());
		}
		temp.put("aid", a.getAid());
		temp.put("title", a.getTitle());
		content.setData(temp);
		return content;
	}

	private Content genSimpleNotificationContent(String type, Activity a) {
		Content content = new Content();
		content.setType(type);
		HashMap<String, Object> temp = new HashMap<String, Object>();
		temp.put("aid", a.getAid());
		temp.put("title", a.getTitle());
		content.setData(temp);
		return content;
	}

	public void testInsert(HashMap<String, Object> contentMap) {
		try {
			Activity saveActivity = new Activity();
			saveActivity.setContent("111");
			saveActivity.setAid("00000000000000000");
			saveActivity.setOwner("333");
			ActivityUser au = new ActivityUser();
			au.setMobile("323");
			au.setNickname("xxxx");
			List<ActivityUser> l = new ArrayList<ActivityUser>();
			l.add(au);
			saveActivity.setMembers(l);
			activityDao.save(saveActivity);
			log.info("add activity successed!");
		} catch (Exception e) {
			log.error("addActivity Error : " + e.getMessage());
			log.error(e);
		}
	}

}
