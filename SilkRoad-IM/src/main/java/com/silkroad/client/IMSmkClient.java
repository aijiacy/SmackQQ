package com.silkroad.client;

import com.silkroad.core.bean.IMAccount;
import com.silkroad.core.bean.IMBuddy;
import com.silkroad.core.bean.IMMsg;
import com.silkroad.core.bean.IMStatus;
import com.silkroad.core.bean.args.IMSocketRequest;
import com.silkroad.core.context.IMContext;
import com.silkroad.core.context.IMCoreStore;
import com.silkroad.core.context.IMSession;
import com.silkroad.core.context.IMStore;
import com.silkroad.core.service.*;
import com.silkroad.core.actor.IMActionActor;
import com.silkroad.core.actor.IMActor;
import com.silkroad.core.actor.IMActorDispatcher;
import com.silkroad.core.bean.args.LoginArgs;
import com.silkroad.core.event.IMActionListener;
import com.silkroad.core.event.IMEventType;
import com.silkroad.core.event.IMNotifyEvent;
import com.silkroad.core.event.IMNotifyListener;
import com.silkroad.core.event.future.IMActionFuture;
import com.silkroad.core.event.future.IMFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by caoyong on 2014/7/8.
 */
public class IMSmkClient implements IMClient, IMCoreStore {
    private static final Logger Log = LoggerFactory.getLogger(IMSmkClient.class);
    private IMAccount account;
    private IMSession session;
    private IMStore store;
    private IMNotifyListener notifyListener;
    private IMActorDispatcher actorDispatcher;


    public IMSmkClient(LoginArgs loginArgs, IMNotifyListener notifyListener, IMActorDispatcher actorDispatcher){

        this.account = new IMAccount();
        this.account.setClientRes(loginArgs.getClientRes());

        this.session = new IMSession();
        this.session.setUseProxy(loginArgs.isUseProxy());
        this.session.setHost(loginArgs.getHost());
        this.session.setPort(loginArgs.getPort());
        this.session.setProxyHost(loginArgs.getProxyHost());
        this.session.setProxyPort(loginArgs.getProxyPort());
        this.session.setProxyUser(loginArgs.getProxyUser());
        this.session.setProxyPwd(loginArgs.getProxyPwd());

        this.store = new IMStore();

        this.notifyListener = notifyListener;
        this.actorDispatcher = actorDispatcher;
    }

    public void initAccount(LoginArgs loginArgs){
        this.account.setUsername(loginArgs.getLoginName());
        this.account.setPassword(loginArgs.getPassword());
        this.account.setStatus(IMStatus.valueOf(loginArgs.getStatus().name()));
    }

    @Override
    public void pushActor(IMActor actor) {
        this.actorDispatcher.pushActor(actor);
    }

    public void fireNotify(IMNotifyEvent event){
        if (notifyListener != null) {
            try {
                notifyListener.onNotifyEvent(event);
            } catch (Throwable e) {
                Log.warn("fireNotify Error!!", e);
            }
        }
        // 这里需要加载通知消息监听器
        if(event.getType() == IMNotifyEvent.Type.RELOGIN_SUCCESS){
            doPollMsg();
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public IMAccount getAccount() {
        return this.account;
    }

    @Override
    public IMSession getSession() {
        return this.session;
    }

    @Override
    public IMStore getStore() {
        return store;
    }

    public IMFuture loginReady(IMActionListener listener){
        LoginService loginService = IMContext.me().getBean(LoginService.class);
        loginService.setCoreStore(this);
        loginService.setActionListener(listener);
        IMFuture smackFuture = new IMActionFuture(loginService);
        this.pushActor(new IMActionActor(IMActionActor.Type.EXECUTE_REQUEST, this, loginService, new IMSocketRequest(IMEventType.LOGIN_READY_REQUEST)));
        return smackFuture;
    }

    @Override
    public IMFuture login(IMStatus status, IMActionListener listener) {
        if(IMStatus.ONLINE == this.session.getStatus()){
            throw new IllegalArgumentException("client is already online.");
        }
        this.session.setStatus(status);

        LoginService loginService = IMContext.me().getBean(LoginService.class);
        loginService.setCoreStore(this);
        loginService.setActionListener(listener);
        IMFuture smackFuture = new IMActionFuture(loginService);
        this.pushActor(new IMActionActor(IMActionActor.Type.EXECUTE_REQUEST, this, loginService, new IMSocketRequest(IMEventType.LOGIN_REQUEST)));
        return smackFuture;
    }

    @Override
    public IMFuture relogin(IMStatus status, IMActionListener listener) {
        return null;
    }

    @Override
    public IMFuture logout(IMActionListener listener) {
        return null;
    }

    @Override
    public IMFuture changeStatus(IMStatus status, IMActionListener listener) {
        return null;
    }

    @Override
    public IMFuture getBuddyList(IMActionListener listener) {
        BuddyService buddyService = IMContext.me().getBean(BuddyService.class);
        buddyService.setCoreStore(this);
        buddyService.setActionListener(listener);
        IMFuture smackFuture = new IMActionFuture(buddyService);
        this.pushActor(new IMActionActor(IMActionActor.Type.EXECUTE_REQUEST,this, buddyService, new IMSocketRequest(IMEventType.QUERY_BUDDY_LIST)));
        return smackFuture;
    }

    /**
     * shake message
     * @param listener
     * @return
     */
    @Override
    public IMFuture sendShakeRequest(IMActionListener listener) {
        ChatMsgService chatMsgService = IMContext.me().getBean(ChatMsgService.class);
        chatMsgService.setCoreStore(this);
        chatMsgService.setActionListener(listener);
        IMFuture smackFuture = new IMActionFuture(chatMsgService);
        this.pushActor(new IMActionActor(IMActionActor.Type.EXECUTE_REQUEST,this, chatMsgService, new IMSocketRequest(IMEventType.SEND_SHAKE_REQUEST)));
        return smackFuture;
    }

    @Override
    public IMFuture sendChatRequest(IMMsg imMsg, IMActionListener listener) {
        ChatMsgService chatMsgService = IMContext.me().getBean(ChatMsgService.class);
        chatMsgService.setCoreStore(this);
        chatMsgService.setActionListener(listener);
        IMFuture smackFuture = new IMActionFuture(chatMsgService);
        this.pushActor(new IMActionActor(IMActionActor.Type.EXECUTE_REQUEST,this, chatMsgService, new IMSocketRequest(IMEventType.SEND_MSG_REQUEST, imMsg)));
        return smackFuture;
    }

    @Override
    public IMFuture sendInputRequest(String jid, IMActionListener listener) {
        ChatMsgService chatMsgService = IMContext.me().getBean(ChatMsgService.class);
        chatMsgService.setCoreStore(this);
        chatMsgService.setActionListener(listener);
        IMFuture smackFuture = new IMActionFuture(chatMsgService);
        this.pushActor(new IMActionActor(IMActionActor.Type.EXECUTE_REQUEST,this, chatMsgService, new IMSocketRequest(IMEventType.SEND_CHAT_MSG, jid)));
        return smackFuture;
    }

    @Override
    public IMFuture getOfflineMsg() {
        OfflineMsgService OfflineMsgService = IMContext.me().getBean(OfflineMsgService.class);
        OfflineMsgService.setCoreStore(this);
        OfflineMsgService.setActionListener(null);
        IMFuture smackFuture = new IMActionFuture(OfflineMsgService);
        this.pushActor(new IMActionActor(IMActionActor.Type.EXECUTE_REQUEST,this, OfflineMsgService, new IMSocketRequest(IMEventType.GET_OFFLINE_MSG_REQUEST)));
        return smackFuture;
    }

    @Override
    public IMFuture doPollMsg() {
        PollMsgService pollMsgService = IMContext.me().getBean(PollMsgService.class);
        pollMsgService.setCoreStore(this);
        pollMsgService.setActionListener(null);
        IMFuture smackFuture = new IMActionFuture(pollMsgService);
        this.pushActor(new IMActionActor(IMActionActor.Type.EXECUTE_REQUEST,this, pollMsgService, new IMSocketRequest(IMEventType.GET_SESSION_MSG_REQUEST)));
        return smackFuture;
    }

    @Override
    public IMFuture createAccount(IMAccount imAccount, IMActionListener listener) {
        AccountService accountService = IMContext.me().getBean(AccountService.class);
        accountService.setCoreStore(this);
        accountService.setActionListener(listener);
        IMFuture smackFuture = new IMActionFuture(accountService);
        this.pushActor(new IMActionActor(IMActionActor.Type.EXECUTE_REQUEST,this, accountService, new IMSocketRequest(IMEventType.USER_REG_REQUEST,imAccount)));
        return smackFuture;
    }

    @Override
    public IMFuture findUser(String userName, IMActionListener listener) {
        AccountService accountService = IMContext.me().getBean(AccountService.class);
        accountService.setCoreStore(this);
        accountService.setActionListener(listener);
        IMFuture smackFuture = new IMActionFuture(accountService);
        this.pushActor(new IMActionActor(IMActionActor.Type.EXECUTE_REQUEST,this, accountService, new IMSocketRequest(IMEventType.USER_FIND_REQUEST, userName)));
        return smackFuture;
    }

    @Override
    public IMFuture sendAddBuddyRequest(IMBuddy imBuddy, IMActionListener listener) {
        BuddyService buddyService = IMContext.me().getBean(BuddyService.class);
        buddyService.setCoreStore(this);
        buddyService.setActionListener(listener);
        IMFuture smackFuture = new IMActionFuture(buddyService);
        this.pushActor(new IMActionActor(IMActionActor.Type.EXECUTE_REQUEST,this, buddyService, new IMSocketRequest(IMEventType.BUDDY_ADD_REQUEST, imBuddy)));
        return smackFuture;
    }

    @Override
    public IMFuture sendAddedBuddy(IMBuddy imBuddy, IMActionListener listener) {
        BuddyService buddyService = IMContext.me().getBean(BuddyService.class);
        buddyService.setCoreStore(this);
        buddyService.setActionListener(listener);
        IMFuture smackFuture = new IMActionFuture(buddyService);
        this.pushActor(new IMActionActor(IMActionActor.Type.EXECUTE_REQUEST,this, buddyService, new IMSocketRequest(IMEventType.REQUEST_ADDED_BUDDY, imBuddy)));
        return smackFuture;
    }
}
