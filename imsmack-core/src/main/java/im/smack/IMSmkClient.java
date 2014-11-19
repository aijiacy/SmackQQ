package im.smack;

import im.smack.actor.IMSmkActor;
import im.smack.actor.IMSmkActorDispatcher;
import im.smack.bean.IMSmkAccount;
import im.smack.bean.IMSmkMsg;
import im.smack.bean.IMSmkStatus;
import im.smack.core.*;
import im.smack.core.IMSmkSession.State;
import im.smack.event.IMSmkActionFuture;
import im.smack.event.IMSmkNotifyEvent;
import im.smack.module.BuddyModule;
import im.smack.module.LoginModule;
import im.smack.service.BuddyService;
import im.smack.service.LoginService;
import im.smack.socket.IMSmkSocketConn;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.proxy.ProxyInfo;
import org.jivesoftware.smack.proxy.ProxyInfo.ProxyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IMSmkClient implements IMClient, IMSmkContext {
    private static final Logger LOG = LoggerFactory.getLogger(IMSmkClient.class);
    private Map<IMSmkService.Type, IMSmkService> services;
    private Map<IMSmkModule.Type, IMSmkModule> modules;
    private IMSmkActorDispatcher actorDispatcher;
    private IMSmkAccount account;
    private IMSmkSession session;
    private IMSmkStore store;
    private IMSmkNotifyListener notifyListener;

    /**
     * 初始化服务信息
     * @param clientName
     * @param host
     * @param port
     * @param type
     * @param pHost
     * @param pPort
     * @param pUser
     * @param pPass
     * @param notifyListener
     * @param actorDispatcher
     */
    public IMSmkClient(String clientName, String host, int port, ProxyType type, String pHost, int pPort, String pUser,
                       String pPass, IMSmkNotifyListener notifyListener, IMSmkActorDispatcher actorDispatcher) {
        this.modules = new HashMap<IMSmkModule.Type, IMSmkModule>(0);
        this.services = new HashMap<IMSmkService.Type, IMSmkService>(0);

        this.modules.put(IMSmkModule.Type.LOGIN, new LoginModule());
        this.modules.put(IMSmkModule.Type.BUDDY, new BuddyModule());

        this.services.put(IMSmkService.Type.SOCKET_LOGIN, new LoginService());
        this.services.put(IMSmkService.Type.SOCKET_BUDDY, new BuddyService());

        this.account = new IMSmkAccount();
        this.session = new IMSmkSession();
        this.session.setClientOrigin(clientName);
        this.session.setSvrAddr(host);
        this.session.setSvrPort(port);
        ProxyInfo proxyInfo = new ProxyInfo(type, pHost, pPort, pUser, pPass);
        this.session.setProxyInfo(proxyInfo);
        this.store = new IMSmkStore();

        this.actorDispatcher = actorDispatcher;
        this.notifyListener = notifyListener;

        this.init();
    }

    /**
     * 初始化所有模块和服务
     */
    private void init() {
        try {
            for (Map.Entry<IMSmkService.Type, IMSmkService> entry : this.services.entrySet()) {
                IMSmkService service = entry.getValue();
                service.init(this);
            }
            for (Map.Entry<IMSmkModule.Type, IMSmkModule> entry : this.modules.entrySet()) {
                IMSmkModule module = entry.getValue();
                module.init(this);
            }
            //连接初始化
            IMSmkSocketConn sktConn = IMSmkSocketConn.getInstance(this.session.getSvrAddr(), this.session.getSvrPort(), this.session.getProxyInfo());
            this.session.setConnection(sktConn.getConnection());
        } catch (IMSmkException ex) {
            LOG.warn("init error:", ex);
        }
    }

    @Override
    public void destory() {
        try {
            for (Map.Entry<IMSmkService.Type, IMSmkService> entry : this.services.entrySet()) {
                IMSmkService service = entry.getValue();
                service.destroy();
            }
            for (Map.Entry<IMSmkModule.Type, IMSmkModule> entry : this.modules.entrySet()) {
                IMSmkModule module = entry.getValue();
                module.destroy();
            }
            this.actorDispatcher.destroy();
        } catch (IMSmkException ex) {
            LOG.warn("destroy error:", ex);
        }
    }

    @Override
    public void pushActor(IMSmkActor actor) {
        this.actorDispatcher.pushActor(actor);
    }

    @Override
    public void fireNotify(IMSmkNotifyEvent event) {
        if (notifyListener != null) {
            try {
                notifyListener.onNotifyEvent(event);
            } catch (Throwable e) {
                LOG.warn("fireNotify Error!!", e);
            }
        }
        // 这里需要加载通知消息监听器
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IMSmkModule> T getModule(im.smack.core.IMSmkModule.Type type) {
        // TODO Auto-generated method stub
        return (T) this.modules.get(type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IMSmkService> T getSerivce(im.smack.core.IMSmkService.Type type) {
        return (T) this.services.get(type);
    }

    @Override
    public IMSmkAccount getAccount() {
        return this.account;
    }

    @Override
    public IMSmkSession getSession() {
        return this.session;
    }

    @Override
    public IMSmkStore getStore() {
        return this.store;
    }

    @Override
    public IMSmkActionFuture login(String username, String password, IMSmkStatus status,
                                   final IMSmkActionListener listener) {
        if (session.getState() == State.ONLINE) {
            throw new IllegalArgumentException("Client is aready online!!!");
        }
        this.getAccount().setUsername(username);
        this.getAccount().setPassword(password);
        this.getAccount().setStatus(status);
        LoginModule loginModule = getModule(IMSmkModule.Type.LOGIN);
        return loginModule.login(listener);
    }

    @Override
    public IMSmkActionFuture relogin(IMSmkStatus status, final IMSmkActionListener listener) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IMSmkActionFuture loginout(final IMSmkActionListener listener) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IMSmkActionFuture changeStatus(IMSmkStatus status, final IMSmkActionListener listener) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IMSmkActionFuture getUserFace(IMSmkMsg msg, OutputStream picout, final IMSmkActionListener listener) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 获取好友分组信息
     * @param listener
     * @return
     */
    @Override
    public IMSmkActionFuture getBundyList(IMSmkActionListener listener) {
        BuddyModule bundyModule = getModule(IMSmkModule.Type.BUDDY);
        return bundyModule.getBuddyList(listener);
    }
}
