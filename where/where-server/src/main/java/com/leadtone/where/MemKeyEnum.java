/**
 * 
 */
package com.leadtone.where;

/**
 * @author lvqi
 * 
 */

public enum MemKeyEnum {
	LOGIN(0), 
	REGISTER_USER(1), GET_USER_PROFILE(2),GET_TEAM_INFO(3),GET_FRIENDS(4) , GET_ACTIVITY(5) , GET_ACTIVITY_LIST(6) , UPDATE_USER_PROFILE(7);

	private static MemKeyEnum[] allEnums = { 
		LOGIN, 
		REGISTER_USER, GET_USER_PROFILE,GET_TEAM_INFO,GET_FRIENDS , GET_ACTIVITY , GET_ACTIVITY_LIST , UPDATE_USER_PROFILE};

	private MemKeyEnum(int value) {
	}

	public static MemKeyEnum[] getAllEnums() {
		return allEnums;
	}

	public int value() {
		return ordinal();
	}

	public static MemKeyEnum getEnum(int value) {
		switch (value) {
		case 0:
			return LOGIN;
		case 1:
			return REGISTER_USER;
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

	public static MemKeyEnum getEnum(String value) {
		return MemKeyEnum.valueOf(value);
	}

	/**
	 * Checks whether the enum's value is greater than the input enum's value.
	 */
	public boolean above(MemKeyEnum input) {
		return compareTo(input) > 0;
	}

	/**
	 * Checks whether the enum's value is less than the input enum's value.
	 */
	public boolean below(MemKeyEnum input) {
		return compareTo(input) < 0;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(MemKeyEnum.LOGIN.ordinal());
		System.out.println(MemKeyEnum.getEnum("LOGIN"));
	}

}
