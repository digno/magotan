package com.leadtone.where.websocket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import com.leadtone.where.protocol.beans.Content;
import com.leadtone.where.protocol.beans.WhereMessage;
import com.leadtone.where.protocol.converter.ProtocolConverter;
import com.leadtone.where.utils.DateProvider;

public class TestMessageGenerator {

	public static DateProvider dateProvider = DateProvider.DEFAULT;

	// GEN MSGS
	public static WhereMessage genBasicMessage(String subject, String from,
			String to) {

		WhereMessage message = new WhereMessage();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		message.setCreateDate(sdf.format(dateProvider.getDate()));
		message.setFrom(from);
		message.setMsg_id(RandomStringUtils.randomNumeric(10));
		message.setStatus("0");
		message.setSubject(subject);
		message.setTo(to);
		message.setVersion("1.0");
		return message;

	}

	// 用户登陆
	public static String genLoginMessage(String mobile) {
		WhereMessage message = genBasicMessage("user", mobile, "server");
		Content content = new Content();
		content.setType("login");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("mobile", mobile);
		map.put("password", "1234");
		content.setData(map);
		message.setContent(content);

		return ProtocolConverter.unmarshallMsg(message);
	}

	// 用户注册
	public static String genRegMessage(String mobile, String nickname) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WhereMessage message = genBasicMessage("user", mobile, "server");
		Content content = new Content();
		content.setType("register");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("mobile", mobile);
		map.put("password", "1234");
		map.put("nickname", nickname);
		map.put("sex", "MAN");
		map.put("picture", "");
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	// 获取用户列表
	public static String genGetUserListMessage(String mobile) {
		WhereMessage message = genBasicMessage("user", mobile, "server");
		Content content = new Content();
		content.setType("getUserList");
		HashMap<String, Object> map = new HashMap<String, Object>();
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	// 获取用户信息
	public static String genGetUserDetailMessage(String mobile,
			String targetMobile) {
		WhereMessage message = genBasicMessage("user", mobile, "server");
		Content content = new Content();
		content.setType("getUserDetail");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("mobile", targetMobile);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	// 更新用户信息
	public static String genModifyUserMessage(String mobile,String nickname) {
		WhereMessage message = genBasicMessage("user", mobile, "server");
		Content content = new Content();
		content.setType("modifyUser");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("picture", "");
		map.put("nickname", nickname);
		map.put("mobile", mobile);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	// 添加一个活动
	public static String genAddActivityMessage(String owner,String aid, String title,
			String detail) {
		WhereMessage message = genBasicMessage("activity", owner, "server");
		Content content = new Content();
		content.setType("add_activity");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("content", detail);
		map.put("title", title);
		map.put("owner", owner);
		map.put("aid", aid);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	// 查找一个活动
	public static String genGetActivityMessage(String mobile, String str) {
		WhereMessage message = genBasicMessage("activity", mobile, "server");
		Content content = new Content();
		content.setType("get_activity");
		HashMap<String, Object> map = new HashMap<String, Object>();
		 map.put("aid", str);
//		 map.put("owner", str);
//		map.put("title", str);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	// 查询一个活动
	public static String genFindActivityMessage(String mobile, String title) {
		WhereMessage message = genBasicMessage("activity", mobile, "server");
		Content content = new Content();
		content.setType("find_activity");
		HashMap<String, Object> map = new HashMap<String, Object>();
		// map.put("title", "First");
		map.put("title", title);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	// 获取活动列表
	public static String genListActivitiesMessage(String mobile) {
		WhereMessage message = genBasicMessage("activity", mobile, "server");
		Content content = new Content();
		content.setType("list_activities");
		HashMap<String, Object> map = new HashMap<String, Object>();
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	// 加入活动列表
	public static String genJoinActivitiesMessage(String mobile, String aid) {
		WhereMessage message = genBasicMessage("activity", mobile, "server");
		Content content = new Content();
		content.setType("join_activity");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("aid", aid);
		map.put("mobile", mobile);
		map.put("nickname", "moumou");
		map.put("picture", "http://1111");
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	// 加入活动列表
	public static String genLeaveActivityMessage(String mobile, String aid) {
		WhereMessage message = genBasicMessage("activity", mobile, "server");
		Content content = new Content();
		content.setType("exit_activity");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("aid", aid);
		map.put("mobile", mobile);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	// 聊天信息
	public static String genChatMessage(String from, String to) {
		WhereMessage message = genBasicMessage("chat", from, to);
		Content content = new Content();
		content.setType("msg");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("msg_type", "text");
		map.put("body", from + " - " + RandomStringUtils.randomAscii(5));
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	// 地理位置上报
	public static String genGeoReportMessage(String from, String aid,
			Number[] loc) {
		WhereMessage message = genBasicMessage("geo", from, "server");
		Content content = new Content();
		content.setType("report");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("mobile", from);
		map.put("aid", aid);
		map.put("address", "牡丹园");
		HashMap<String, Object> locMap = new HashMap<String, Object>();
		locMap.put("type", "Point");
		List<Number> coordinates = new ArrayList<Number>();
		coordinates.add(loc[0]);
		coordinates.add(loc[1]);
		locMap.put("coordinates", coordinates);

		map.put("loc", locMap);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	// 地理位置上报
	public static String genSetTargetMessage(String from, String aid,
			Number[] loc) {
		WhereMessage message = genBasicMessage("activity", from, "server");
		Content content = new Content();
		content.setType("set_target");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("aid", aid);
		map.put("address", "看丹桥");
		HashMap<String, Object> locMap = new HashMap<String, Object>();
		locMap.put("type", "Point");
		List<Number> coordinates = new ArrayList<Number>();
		coordinates.add(loc[0]);
		coordinates.add(loc[1]);
		locMap.put("coordinates", coordinates);

		map.put("loc", locMap);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	// 获取活动成员地理位置
	public static String genGetMembersGeoMessage(String from, String aid) {
		WhereMessage message = genBasicMessage("geo", from, "server");
		Content content = new Content();
		content.setType("get_members_geo");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("aid", aid);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	public static String genEndActivityMessage(String from, String aid) {
		WhereMessage message = genBasicMessage("activity", from, "server");
		Content content = new Content();
		content.setType("end_activity");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("aid", aid);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	public static String genGetTargetsMessage(String from, String aid) {
		WhereMessage message = genBasicMessage("activity", from, "server");
		Content content = new Content();
		content.setType("get_target");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("aid", aid);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}
	
	public static String genGetUndelieverMessage(String from,String type) {
		WhereMessage message = genBasicMessage("system", from, "server");
		Content content = new Content();
		content.setType("get_undeliver_msg");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("mobile", from);
		map.put("type", type);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}

	public static String genJoinedActivitiesMessage(String from) {
		WhereMessage message = genBasicMessage("activity", from, "server");
		Content content = new Content();
		content.setType("joined_activity");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("mobile", from);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}
	
	public static String genJoinActivityRequestMessage(String from,String aid,String nickname,String icon) {
		WhereMessage message = genBasicMessage("activity", from, "server");
		Content content = new Content();
		content.setType("join_activity_request");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("mobile", from);
		map.put("aid", aid);
		map.put("nickname", nickname);
		map.put("picture", icon);
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}
	
	public static String genJoinActivityConfirmMessage(String from ,String mobile,String aid,String nickname,String icon) {
		WhereMessage message = genBasicMessage("activity", from, "server");
		Content content = new Content();
		content.setType("join_activity_confirm");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("mobile", mobile);
		map.put("aid", aid);
		map.put("nickname", nickname);
		map.put("picture", icon);
		map.put("result", "0"); // 同意
		content.setData(map);
		message.setContent(content);
		return ProtocolConverter.unmarshallMsg(message);
	}


	

}
