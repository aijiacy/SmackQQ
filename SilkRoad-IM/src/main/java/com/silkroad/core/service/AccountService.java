package com.silkroad.core.service;

import com.silkroad.core.action.AbstractAction;
import com.silkroad.core.action.IMAction;
import com.silkroad.core.bean.IMAccount;
import com.silkroad.core.bean.args.IMSocketRequest;
import com.silkroad.core.bean.args.IMSocketResponse;
import com.silkroad.core.event.IMActionEvent;
import com.silkroad.core.event.IMSocketListener;
import com.silkroad.core.exception.IMException;
import com.silkroad.core.manager.SmackManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * Created by caoyong on 2014/7/28.
 */
@Service
public class AccountService extends AbstractAction implements IMAction {
    private static final Logger Log = LoggerFactory.getLogger(AccountService.class);
    private SmackManager smackManager;

    public AccountService() {
        this.smackManager = SmackManager.getInstance();
    }

    @Override
    public Future<IMSocketResponse> onExecute(IMSocketRequest request, IMSocketListener listener) {
        Future<IMSocketResponse> futureResponse = null;
        switch (request.getType()){
            case USER_REG_REQUEST:
                IMAccount imAccount = (IMAccount) request.getRequestData();
                futureResponse = smackManager.createAccount(imAccount,request.getType(),listener);
                break;
            case USER_FIND_REQUEST:
                futureResponse = smackManager.searchUser(request.getRequestData().toString(), request.getType() ,listener);
                break;
        }
        return futureResponse;
    }

    @Override
    public void onSuccess(IMSocketResponse response) {
        switch (response.getType()){
            case USER_REG_REQUEST:
                switch (response.getRetCode()){
                    case 0:
                        notifyActionEvent(IMActionEvent.Type.EVT_OK, response.getRetData());
                        break;
                    default:
                        notifyActionEvent(IMActionEvent.Type.EVT_ERROR, new IMException(IMException.IMErrorCode.UNKNOWN_ERROR, "UNKNOWN_ERROR."));
                        break;
                }
                break;
            case USER_FIND_REQUEST:
                switch (response.getRetCode()){
                    case 0:
                        notifyActionEvent(IMActionEvent.Type.EVT_OK, response.getRetData());
                        break;
                    default:
                        notifyActionEvent(IMActionEvent.Type.EVT_ERROR, new IMException(IMException.IMErrorCode.UNKNOWN_ERROR, "UNKNOWN_ERROR."));
                        break;
                }
                break;
        }
        super.onSuccess(response);
    }
}
