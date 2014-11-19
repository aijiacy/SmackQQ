package com.silkroad.ui.bean;

/**
 * Project  : iqq-projects
 * Author   : 承∮诺 < 6208317@qq.com >
 * Created  : 14-5-4
 * License  : Apache License 2.0
 */
public class UIAccount extends UIUser {
    private String loginName;
    private String password;
    private boolean isRememberPwd;
    private boolean isAutoLogin;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberPwd() {
        return isRememberPwd;
    }

    public void setRememberPwd(boolean isRememberPwd) {
        this.isRememberPwd = isRememberPwd;
    }


    public boolean isAutoLogin() {
        return isAutoLogin;
    }

    public void setAutoLogin(boolean isAutoLogin) {
        this.isAutoLogin = isAutoLogin;
    }
}
