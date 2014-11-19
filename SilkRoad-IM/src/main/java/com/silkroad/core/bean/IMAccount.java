package com.silkroad.core.bean;

/**
 * 登录账号信息
 * @author CaoYong
 */
public class IMAccount extends IMUser {
	private static final long serialVersionUID = 1L;
	private String username;//用户登陆名
	private String password;//用户登陆密码
	private String clientRes;//资源
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

    public String getClientRes() {
        return clientRes;
    }

    public void setClientRes(String clientRes) {
        this.clientRes = clientRes;
    }
}
