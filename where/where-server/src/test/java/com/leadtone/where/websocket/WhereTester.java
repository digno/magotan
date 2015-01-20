package com.leadtone.where.websocket;

import java.net.URI;
import java.net.URISyntaxException;

public class WhereTester {

	private URI uri;

	public WhereTester() {
		init();
	}

	private void init() {
		try {
			uri = new URI("ws://localhost:10009/websocket");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public URI getUri() {
		return uri;
	}

	public WhereClient initClient(String mobile) throws Exception {
		WhereClient c = new WhereClient(getUri());
		c.init();
		c.sendMsg(TestMessageGenerator.genLoginMessage(mobile));
		return c;
	}

	public WhereClient initClient() throws Exception {
		WhereClient c = new WhereClient(getUri());
		c.init();
		return c;
	}

	// 用户登录
	// 用户注册
	// 获取用户信息
	// 获取用户列表
	// 用户修改
	// 创建活动
	// 加入一个活动
	// 设置终点
	// 获取终点
	// 获取活动
	// 查询活动
	// 获取活动列表
	// 用户地理位置上报
	// 退出活动
	// 结束活动

	// 主动获取未投递的消息
	// 聊天消息请求
	// ---------------------
	// 聊天信息通知
	// 加入活动通知
	// 退出活动通知
	// 活动解散通知
	// 地理位置信息通知
	// 通知响应

	private void testGroupOneFunction() throws Exception {
		String mobile0 = "1390000000";
		String mobile1 = "13910766800";
		String mobile2 = "13910766842";
		String mobile3 = "13910221234";
		String aid = "000000";

		WhereClient c0 = initClient();
		// WhereClient c0 = initClient(mobile0);
		WhereClient c1 = initClient(mobile1);
		WhereClient c2 = initClient(mobile2);
		// WhereClient c3 = initClient(mobile3);

		// ----------------------------------------------------

		c0.sendMsg(TestMessageGenerator.genRegMessage(mobile0, "moumou"));
		c0.sendMsg(TestMessageGenerator.genLoginMessage(mobile0));
		c0.sendMsg(TestMessageGenerator.genGetUserDetailMessage(mobile0,
				mobile0));
		c0.sendMsg(TestMessageGenerator.genGetUserListMessage(mobile0));
		c0.sendMsg(TestMessageGenerator.genModifyUserMessage(mobile0, "digno111"));
		c0.sendMsg(TestMessageGenerator.genAddActivityMessage(mobile0, aid,
				"mobile 0 created activity", "test detail"));
		c1.sendMsg(TestMessageGenerator.genJoinActivitiesMessage(mobile1, aid));
		c2.sendMsg(TestMessageGenerator.genJoinActivitiesMessage(mobile2, aid));
		c0.sendMsg(TestMessageGenerator.genGetActivityMessage(mobile0, aid));
		c0.sendMsg(TestMessageGenerator.genFindActivityMessage(mobile0,
				"mobile"));
		c0.sendMsg(TestMessageGenerator.genSetTargetMessage(mobile0, aid,
				new Number[] { 11.1111, 22.2222 }));
		c0.sendMsg(TestMessageGenerator.genSetTargetMessage(mobile0, aid,
				new Number[] { 13.3333, 25.3333 }));
		c0.sendMsg(TestMessageGenerator.genGetTargetsMessage(mobile0, aid));
		c0.sendMsg(TestMessageGenerator.genListActivitiesMessage(mobile0));
		c0.sendMsg(TestMessageGenerator.genGeoReportMessage(mobile0, aid,
				new Number[] { 102.3333, 125.3333 }));
		c0.sendMsg(TestMessageGenerator.genGetMembersGeoMessage(mobile0,aid));
		c0.sendMsg(TestMessageGenerator.genChatMessage(mobile0, aid));
		c0.sendMsg(TestMessageGenerator.genLeaveActivityMessage(mobile0, aid));
		c0.sendMsg(TestMessageGenerator.genEndActivityMessage(mobile0, aid));
	}

	private void testGroupTwoFunction() throws Exception {
		String mobile1 = "13959588877";
		String mobile2 = "13922635953";
		String aid = "895848";
		String type = "user_join_notify";
		WhereClient c1 = initClient(mobile1);
		WhereClient c2 = initClient(mobile2);

		c1.sendMsg(TestMessageGenerator.genGetUndelieverMessage(mobile1,type));
//		c2.sendMsg(TestMessageGenerator.genJoinedActivitiesMessage(mobile2));
		
		//  13922635953 收到1通知， 13959588877 收到 1 通知
//		c1.sendMsg(TestMessageGenerator.genJoinActivityRequestMessage(mobile1, aid, "user_13959588877", "http://"+mobile1));
//		c2.sendMsg(TestMessageGenerator.genJoinActivityConfirmMessage(mobile2,mobile1, aid, "user_13959588877", "http://"+mobile1));
		
	}
	
	private void testGroupThreeFunction() throws Exception {
		String mobile1 = "13935183451";
		String aid = "895848";
		WhereClient c1 = initClient(mobile1);

		// ----------------------------------------------------

		c1.sendMsg(TestMessageGenerator.genJoinedActivitiesMessage(mobile1));
		c1.sendMsg(TestMessageGenerator.genFindActivityMessage(mobile1, mobile1));
		c1.sendMsg(TestMessageGenerator.genJoinActivitiesMessage(mobile1, aid));
	
	}

	public static void main(String[] args) throws Exception {
		WhereTester test = new WhereTester();

//		 test.testGroupOneFunction();

		test.testGroupTwoFunction();
//		test.testGroupThreeFunction();

	}

}
