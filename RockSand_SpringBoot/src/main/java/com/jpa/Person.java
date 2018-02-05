package com.jpa;

import java.util.Date;

public class Person {
	/**
	 * 唯一ID
	 */
	private String uuid;
	/**
	 * 名称
	 */
	private String name;

	/**
	 * 年岁
	 */
	private int age;

	/**
	 * 出生日期
	 */
	private Date born;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getBorn() {
		return born;
	}

	public void setBorn(Date born) {
		this.born = born;
	}
}
