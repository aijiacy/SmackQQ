package com.silkroad.core.context;

import com.silkroad.core.bean.IMStatus;
import org.jivesoftware.smack.XMPPConnection;

import java.io.Serializable;

/**
 * Created by caoyong on 2014/7/11.
 */
public class IMSession implements Serializable {

    private String uid;
    private String jid;
    private String host;
    private int port;
    private String proxyType;
    private String proxyHost;
    private int proxyPort;
    private String proxyUser;
    private String proxyPwd;
    private XMPPConnection connection;
    private String connectionId;

    private IMStatus status = IMStatus.OFFLINE;

    private boolean useProxy;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

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

    public XMPPConnection getConnection() {
        return connection;
    }

    public void setConnection(XMPPConnection connection) {
        this.connection = connection;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public boolean isUseProxy() {
        return useProxy;
    }

    public void setUseProxy(boolean useProxy) {
        this.useProxy = useProxy;
    }

    public void setStatus(IMStatus status) {
        this.status = status;
    }

    public IMStatus getStatus() {
        return status;
    }
}
