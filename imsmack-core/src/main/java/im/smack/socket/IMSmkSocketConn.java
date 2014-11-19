package im.smack.socket;

import java.io.IOException;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.proxy.ProxyInfo;
import org.jivesoftware.smack.proxy.ProxyInfo.ProxyType;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IMSmkSocketConn {
	private static final Logger LOG = LoggerFactory.getLogger(IMSmkSocketConn.class);

	private static IMSmkSocketConn instance = null;
	private String server;
	private int port;
	private ConnectionConfiguration connectionConfig;
	private XMPPConnection connection = null;
	private ProxyInfo proxyInfo;

	private IMSmkSocketConn(String server, int port, ProxyInfo proxyInfo) {
		this.server = server;
		this.port = port;
		if (proxyInfo == null) {
			this.proxyInfo = ProxyInfo.forDefaultProxy();
		} else {
			this.proxyInfo = proxyInfo;
		}
	}

	/**
	 * 设置HTTP代理
	 * 
	 * @param pType
	 *            代理类型
	 * @param pHost
	 *            代理主机
	 * @param pPort
	 *            代理端口
	 * @param pUser
	 *            认证用户名， 如果不需要认证，设置为null
	 * @param pPass
	 *            认证密码，如果不需要认证，设置为null
	 */
	public void setProxyHost(ProxyType pType, String pHost, int pPort, String pUser, String pPass) {
		this.proxyInfo = new ProxyInfo(pType, pHost, pPort, pUser, pPass);
	}

	public static IMSmkSocketConn getInstance(String server, int port, ProxyInfo proxyInfo) {
		if (instance == null) {
			instance = new IMSmkSocketConn(server, port, proxyInfo);
		}
		return instance;
	}

	private ConnectionConfiguration getConnectionConfig() {
		switch (this.proxyInfo.getProxyType()) {
		case NONE:
			connectionConfig = new ConnectionConfiguration(server, port);
			break;
		default:
			connectionConfig = new ConnectionConfiguration(server, port, proxyInfo);
			break;
		}
		connectionConfig.setSocketFactory(new DummySSLSocketFactory());
		connectionConfig.setCompressionEnabled(true);
		connectionConfig.setSecurityMode(SecurityMode.enabled);
		connectionConfig.setReconnectionAllowed(true);
        connectionConfig.setRosterLoadedAtLogin(true);
		connectionConfig.setSendPresence(false);
		return connectionConfig;
	}

	public XMPPConnection getConnection() {
		if (connection != null) {
			return connection;
		} else {
			connection = new XMPPTCPConnection(this.getConnectionConfig());
			try {
				connection.connect();
			} catch (SmackException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (XMPPException e) {
				e.printStackTrace();
			}
			return connection;
		}
	}

	public void destory() {
		if (connection != null && connection.isConnected()) {
			try {
				connection.disconnect();
			} catch (NotConnectedException e) {
				LOG.warn("释放连接出错:", e);
			}
		}
	}
}
