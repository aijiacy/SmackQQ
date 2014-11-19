package com.silkroad.core.service;

import com.silkroad.core.action.AbstractAction;
import com.silkroad.core.action.IMAction;
import com.silkroad.core.bean.*;
import com.silkroad.core.bean.args.IMSocketRequest;
import com.silkroad.core.bean.args.IMSocketResponse;
import com.silkroad.core.event.IMNotifyEvent;
import com.silkroad.core.event.IMSocketListener;
import com.silkroad.core.manager.SmackManager;
import com.silkroad.util.ClientUtil;
import com.silkroad.util.LanguageUtils;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by caoyong on 2014/7/13.
 */
@Service
public class PollMsgService extends AbstractAction implements IMAction {
    private static final Logger Log = LoggerFactory.getLogger(LoginService.class);
    private SmackManager smackManager;
    public PollMsgService(){
        this.smackManager = SmackManager.getInstance();
    }

    @Override
    public Future<IMSocketResponse> onExecute(IMSocketRequest request, IMSocketListener listener) {
        Future<IMSocketResponse> futureResponse = null;
        switch (request.getType()){
            case GET_SESSION_MSG_REQUEST:
                futureResponse = smackManager.doPollMessage(request.getType(), listener);
                break;
        }
        return futureResponse;
    }

    @Override
    public void onSuccess(IMSocketResponse response) {
        LanguageUtils languageUtils = new LanguageUtils();
        String language = getCoreStore().getStore().getLanguage();
        switch (response.getRetCode()) {
            case 0:
                Packet packet = (Packet) response.getRetData();
                String form = ClientUtil.convLocalJID(getCoreStore().getAccount().getClientRes(), packet.getFrom());
                String to = ClientUtil.convLocalJID(getCoreStore().getAccount().getClientRes(), packet.getTo());
                packet.setFrom(form);
                packet.setTo(to);
                if(packet instanceof Message) { //接收到消息
                    Message message = (Message) packet;
                    IMMsg imMsg = new IMMsg();
                    imMsg.setThread(message.getThread());
                    imMsg.setFrom(message.getFrom());
                    imMsg.setTo(message.getTo());
                    imMsg.setType(message.getType());
                    imMsg.setError(message.getError());
                    imMsg.setLanguage(message.getLanguage());
                    imMsg.setPacketID(message.getPacketID());
                    for (Message.Body body : message.getBodies()){
                        String transMessage = languageUtils.transLanguage(language, body.getMessage());
                        imMsg.addBody(body.getLanguage(), transMessage);
                    }
                    for (Message.Subject subject : message.getSubjects()){
                        imMsg.addSubject(subject.getLanguage(),subject.getSubject());
                    }
                    this.getCoreStore().fireNotify(new IMNotifyEvent(IMNotifyEvent.Type.CHAT_MSG, imMsg));
                }else if( packet instanceof Presence){ //状态变更
                    Presence presence = (Presence) packet;
                    if(presence.getFrom().equals(presence.getTo())){

                        switch (presence.getType()) {
                            case available:
                                getCoreStore().getAccount().setStatus(IMStatus.ONLINE);
                                getCoreStore().getSession().setStatus(IMStatus.ONLINE);
                                break;
                            default:
                                getCoreStore().getAccount().setStatus(IMStatus.OFFLINE);
                                getCoreStore().getSession().setStatus(IMStatus.OFFLINE);
                                break;
                        }
                    }else {
                        IMBuddy imBuddy = null;
                        switch (presence.getType()) {
                            case subscribe: //好友请求
                                imBuddy = new IMBuddy();
                                imBuddy.setJid(presence.getFrom());
                                imBuddy.setUsername(StringUtils.parseName(presence.getFrom()));
                                imBuddy.setNickName(StringUtils.parseName(presence.getFrom()));
                                this.getCoreStore().fireNotify(new IMNotifyEvent(IMNotifyEvent.Type.BUDDY_NOTIFY, imBuddy));
                                break;
                            case subscribed: //请求已接受
                                //smackManager.addBuddy(presence.getFrom(),StringUtils.parseName(presence.getFrom()), "Friends");
                                this.getCoreStore().fireNotify(new IMNotifyEvent(IMNotifyEvent.Type.BUDDY_REQUEST_PASS, null));
                                break;
                            case unsubscribe:
                                break;
                            case unsubscribed:
                                break;
                            case error:
                                break;
                            default:
                                imBuddy = getCoreStore().getStore().getBuddy(presence.getFrom());
                                if(presence.getType() == Presence.Type.available){
                                    imBuddy.setStatus(IMStatus.ONLINE);
                                }else{
                                    imBuddy.setStatus(IMStatus.OFFLINE);
                                }
                                Map<String, IMCategory> categoryMap = getCoreStore().getStore().getCategoryMap();
                                Iterator<String> itKeys = categoryMap.keySet().iterator();
                                while (itKeys.hasNext()) {
                                    String key = itKeys.next();
                                    for (IMBuddy buddy : categoryMap.get(key).getBuddyList()) {
                                        if (buddy.getJid().equals(presence.getFrom())) {
                                            buddy.setStatus(imBuddy.getStatus());
                                        }
                                    }
                                }
                                this.getCoreStore().fireNotify(new IMNotifyEvent(IMNotifyEvent.Type.BUDDY_STATUS_CHANGE, imBuddy));
                                break;
                        }
                    }
                }else if( packet instanceof IQ){

                }
                break;
            default:
                break;
        }
    }
}
