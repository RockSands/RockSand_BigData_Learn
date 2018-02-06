package com.author.service;

public class SayHi {
	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String sayHi() {
		return "hi," + msg + "!";
	}
}
