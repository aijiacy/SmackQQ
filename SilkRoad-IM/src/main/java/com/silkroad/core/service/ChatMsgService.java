package com.silkroad.core.service;

import com.silkroad.core.action.AbstractAction;
import com.silkroad.core.action.IMAction;
import com.silkroad.core.bean.IMMsg;
import com.silkroad.core.bean.args.IMSocketRequest;
import com.silkroad.core.bean.args.IMSocketResponse;
import com.silkroad.core.event.IMActionEvent;
import com.silkroad.core.event.IMNotifyEvent;
import com.silkroad.core.event.IMSocketListener;
import com.silkroad.core.manager.SmackManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * Created by caoyong on 2014/7/14.
 */
@Service
public class ChatMsgService extends AbstractAction implements IMAction {
    private static final Logger Log = LoggerFactory.getLogger(LoginService.class);
    private SmackManager smackManager;

    public ChatMsgService(){
        this.smackManager = SmackManager.getInstance();
    }

    @Override
    public Future<IMSocketResponse> onExecute(IMSocketRequest request, IMSocketListener listener) {
        Future<IMSocketResponse> futureResponse = null;
        switch (request.getType()){
            case SEND_MSG_REQUEST:
                IMMsg imMsg = (IMMsg) request.getRequestData();
                smackManager.processChatMessage(imMsg, request.getType(), listener);
                break;
            case SEND_SHAKE_REQUEST:
                break;
            case SEND_INPUT_REQUEST:
                break;
        }
        return futureResponse;
    }

    @Override
    public void onSuccess(IMSocketResponse response) {
        switch (response.getType()){
            case SEND_MSG_REQUEST:
                switch (response.getRetCode()){
                    case 0:
                        this.notifyActionEvent(IMActionEvent.Type.EVT_OK, response.getRetData());
                        break;
                    default:
                        this.notifyActionEvent(IMActionEvent.Type.EVT_ERROR, response.getRetData());
                        break;
                }
                break;
            case SEND_SHAKE_REQUEST:
                break;
            case SEND_INPUT_REQUEST:
        }
    }
}
