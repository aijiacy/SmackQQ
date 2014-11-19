package im.smack.core;

import org.jivesoftware.smack.proxy.ProxyInfo.ProxyType;

import im.smack.IMSmkActionListener;
import im.smack.IMSmkClient;
import im.smack.IMSmkNotifyListener;
import im.smack.actor.SwingActorDispatcher;
import im.smack.event.IMSmkActionEvent;
import im.smack.event.IMSmkNotifyEvent;
import im.smack.event.IMSmkNotifyHandlerProxy;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
		IMSmkClient client = new IMSmkClient("IMSMK", "www.zyun168.com", 5223, ProxyType.NONE, null, 0, null, null,
				new IMSmkNotifyHandlerProxy(this), new SwingActorDispatcher());
		client.login("aijiacy", "123456", null, new IMSmkActionListener() {
			@Override
			public void onActionEvent(IMSmkActionEvent event) {
				System.out.println("cc" + event.getType());
			}
		});
	}
}
