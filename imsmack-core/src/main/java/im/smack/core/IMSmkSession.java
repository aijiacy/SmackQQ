package im.smack.core;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.proxy.ProxyInfo;

/**
 * 登录会话信息
 * @author CaoYong
 *
 */
public class IMSmkSession {
	private String sessionId;
	private String loginId;
	private String svrAddr;
	private int svrPort;
	private String clientOrigin;
	private ProxyInfo proxyInfo;
	private XMPPConnection connection;
	
	private volatile State  state;
	
	public enum State{ 
		OFFLINE,
		ONLINE,
		KICKED,
		LOGINING,
		ERROR
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getSvrAddr() {
		return svrAddr;
	}

	public void setSvrAddr(String svrAddr) {
		this.svrAddr = svrAddr;
	}

	public int getSvrPort() {
		return svrPort;
	}

	public void setSvrPort(int svrPort) {
		this.svrPort = svrPort;
	}

	public String getClientOrigin() {
		return clientOrigin;
	}

	public void setClientOrigin(String clientOrigin) {
		this.clientOrigin = clientOrigin;
	}
	
	public ProxyInfo getProxyInfo() {
		return proxyInfo;
	}

	public void setProxyInfo(ProxyInfo proxyInfo) {
		this.proxyInfo = proxyInfo;
	}

	public XMPPConnection getConnection() {
		return connection;
	}

	public void setConnection(XMPPConnection connection) {
		this.connection = connection;
	}
}
