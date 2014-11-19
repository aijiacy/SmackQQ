package com.silkroad.bridge;

import com.silkroad.client.IMSmkClient;
import com.silkroad.core.actor.SwingActorDispatcher;
import com.silkroad.core.annotation.IMEventHandler;
import com.silkroad.core.bean.*;
import com.silkroad.core.bean.args.LoginArgs;
import com.silkroad.core.context.IMContext;
import com.silkroad.core.context.IMCoreStore;
import com.silkroad.core.context.IMSession;
import com.silkroad.core.event.*;
import com.silkroad.core.event.base.IMApp;
import com.silkroad.core.event.base.IMBridge;
import com.silkroad.ui.bean.*;
import com.silkroad.ui.bean.content.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by caoyong on 2014/7/11.
 */
public class XMPPBridge extends IMEventDispatcher implements IMBridge {
    private static final Logger Log = LoggerFactory.getLogger(XMPPBridge.class);
    private IMApp imApp;
    private IMSmkClient client;

    @Override
    public void setIMApp(IMApp imApp) {
        this.imApp = imApp;
    }

    @IMEventHandler(IMEventType.LOGIN_READY_REQUEST)
    private void processLoginReadyEvent(final IMEvent imEvent){
        LoginArgs loginArgs = (LoginArgs) imEvent.getTarget();
        client = new IMSmkClient(loginArgs, new IMNotifyHandlerProxy(this), new SwingActorDispatcher());
        client.loginReady(new IMActionListener() {
            @Override
            public void onActionEvent(IMActionEvent event) {
                switch (event.getType()){
                    case EVT_OK:
                        broadcastIMEvent(IMEventType.LOGIN_READY_SUCCESS, event.getTarget());
                        break;
                    case EVT_ERROR:
                        broadcastIMEvent(IMEventType.LOGIN_READY_ERROR, event.getTarget());
                        break;
                }
            }
        });
    }

    @IMEventHandler(IMEventType.USER_REG_REQUEST)
    private void processRegAccountEvent(final IMEvent imEvent){
        UIAccount uiAccount = (UIAccount) imEvent.getTarget();
        IMAccount imAccount = new IMAccount();
        imAccount.setUsername(uiAccount.getLoginName());
        imAccount.setPassword(uiAccount.getPassword());
        client.createAccount(imAccount,new IMActionListener() {
            @Override
            public void onActionEvent(IMActionEvent event) {
                switch (event.getType()){
                    case EVT_OK:
                        broadcastIMEvent(IMEventType.USER_REG_SUCCESS, event.getTarget());
                        break;
                    case EVT_ERROR:
                        broadcastIMEvent(IMEventType.USER_REG_ERROR, event.getTarget());
                        break;
                }
            }
        });
    }

    @IMEventHandler(IMEventType.LOGIN_REQUEST)
    private void processLoginRequestEvent(final IMEvent imEvent){
        LoginArgs loginArgs = (LoginArgs) imEvent.getTarget();
        client.initAccount(loginArgs);
        client.login(IMStatus.valueOf(loginArgs.getStatus().name()), new IMActionListener(){
            @Override
            public void onActionEvent(IMActionEvent event) {
                switch (event.getType()){
                    case EVT_OK:
                        try {
                            IMAccount account = (IMAccount) event.getTarget();
                            UIUser uiUser = new UIUser();
                            uiUser.setJid(account.getJid());
                            uiUser.setNick(account.getUsername());
                            uiUser.setUser(account.getUsername());
                            uiUser.setSign("Sign!!!");
                            broadcastIMEvent(IMEventType.LOGIN_SUCCESS, uiUser);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case EVT_ERROR:
                        broadcastIMEvent(IMEventType.LOGIN_ERROR, event.getTarget());
                        break;
                    case EVT_CANCELED:
                        break;
                }
            }
        });
    }

    @IMEventHandler(IMEventType.QUERY_BUDDY_LIST)
    private void processBuddyRequestEvent(IMEvent imEvent){
        client.getBuddyList(new IMActionListener() {
            @Override
            public void onActionEvent(IMActionEvent event) {
                switch (event.getType()){
                    case EVT_OK:
                        List<IMCategory> imCategories = (List<IMCategory>) event.getTarget();
                        List<UIBuddyCategory> uiCategories = new LinkedList<UIBuddyCategory>();
                        for (IMCategory imCategory : imCategories) {
                            UIBuddyCategory uiBuddyCategory = new UIBuddyCategory();
                            uiBuddyCategory.setName(imCategory.getName());
                            uiBuddyCategory.setOnline(imCategory.getOnlineCount());
                            uiBuddyCategory.setTotal(imCategory.getBuddyCount());
                            for(IMBuddy imBuddy : imCategory.getBuddyList()){
                                UIBuddy uiBuddy = new UIBuddy();
                                uiBuddy.setJid(imBuddy.getJid());
                                uiBuddy.setUser(imBuddy.getUsername());
                                uiBuddy.setNick(imBuddy.getNickName());
                                uiBuddy.setSign(imBuddy.getSignMark());
                                uiBuddy.setStatus(UIStatus.valueOf(imBuddy.getStatus().name()));
                                uiBuddyCategory.getBuddyList().add(uiBuddy);
                            }
                            uiCategories.add(uiBuddyCategory);
                        }
                        broadcastIMEvent(IMEventType.BUDDY_LIST_UPDATE, uiCategories);
                        //离线消息
                        client.getOfflineMsg();
                        //轮询消息
                        client.doPollMsg();
                        break;
                }

            }
        });
    }

    @IMEventHandler(IMEventType.CHANGE_RECV_MSG_LANGUAGE)
    private void processChatLanguageEvent(IMEvent imEvent){
        client.getStore().setLanguage(imEvent.getTarget().toString());
    }

    @IMEventHandler(IMEventType.SEND_MSG_REQUEST)
    private void processChatMsgEvent(IMEvent imEvent){
        final UIMsg uiMsg = (UIMsg) imEvent.getTarget();
        IMMsg imMsg = new IMMsg();
        imMsg.setFrom(client.getAccount().getJid());
        imMsg.setTo(uiMsg.getOwner().getJid());
        imMsg.setThread(uiMsg.getOwner().getJid());
        imMsg.setLanguage(uiMsg.getLanguage());
        final List<UIContentItem> uiContentItems = uiMsg.getContents();
        for (UIContentItem item : uiContentItems){
            switch (item.getType()){
                case TEXT:
                    UITextItem uiTextItem = (UITextItem) item;
                    imMsg.addBody(Locale.getDefault().getLanguage(), uiTextItem.getText());
                    break;
                case PIC:
                    UIPictureItem uiPictureItem = (UIPictureItem) item;
                    imMsg.addBody(Locale.getDefault().getLanguage(), uiPictureItem.getFile().toString());
                    break;
                case FACE:
                    UIFaceItem uiFaceItem = (UIFaceItem) item;
                    imMsg.addBody(Locale.getDefault().getLanguage(),uiFaceItem.getKey());
                    break;
            }
        }
        switch (uiMsg.getCategory()){
            case CHAT:
                imMsg.setType(Message.Type.chat);
            break;
        }
        client.sendChatRequest(imMsg,new IMActionListener() {
            @Override
            public void onActionEvent(IMActionEvent event) {
                switch (event.getType()){
                    case EVT_OK:
                        uiMsg.setState(UIMsg.State.SENT);
                        broadcastIMEvent(new IMEvent(IMEventType.SEND_MSG_SUCCESS, uiMsg));
                        break;
                    case EVT_ERROR:
                        uiMsg.setState(UIMsg.State.ERROR);
                        broadcastIMEvent(new IMEvent(IMEventType.SEND_MSG_ERROR, uiMsg));
                        break;
                }
            }
        });
    }

    @IMEventHandler(IMEventType.USER_FIND_REQUEST)
    private void onUserFind(IMEvent imEvent){
        String userName = imEvent.getTarget().toString();
        client.findUser(userName, new IMActionListener() {
            @Override
            public void onActionEvent(IMActionEvent event) {
                switch (event.getType()){
                    case EVT_OK:
                        IMUser imUser = (IMUser) event.getTarget();
                        UIUser uiUser = new UIUser();
                        uiUser.setJid(imUser.getJid());
                        uiUser.setUser(imUser.getJid().substring(0, imUser.getJid().indexOf("@")));
                        uiUser.setNick(imUser.getNickName());
                        broadcastIMEvent(new IMEvent(IMEventType.USER_FIND_SUCCESS, uiUser));
                        break;
                    case EVT_ERROR:
                        broadcastIMEvent(new IMEvent(IMEventType.USER_FIND_ERROR, event.getTarget()));
                        break;
                }
            }
        });
    }

    @IMEventHandler(IMEventType.BUDDY_ADD_REQUEST)
    private void sendAddBuddyRequest(IMEvent imEvent){
        UIBuddy uiBuddy = (UIBuddy) imEvent.getTarget();
        IMBuddy imBuddy = new IMBuddy();
        imBuddy.setJid(uiBuddy.getJid());
        imBuddy.setUsername(uiBuddy.getUser());
        imBuddy.setNickName(uiBuddy.getNick());
        IMCategory imCategory = new IMCategory();
        imCategory.setName(uiBuddy.getCategoryName());
        imBuddy.setCategory(imCategory);
        client.sendAddBuddyRequest(imBuddy,new IMActionListener() {
            @Override
            public void onActionEvent(IMActionEvent event) {
                switch (event.getType()){
                    case EVT_OK:
                        broadcastIMEvent(new IMEvent(IMEventType.BUDDY_ADD_SUCCESS, event.getTarget()));
                        break;
                    case EVT_ERROR:
                        broadcastIMEvent(new IMEvent(IMEventType.BUDDY_ADD_ERROR, event.getTarget()));
                        break;
                }
            }
        });
    }

    @IMEventHandler(IMEventType.REQUEST_ADDED_BUDDY)
    private void sendAddedBuddy(IMEvent imEvent){
        UIBuddy uiBuddy = (UIBuddy) imEvent.getTarget();
        IMBuddy imBuddy = new IMBuddy();
        imBuddy.setJid(uiBuddy.getJid());
        imBuddy.setUsername(uiBuddy.getUser());
        imBuddy.setNickName(uiBuddy.getNick());
        IMCategory imCategory = new IMCategory();
        imCategory.setName(uiBuddy.getCategoryName());
        imBuddy.setCategory(imCategory);
        client.sendAddedBuddy(imBuddy, new IMActionListener() {
            @Override
            public void onActionEvent(IMActionEvent event) {
                switch (event.getType()){
                    case EVT_OK:
                        processBuddyRequestEvent(null);
                        break;
                    case EVT_ERROR:
                        break;
                }
            }
        });
    }


    @IMNotifyHandler(IMNotifyEvent.Type.CHAT_MSG)
    private void fireMessage(IMNotifyEvent event){
        IMMsg imMsg = (IMMsg) event.getTarget();
        UIMsg uiMsg = new UIMsg();

        IMBuddy imBuddy = this.client.getStore().getBuddy(imMsg.getFrom());
        UIUser uiUser = new UIUser();

        uiUser.setJid(imBuddy.getJid());
        uiUser.setStatus(UIStatus.valueOf(imBuddy.getStatus().name()));
        uiUser.setUser(imBuddy.getUsername());
        uiUser.setNick(imBuddy.getNickName());
        uiUser.setSign(imBuddy.getSignMark());
        uiUser.setAvatar(imBuddy.getFace());

        uiMsg.setSender(uiUser);
        uiMsg.setDate(new Date());
        uiMsg.setCategory(UIMsg.Category.CHAT);
        uiMsg.setState(UIMsg.State.UNREAD);
        uiMsg.setDirection(UIMsg.Direction.RECV);
        List<UIContentItem> uiContentItems = new LinkedList<UIContentItem>();
        for (Message.Body body : imMsg.getBodies()){
            UITextItem uiTextItem = new UITextItem(body.getMessage());
            uiContentItems.add(uiTextItem);
        }
//        UITextItem uiTextItem = new UITextItem("ssssss");
//        uiContentItems.add(uiTextItem);
        uiMsg.setContents(uiContentItems);
        broadcastIMEvent(IMEventType.RECV_CHAT_MSG, uiMsg);
    }

    @IMNotifyHandler(IMNotifyEvent.Type.BUDDY_STATUS_CHANGE)
    private void notifyStateChange(IMNotifyEvent event){
        IMBuddy imBuddy = (IMBuddy) event.getTarget();
        UIUser uiUser = new UIUser();
        uiUser.setJid(imBuddy.getJid());
        uiUser.setStatus(UIStatus.valueOf(imBuddy.getStatus().name()));
        uiUser.setUser(imBuddy.getUsername());
        uiUser.setNick(imBuddy.getNickName());
        uiUser.setSign(imBuddy.getSignMark());
        uiUser.setAvatar(imBuddy.getFace());
        broadcastIMEvent(IMEventType.USER_FACE_UPDATE, uiUser);
    }

    @IMNotifyHandler(IMNotifyEvent.Type.BUDDY_NOTIFY)
    private void notifyBuddyRequest(IMNotifyEvent event){
        IMBuddy imBuddy = (IMBuddy) event.getTarget();
        UIBuddy uiBuddy = new UIBuddy();
        uiBuddy.setJid(imBuddy.getJid());
        uiBuddy.setStatus(UIStatus.ONLINE);
        uiBuddy.setUser(imBuddy.getUsername());
        uiBuddy.setNick(imBuddy.getNickName());
        uiBuddy.setSign(imBuddy.getSignMark());
        uiBuddy.setAvatar(imBuddy.getFace());
        broadcastIMEvent(IMEventType.REQUEST_ADD_BUDDY, uiBuddy);
    }

    @IMNotifyHandler(IMNotifyEvent.Type.BUDDY_REQUEST_PASS)
    private void notifyBuddyRequestPass(IMNotifyEvent event){
        processBuddyRequestEvent(null);
    }

    protected void broadcastIMEvent(IMEvent event) {
        imApp.onIMEvent(event);
    }

    protected void broadcastIMEvent(IMEventType type, Object target) {
        imApp.onIMEvent(new IMEvent(type, target));
    }
}
