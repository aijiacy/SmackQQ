package com.silkroad.ui.moudle;

import com.silkroad.bridge.XMPPBridge;
import com.silkroad.core.annotation.IMEventHandler;
import com.silkroad.core.event.IMEvent;
import com.silkroad.core.event.IMEventDispatcher;
import com.silkroad.core.event.IMEventType;
import com.silkroad.core.event.base.IMApp;
import com.silkroad.core.event.base.IMBridge;
import com.silkroad.core.exception.IMException;
import com.silkroad.ui.event.UIEvent;
import com.silkroad.ui.event.UIEventDispatcher;
import com.silkroad.ui.event.UIEventHandler;
import com.silkroad.ui.event.UIEventType;
import com.silkroad.ui.service.EventService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by caoyong on 2014/7/11.
 */
@Service
public class LogicMoudle extends IMEventDispatcher implements IMApp {

    private IMBridge imBirdge;

    @Resource
    private EventService eventService;

    @PostConstruct
    public void onInit(){
        imBirdge = new XMPPBridge();
        imBirdge.setIMApp(this);

        UIEventDispatcher uiEventDispatcher = new UIEventDispatcher(this);
        eventService.register(uiEventDispatcher.getEventTypes(), uiEventDispatcher);

    }

    @UIEventHandler(UIEventType.LOGIN_READY_REQUEST)
    private void onLoginReadyEvent(UIEvent uiEvent){
        imBirdge.onIMEvent(new IMEvent(IMEventType.LOGIN_READY_REQUEST, uiEvent.getTarget()));
    }

    @IMEventHandler(IMEventType.LOGIN_READY_SUCCESS)
    private void onLoginReadySuccess(IMEvent imEvent){
        eventService.broadcast(new UIEvent(UIEventType.LOGIN_READY_SUCCESS,imEvent.getTarget()));
    }

    @IMEventHandler(IMEventType.LOGIN_READY_ERROR)
    private void onLoginReadyError(IMEvent imEvent){
        eventService.broadcast(new UIEvent(UIEventType.LOGIN_READY_ERROR,imEvent.getTarget()));
    }

    @UIEventHandler(UIEventType.LOGIN_REQUEST)
    private void onLoginEvent(UIEvent uiEvent){
        imBirdge.onIMEvent(new IMEvent(IMEventType.LOGIN_REQUEST, uiEvent.getTarget()));
    }

    @UIEventHandler(UIEventType.BUDDY_LIST_REQUEST)
    private void onBuddyEvent(UIEvent uiEvent){
        imBirdge.onIMEvent(new IMEvent(IMEventType.QUERY_BUDDY_LIST, uiEvent.getTarget()));
    }

    @UIEventHandler(UIEventType.USER_REG_REQUEST)
    private void onRegAccountEvent(UIEvent uiEvent){
        imBirdge.onIMEvent(new IMEvent(IMEventType.USER_REG_REQUEST, uiEvent.getTarget()));
    }

    @IMEventHandler(IMEventType.USER_REG_SUCCESS)
    protected void processRegAccountSuccess(IMEvent imEvent){
        eventService.broadcast(new UIEvent(UIEventType.USER_REG_SUCCESS,imEvent.getTarget()));
    }

    @IMEventHandler(IMEventType.USER_REG_ERROR)
    protected void processRegAccountError(IMEvent imEvent){
        eventService.broadcast(new UIEvent(UIEventType.USER_REG_ERROR,imEvent.getTarget()));
    }

    @IMEventHandler(IMEventType.LOGIN_SUCCESS)
    protected void processLoginSuccess(IMEvent imEvent){
        eventService.broadcast(new UIEvent(UIEventType.LOGIN_SUCCESS,imEvent.getTarget()));
    }

    @IMEventHandler(IMEventType.LOGIN_ERROR)
    protected void processLoginError(IMEvent imEvent){
        IMException ex = (IMException) imEvent.getTarget();
        eventService.broadcast(new UIEvent(UIEventType.LOGIN_ERROR, ex.getMessage()));
    }

    @IMEventHandler(IMEventType.SELF_FACE_UPDATE)
    protected void processSelfFaceUpdate(IMEvent imEvent){
        eventService.broadcast(new UIEvent(UIEventType.SELF_FACE_UPDATE, imEvent.getTarget()));
    }

    @IMEventHandler(IMEventType.SELF_STATUS_UPDATE)
    protected void processSelfStatusUpdate(IMEvent imEvent){
        eventService.broadcast(new UIEvent(UIEventType.SELF_STATUS_UPDATE, imEvent.getTarget()));
    }

    @IMEventHandler(IMEventType.SELF_INFO_UPDATE)
    protected void processSelfInfoUpdate(IMEvent imEvent){
        eventService.broadcast(new UIEvent(UIEventType.SELF_INFO_UPDATE, imEvent.getTarget()));
    }

    @IMEventHandler(IMEventType.SELF_SIGN_UPDATE)
    protected void processSelfSignUpdate(IMEvent imEvent){
        eventService.broadcast(new UIEvent(UIEventType.SELF_SIGN_UPDATE, imEvent.getTarget()));
    }

    @IMEventHandler(IMEventType.BUDDY_LIST_UPDATE)
    protected void processBuddyListUpdate(IMEvent imEvent){
        eventService.broadcast(new UIEvent(UIEventType.BUDDY_LIST_UPDATE, imEvent.getTarget()));
    }

    @IMEventHandler(IMEventType.USER_FACE_UPDATE)
    protected void processUserFaceUpdate(IMEvent imEvent){
        eventService.broadcast(new UIEvent(UIEventType.USER_FACE_UPDATE, imEvent.getTarget()));
    }

    @UIEventHandler(UIEventType.CHANGE_MSG_LANGUAGE)
    private void onChangeLanguageEvent(UIEvent uiEvent){
        imBirdge.onIMEvent(new IMEvent(IMEventType.CHANGE_RECV_MSG_LANGUAGE, uiEvent.getTarget()));
    }

    @UIEventHandler(UIEventType.SEND_CHATMSG_REQUEST)
    private void onSendMsgEvent(UIEvent uiEvent){
        imBirdge.onIMEvent(new IMEvent(IMEventType.SEND_MSG_REQUEST, uiEvent.getTarget()));
    }

    @IMEventHandler(IMEventType.SEND_MSG_SUCCESS)
    protected void processSendMsgSuccess(IMEvent imEvent){
        eventService.broadcast(new UIEvent(UIEventType.SEND_CHATMSG_SUCCESS,imEvent.getTarget()));
    }

    @IMEventHandler(IMEventType.SEND_MSG_ERROR)
    protected void processSendMsgError(IMEvent imEvent){
        eventService.broadcast(new UIEvent(UIEventType.SEND_CHATMSG_ERROR,imEvent.getTarget()));
    }

    @IMEventHandler(IMEventType.RECV_CHAT_MSG)
    protected void processRecvMsg(IMEvent imEvent){
        eventService.broadcast(new UIEvent(UIEventType.RECV_CHATMSG_SUCCESS,imEvent.getTarget()));
    }

    @UIEventHandler(UIEventType.USER_FIND_REQUEST)
    protected void onUserFind(UIEvent uiEvent){
        imBirdge.onIMEvent(new IMEvent(IMEventType.USER_FIND_REQUEST, uiEvent.getTarget()));
    }

    @IMEventHandler(IMEventType.USER_FIND_SUCCESS)
    protected void processUserFindSuccess(IMEvent imEvent){
        eventService.broadcast(new UIEvent(UIEventType.USER_FIND_SUCCESS,imEvent.getTarget()));
    }

    @IMEventHandler(IMEventType.USER_FIND_ERROR)
    protected void processUserFindError(IMEvent imEvent){
        eventService.broadcast(new UIEvent(UIEventType.USER_FIND_ERROR,imEvent.getTarget()));
    }

    @UIEventHandler(UIEventType.BUDDY_ADD_REQUEST)
    protected void onAddBuddy(UIEvent uiEvent){
        imBirdge.onIMEvent(new IMEvent(IMEventType.BUDDY_ADD_REQUEST, uiEvent.getTarget()));
    }

    @IMEventHandler(IMEventType.BUDDY_ADD_SUCCESS)
    protected void processAddBuddySuccess(IMEvent imEvent){
        eventService.broadcast(new UIEvent(UIEventType.BUDDY_ADD_SUCCESS,imEvent.getTarget()));
    }

    @IMEventHandler(IMEventType.BUDDY_ADD_ERROR)
    protected void processAddBuddyError(IMEvent imEvent){
        eventService.broadcast(new UIEvent(UIEventType.BUDDY_ADD_ERROR,imEvent.getTarget()));
    }

    @IMEventHandler(IMEventType.REQUEST_ADD_BUDDY)
    protected void requestAddBuddy(IMEvent imEvent){
        eventService.broadcast(new UIEvent(UIEventType.REQUEST_ADD_BUDDY,imEvent.getTarget()));
    }

    @UIEventHandler(UIEventType.REQUEST_ADDED_BUDDY)
    protected void requestAddedBuddy(UIEvent uiEvent){
        imBirdge.onIMEvent(new IMEvent(IMEventType.REQUEST_ADDED_BUDDY, uiEvent.getTarget()));
    }
}
