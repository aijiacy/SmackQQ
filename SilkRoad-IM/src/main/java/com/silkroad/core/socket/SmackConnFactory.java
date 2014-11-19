package com.silkroad.core.socket;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.proxy.ProxyInfo;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.bytestreams.socks5.Socks5Proxy;

import java.io.IOException;

/**
 * Created by caoyong on 2014/7/7.
 */
public class SmackConnFactory {

    private ConnectionConfiguration configuration;
    private XMPPConnection connection = null;

    private String serverHost;
    private int serverPort;
    private ProxyInfo proxyInfo;

    private boolean isUseProxy = false;
    private boolean isDebuggerEnabled = false;
    private boolean isCompressionEnabled = true;
    private boolean isReconnectionAllowed = true;
    private boolean isRosterLoadedAtLogin = true;
    private boolean isSendPresence = false;

    protected SmackConnFactory(){}

    protected void initConnection(String server, int port, ProxyInfo proxyInfo) throws IOException, XMPPException, SmackException {
        this.serverHost = server;
        this.serverPort = port;
        this.proxyInfo = proxyInfo;
        if(null == connection) {
            this.initConfiguration();
            /**
             * TODO 临时改变，正式使用时取消
             */
            Socks5Proxy.setLocalSocks5ProxyPort(7776);
            connection = new XMPPTCPConnection(getConfiguration());
            connection.setFromMode(XMPPConnection.FromMode.USER);
            connection.connect();
        }
    }

    private void initConfiguration(){
        if(isUseProxy){
            configuration = new ConnectionConfiguration(serverHost, serverPort, proxyInfo);
        }else {
            configuration = new ConnectionConfiguration(serverHost, serverPort);
        }
        getConfiguration().setSocketFactory(new DummySSLSocketFactory());
        getConfiguration().setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
        getConfiguration().setDebuggerEnabled(isDebuggerEnabled);
        /** 是否启用压缩 */
        getConfiguration().setCompressionEnabled(isCompressionEnabled);
        /**
         * 是否允许重新连接
         */
        getConfiguration().setReconnectionAllowed(isReconnectionAllowed);
        /**
         * 是否登陆后就获取好友列表
         */
        getConfiguration().setRosterLoadedAtLogin(isRosterLoadedAtLogin);
        /**
         * 是否发送状态信息
         */
        getConfiguration().setSendPresence(isSendPresence);
    }

    public ConnectionConfiguration getConfiguration() {
        return configuration;
    }

    public XMPPConnection getConnection() {
        return connection;
    }

    public void setUseProxy(boolean isUseProxy) {
        this.isUseProxy = isUseProxy;
    }

    public void setDebuggerEnabled(boolean isDebuggerEnabled) {
        this.isDebuggerEnabled = isDebuggerEnabled;
    }

    public void setCompressionEnabled(boolean isCompressionEnabled) {
        this.isCompressionEnabled = isCompressionEnabled;
    }

    public void setReconnectionAllowed(boolean isReconnectionAllowed) {
        this.isReconnectionAllowed = isReconnectionAllowed;
    }

    public void setRosterLoadedAtLogin(boolean isRosterLoadedAtLogin) {
        this.isRosterLoadedAtLogin = isRosterLoadedAtLogin;
    }

    public void setSendPresence(boolean isSendPresence) {
        this.isSendPresence = isSendPresence;
    }

}
