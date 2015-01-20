/**
 * 
 */
package com.leadtone.where;

/**
 * @author lvqi
 * 
 */

public enum BizKeyEnum {
	// 用户登陆
	LOGIN(0), 
	// 用户注册
	REGISTER(1),
	// 获取用户信息
	GET_USER_PROFILE(2),
	// 获取车队信息
	GET_TEAM_INFO(3),
	// 获取好友列表
	GET_FRIENDS(4) ,
	// 获取活动
	GET_ACTIVITY(5) ,
	// 获取活动列表
	GET_ACTIVITY_LIST(6),
	// 更新用户信息
	UPDATE_USER_PROFILE(7),
	;

	private static BizKeyEnum[] allEnums = { 
		LOGIN, 
		REGISTER, GET_USER_PROFILE,GET_TEAM_INFO,GET_FRIENDS , GET_ACTIVITY , GET_ACTIVITY_LIST , UPDATE_USER_PROFILE};

	private BizKeyEnum(int value) {
	}

	public static BizKeyEnum[] getAllEnums() {
		return allEnums;
	}

	public int value() {
		return ordinal();
	}

	public static BizKeyEnum getEnum(int value) {
		switch (value) {
		case 0:
			return LOGIN;
		case 1:
			return REGISTER;
		case 2:
			return GET_USER_PROFILE;
		case 3:
            return GET_TEAM_INFO;
		case 4:
            return GET_FRIENDS;
		case 5:
            return GET_ACTIVITY;
		case 6:
            return GET_ACTIVITY_LIST;
		case 7:
            return UPDATE_USER_PROFILE;
		default:
			return null;
		}
	}

	public static BizKeyEnum getEnum(String value) {
		return BizKeyEnum.valueOf(value);
	}

	/**
	 * Checks whether the enum's value is greater than the input enum's value.
	 */
	public boolean above(BizKeyEnum input) {
		return compareTo(input) > 0;
	}

	/**
	 * Checks whether the enum's value is less than the input enum's value.
	 */
	public boolean below(BizKeyEnum input) {
		return compareTo(input) < 0;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(BizKeyEnum.LOGIN.ordinal());
		System.out.println(BizKeyEnum.getEnum("LOGIN"));
	}

}
