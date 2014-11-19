package com.silkroad.core.service;

import com.silkroad.core.action.AbstractAction;
import com.silkroad.core.action.IMAction;
import com.silkroad.core.bean.IMAccount;
import com.silkroad.core.bean.args.IMSocketRequest;
import com.silkroad.core.bean.args.IMSocketResponse;
import com.silkroad.core.context.IMSession;
import com.silkroad.core.event.IMActionEvent;
import com.silkroad.core.event.IMSocketListener;
import com.silkroad.core.exception.IMException;
import com.silkroad.core.manager.SmackManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * Created by caoyong on 2014/7/8.
 */
@Service
public class LoginService extends AbstractAction implements IMAction {
    private static final Logger Log = LoggerFactory.getLogger(LoginService.class);
    private SmackManager smackManager;

    public LoginService(){
        smackManager = SmackManager.getInstance();
    }

    public Future<IMSocketResponse> onExecute(IMSocketRequest request, IMSocketListener listener) {
        Future<IMSocketResponse> futureResponse = null;
        if(getCoreStore().getSession().isUseProxy()) {
            smackManager.setProxyInformation(getCoreStore().getSession().getProxyType(), getCoreStore().getSession().getProxyHost(), getCoreStore().getSession().getProxyPort(), getCoreStore().getSession().getProxyUser(), getCoreStore().getSession().getProxyPwd());
        }
        switch (request.getType()){
            case LOGIN_READY_REQUEST:
                futureResponse = smackManager.initServer(getCoreStore().getSession(), request.getType(), listener);
                break;
            case LOGIN_REQUEST:
                futureResponse = smackManager.loginToServer(getCoreStore().getAccount(), request.getType(), listener);
                break;
        }
        return futureResponse;
    }

    @Override
    public void onSuccess(IMSocketResponse response) {
        switch (response.getType()){
            case LOGIN_READY_REQUEST:
                switch (response.getRetCode()){
                    case 0:
                        notifyActionEvent(IMActionEvent.Type.EVT_OK, response.getRetData());
                        break;
                    default:
                        notifyActionEvent(IMActionEvent.Type.EVT_ERROR, response.getRetData());
                        break;
                }
                break;
            case LOGIN_REQUEST:
                switch (response.getRetCode()){
                    case 0:
                        getCoreStore().getSession().setJid(((IMAccount)response.getRetData()).getJid());
                        notifyActionEvent(IMActionEvent.Type.EVT_OK, response.getRetData());
                        break;
                    default:
                        notifyActionEvent(IMActionEvent.Type.EVT_ERROR, new IMException(IMException.IMErrorCode.UNKNOWN_ERROR, "UNKNOWN_ERROR."));
                        break;
                }
                break;
        }
    }
}
