//package org.silkroad.im;
//
//import java.io.IOException;
//import java.util.Collection;
//import java.util.Iterator;
//
//import com.silkroad.core.socket.DummySSLSocketFactory;
//import org.jivesoftware.smack.*;
//import org.jivesoftware.smack.packet.Message;
//import org.jivesoftware.smack.packet.Presence;
//import org.jivesoftware.smack.packet.Session;
//import org.jivesoftware.smack.packet.Message.Type;
//import org.jivesoftware.smack.proxy.Socks5ProxySocketFactory;
//import org.jivesoftware.smack.tcp.XMPPTCPConnection;
//import org.jivesoftware.smackx.bytestreams.socks5.Socks5Proxy;
//import org.jivesoftware.smackx.xhtmlim.XHTMLManager;
//import org.jivesoftware.smackx.xhtmlim.XHTMLText;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
///**
// * <b>function:</b> 利用Smack框架完成 XMPP 协议通信
// * @author hoojo
// * @createDate 2012-5-22 上午10:28:18
// * @file ConnectionServerTest.java
// * @package com.hoo.smack.conn
// * @project jwchat
// * @blog http://blog.csdn.net/IBM_hoojo
// * @email hoojo_@126.com
// * @version 1.0
// */
//public class SmackXMPPTest {
//
//    private XMPPConnection connection;
//    private ConnectionConfiguration config;
//    /** openfire服务器address */
//    private final static String server = "localhost";
//
//    private final void fail(Object o) {
//        if (o != null) {
//            System.out.println(o);
//        }
//    }
//
//    private final void fail(Object o, Object... args) {
//        if (o != null && args != null && args.length > 0) {
//            String s = o.toString();
//            for (int i = 0; i < args.length; i++) {
//                String item = args[i] == null ? "" : args[i].toString();
//                if (s.contains("{" + i + "}")) {
//                    s = s.replace("{" + i + "}", item);
//                } else {
//                    s += " " + item;
//                }
//            }
//            System.out.println(s);
//        }
//    }
//
//    /**
//     * <b>function:</b> 初始Smack对openfire服务器链接的基本配置
//     * @author hoojo
//     * @createDate 2012-6-25 下午04:06:42
//     */
//    @Before
//    public void init() {
//        try {
//            //connection = new XMPPConnection(server);
//            //connection.connect();
//            /** 5222是openfire服务器默认的通信端口，你可以登录http://192.168.8.32:9090/到管理员控制台查看客户端到服务器端口 */
//            config = new ConnectionConfiguration(server, 5223);
//            config.setSocketFactory(new DummySSLSocketFactory());
//            /** 是否启用压缩 */
//            config.setCompressionEnabled(true);
//            /** 是否启用安全验证 */
//            config.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
//            /** 是否启用调试 */
//            config.setDebuggerEnabled(true);
//            //config.setReconnectionAllowed(true);
//            //config.setRosterLoadedAtLogin(true);
//            //config.setSendPresence(false);
//
//            //Socks5Proxy.setLocalSocks5ProxyPort(7776);
//            /** 创建connection链接 */
//            connection = new XMPPTCPConnection(config);
//            /** 建立连接 */
//            connection.addConnectionListener(new ConnectionListener() {
//                @Override
//                public void connected(XMPPConnection connection) {
//                    fail("connected");
//                }
//
//                @Override
//                public void authenticated(XMPPConnection connection) {
//                    fail("authenticated");
//                }
//
//                @Override
//                public void connectionClosed() {
//                    fail("connection is Closed");
//                }
//
//                @Override
//                public void connectionClosedOnError(Exception e) {
//                    fail("connection on Closed error." + e);
//                }
//
//                @Override
//                public void reconnectingIn(int i) {
//                    fail("reconnectingIn" + i);
//                }
//
//                @Override
//                public void reconnectionSuccessful() {
//                    fail("reconnectionSuccessful");
//                }
//
//                @Override
//                public void reconnectionFailed(Exception e) {
//                    fail("reconnectionFailed" + e);
//                }
//            });
//            connection.connect();
//        } catch (XMPPException e) {
//            fail("XMPPException: " + e.getMessage());
//        } catch (SmackException e){
//            fail("SmackException: " + e.toString());
//        } catch (IOException e){
//            fail("IOException: " + e.getMessage());
//        }
//        fail(connection);
//        fail(connection.getConnectionID());
//    }
//
//    @After
//    public void destory() {
//        if (connection != null && connection.isConnected()) {
//            try {
//                connection.disconnect();
//                connection = null;
//            }catch (SmackException.NotConnectedException e){
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * <b>function:</b> ConnectionConfiguration 的基本配置相关信息
//     * @author hoojo
//     * @createDate 2012-6-25 下午04:11:25
//     */
//    @Test
//    public void testConfig() {
//        fail("PKCS11Library: " + config.getPKCS11Library());
//        fail("ServiceName: {0}", config.getServiceName());
//        // ssl证书密码
//        fail("TruststorePassword: {0}", config.getPassword());
//        fail("TruststorePath: {0}", config.getKeystorePath());
//        fail("TruststoreType: {0}", config.getKeystoreType());
//
//        //SocketFactory socketFactory = config.getSocketFactory();
//        //fail("SocketFactory: {0}", socketFactory);
//        /*try {
//            fail("createSocket: {0}", socketFactory.createSocket("localhost", 3333));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }*/
//    }
//
//    /**
//     * <b>function:</b> Connection 基本方法信息
//     * @author hoojo
//     * @createDate 2012-6-25 下午04:12:04
//     */
//    @Test
//    public void testConnection() {
//        try {
//            /** 用户管理 */
//            AccountManager accountManager = AccountManager.getInstance(connection);
//            for (String attr : accountManager.getAccountAttributes()) {
//                fail("AccountAttribute: {0}", attr);
//            }
//            fail("AccountInstructions: {0}", accountManager.getAccountInstructions());
//            /** 是否链接 */
//            fail("isConnected:", connection.isConnected());
//            fail("isAnonymous:", connection.isAnonymous());
//            /** 是否有权限 */
//            fail("isAuthenticated:", connection.isAuthenticated());
//            fail("isSecureConnection:", connection.isSecureConnection());
//            /** 是否使用压缩 */
//            fail("isUsingCompression:", connection.isUsingCompression());
//        } catch (XMPPException.XMPPErrorException e){
//            e.printStackTrace();
//        } catch (SmackException e){
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * <b>function:</b> 用户管理器
//     * @author hoojo
//     * @createDate 2012-6-25 下午04:22:31
//     */
//    @Test
//    public void testAccountManager() {
//        AccountManager accountManager = AccountManager.getInstance(connection);
//        try {
//            fail("supportsAccountCreation: {0}", accountManager.supportsAccountCreation());
//            /** 创建一个用户boy，密码为boy；你可以在管理员控制台页面http://192.168.8.32:9090/user-summary.jsp查看用户/组的相关信息，来查看是否成功创建用户 */
//            accountManager.createAccount("aijiacy1", "5201314");
//            /** 修改密码 */
//            accountManager.changePassword("123456");
//        } catch (XMPPException e) {
//            e.printStackTrace();
//        } catch (SmackException e){
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testUser() {
//        try {
//            /** 用户登陆，用户名、密码 */
//            connection.login("aijiacy", "123456");
//        } catch (XMPPException e) {
//            e.printStackTrace();
//        } catch (SmackException e){
//            e.printStackTrace();
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//        /** 获取当前登陆用户 */
//        fail("User:", connection.getUser());
//
//        /** 所有用户组 */
//        Roster roster = connection.getRoster();
//
//        /** 好友用户组，你可以用Spark添加用户好友，这样这里就可以查询到相关的数据 */
//        Collection<RosterEntry> rosterEntiry = roster.getEntries();
//        Iterator<RosterEntry> iter = rosterEntiry.iterator();
//        while (iter.hasNext()) {
//            RosterEntry entry = iter.next();
//            fail("jid:" + entry.getUser());
//            fail("Groups: {0}, Name: {1}, Status: {2}, Type: {3}, User: {4}", entry.getGroups(), entry.getName(), entry.getStatus(), entry.getType(), entry);
//        }
//
//        fail("-------------------------------");
//        /** 未处理、验证好友，添加过的好友，没有得到对方同意 */
//        Collection<RosterEntry> unfiledEntries = roster.getUnfiledEntries();
//        iter = unfiledEntries.iterator();
//        while (iter.hasNext()) {
//            RosterEntry entry = iter.next();
//            fail("Groups: {0}, Name: {1}, Status: {2}, Type: {3}, User: {4}", entry.getGroups(), entry.getName(), entry.getStatus(), entry.getType(), entry);
//        }
//    }
//
//    @Test
//    @SuppressWarnings("static-access")
//    public void testPacket() {
//        try {
//            /** 用户登陆，用户名、密码 */
//            connection.login("aijiacy1", "123456");
//        } catch (XMPPException e) {
//            e.printStackTrace();
//        } catch (SmackException e){
//            e.printStackTrace();
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//
//        //Packet packet = new Data(new DataPacketExtension("jojo@" + server, 2, "this is a message"));
//        //connection.sendPacket(packet);
//        try {
//            /** 更改用户状态，available=true表示在线，false表示离线，status状态签名；当你登陆后，在Spark客户端软件中就可以看到你登陆的状态 */
//            Presence presence = new Presence(Presence.Type.available);
//            presence.setStatus("上线");
//            connection.sendPacket(presence);
//
//            Session session = new Session();
//            String  sessid = session.nextID();
//            connection.sendPacket(session);
//            /** 向jojo@192.168.8.32 发送聊天消息，此时你需要用Spark软件登陆jojo这个用户，
//             * 这样代码就可以向jojo这个用户发送聊天消息，Spark登陆的jojo用户就可以接收到消息
//             **/
//            /** Type.chat 表示聊天，groupchat多人聊天，error错误，headline在线用户； */
//            Message message = new Message("aijiacy1@" + server, Type.chat);
//            //Message message = new Message(sessid, Type.chat);
//            message.setBody("h!~ jojo, I'am is hoojo!");
//            connection.sendPacket(message);
//        } catch (SmackException e){
//            e.printStackTrace();
//        }
//        try {
//            Thread.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * <b>function:</b> 测试聊天消息管理类
//     * @author hoojo
//     * @createDate 2012-6-25 下午05:03:23
//     */
//    @Test
//    public void testChatManager() {
//        /** 设置状态 */
//        try {
//            /** 用户登陆，用户名、密码 */
//            connection.login("aijiacy@" + server, "123456");
//        } catch (XMPPException e) {
//            e.printStackTrace();
//        } catch (SmackException e){
//            e.printStackTrace();
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//        try {
//        /** 设置状态 */
//        Presence presence = new Presence(Presence.Type.available);
//        presence.setStatus("上线");
//        connection.sendPacket(presence);
//
//        /** 获取当前登陆用户的聊天管理器 */
//        ChatManager chatManager = ChatManager.getInstanceFor(connection);
//        /** 为指定用户创建一个chat，MyMessageListeners用于监听对方发过来的消息  */
//        Chat chat = chatManager.createChat("aijiacy@" + server, new MyMessageListeners());
//
////            /** 发送消息 */
////            chat.sendMessage("h!~ jojo……");
////
////            /** 用message对象发送消息 */
////            Message message = new Message();
////            message.setBody("message");
////            XHTMLText xhtmlText = new XHTMLText(null, null);
////            xhtmlText.appendOpenParagraphTag("font-size:normal");
////            xhtmlText.appendOpenSpanTag("color:red");
////            xhtmlText.append("Hello world.");
////            xhtmlText.appendCloseSpanTag();
////            xhtmlText.appendCloseParagraphTag();
////            XHTMLManager.addBody(message, xhtmlText.toString());
////            chat.sendMessage(message);
//
////        } catch (XMPPException e) {
////            e.printStackTrace();
//        } catch (SmackException e){
//            e.printStackTrace();
//        }
//        try {
//            Thread.sleep(1000 * 1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * <b>function:</b> 消息监听器，用户监听对方发送的消息，也可以想对方发送消息
//     * @author hoojo
//     * @createDate 2012-6-25 下午05:05:31
//     * @file SmackXMPPTest.java
//     * @package com.hoo.smack
//     * @project jwchat
//     * @blog http://blog.csdn.net/IBM_hoojo
//     * @email hoojo_@126.com
//     * @version 1.0
//     */
//    class MyMessageListeners implements MessageListener {
//        public void processMessage(Chat chat, Message message) {
//            try {
//                /** 发送消息 */
//                chat.sendMessage("dingding……" + message.getBody());
//            } catch (XMPPException e) {
//                e.printStackTrace();
//            } catch (SmackException e){
//                e.printStackTrace();
//            }
//            /** 接收消息 */
//            fail("From: {0}, To: {1}, Type: {2}, Sub: {3}", message.getFrom(), message.getTo(), message.getType(), message.toXML());
//            /*Collection<Body> bodys =  message.getBodies();
//            for (Body body : bodys) {
//                fail("bodies[{0}]", body.getMessage());
//            }
//            //fail(message.getLanguage());
//            //fail(message.getThread());
//            //fail(message.getXmlns());*/
//            fail("body: ", message.getBody());
//        }
//    }
//}
