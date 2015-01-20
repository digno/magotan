package com.leadtone.where.bean;

public enum GenderEnum {
	MAN(0), WOMAN(1);

	private static GenderEnum[] allEnums = { MAN, WOMAN };

	private GenderEnum(int value) {
	}

	public static GenderEnum[] getAllEnums() {
		return allEnums;
	}

	public int value() {
		return ordinal();
	}

	public static GenderEnum getEnum(int value) {
		switch (value) {
		case 0:
			return MAN;
		case 1:
			return WOMAN;
		default:
			return null;
		}
	}

	public static GenderEnum getEnum(String value) {
		return GenderEnum.valueOf(value);
	}

	/**
	 * Checks whether the enum's value is greater than the input enum's value.
	 */
	public boolean above(GenderEnum input) {
		return compareTo(input) > 0;
	}

	/**
	 * Checks whether the enum's value is less than the input enum's value.
	 */
	public boolean below(GenderEnum input) {
		return compareTo(input) < 0;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(GenderEnum.MAN.ordinal());
		System.out.println(GenderEnum.getEnum("MAN"));
	}
}
