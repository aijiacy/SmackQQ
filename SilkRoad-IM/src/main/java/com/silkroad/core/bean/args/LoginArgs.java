package com.silkroad.core.bean.args;

import com.silkroad.ui.bean.UIAccount;

import java.io.Serializable;

/**
 * Created by caoyong on 2014/7/8.
 */
public class LoginArgs extends UIAccount implements Serializable{
    private String host;
    private int port;
    private String clientRes;

    private boolean isUseProxy;
    private String proxyType;
    private String proxyHost;
    private int proxyPort;
    private String proxyUser;
    private String proxyPwd;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getClientRes() {
        return clientRes;
    }

    public void setClientRes(String clientRes) {
        this.clientRes = clientRes;
    }

    public boolean isUseProxy() {
        return isUseProxy;
    }

    public void setUseProxy(boolean isUseProxy) {
        this.isUseProxy = isUseProxy;
    }

    public String getProxyType() {
        return proxyType;
    }

    public void setProxyType(String proxyType) {
        this.proxyType = proxyType;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public String getProxyPwd() {
        return proxyPwd;
    }

    public void setProxyPwd(String proxyPwd) {
        this.proxyPwd = proxyPwd;
    }
}
