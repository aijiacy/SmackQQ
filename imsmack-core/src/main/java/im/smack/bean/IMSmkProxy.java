package im.smack.bean;

import java.io.Serializable;

import org.jivesoftware.smack.proxy.ProxyInfo.ProxyType;

public class IMSmkProxy implements Serializable {
	private static final long serialVersionUID = 1L;
	private ProxyType type;
	private String proxyHost;
	private int proxyPort;
	private String proxyAuthUser;
	private String proxyAuthPass;
	
	public ProxyType getType() {
		return type;
	}
	public void setType(ProxyType type) {
		this.type = type;
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
	public String getProxyAuthUser() {
		return proxyAuthUser;
	}
	public void setProxyAuthUser(String proxyAuthUser) {
		this.proxyAuthUser = proxyAuthUser;
	}
	public String getProxyAuthPass() {
		return proxyAuthPass;
	}
	public void setProxyAuthPass(String proxyAuthPass) {
		this.proxyAuthPass = proxyAuthPass;
	}
}
