package im.smack.core;

import im.smack.socket.DummySSLSocketFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class XMPPTest extends TestCase {

	private XMPPConnection xmppConn;
	
	public XMPPTest(String caseName) {
		super(caseName);
	}
	
	public static Test suite(){
		return new TestSuite(XMPPTest.class);
	}
	
	public void testLogin(){
		assertTrue(true);
		ConnectionConfiguration connectionConfig = new ConnectionConfiguration("zyun168.com",5223);
		connectionConfig.setSocketFactory(new DummySSLSocketFactory());
		// 允许自动连接  
		connectionConfig.setReconnectionAllowed(true);  
		connectionConfig.setSendPresence(true); 
		setXmppConn(new XMPPTCPConnection(connectionConfig));
		
		try {
			getXmppConn().connect();
			AccountManager accountMgr = AccountManager.getInstance(getXmppConn());
			getXmppConn().login("aijiacy", "123456","IMSMK");
			Collection<String> collections = accountMgr.getAccountAttributes();
			Iterator<String> itCols = collections.iterator();
			for(;itCols.hasNext();){
				String attrName = itCols.next();
				String val = accountMgr.getAccountAttribute(attrName);
				System.out.println(attrName + ":" + val);
			}
			getGroupInfo();
			getXmppConn().disconnect();
		} catch (SmackException | IOException | XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getGroupInfo(){
		Roster roster = getXmppConn().getRoster();
		int groupCnt = roster.getGroupCount();
		System.out.println("组Size:" + groupCnt);
		Collection<RosterGroup> rosterGroup = roster.getGroups();
		Iterator<RosterGroup> itGroups = rosterGroup.iterator();
		while(itGroups.hasNext()){
			RosterGroup rg = itGroups.next();
			System.out.println("分组名称:" + rg.getName() + "组人数：" + rg.getEntryCount());
		}
	}

	public XMPPConnection getXmppConn() {
		return xmppConn;
	}

	public void setXmppConn(XMPPConnection xmppConn) {
		this.xmppConn = xmppConn;
	}
}
