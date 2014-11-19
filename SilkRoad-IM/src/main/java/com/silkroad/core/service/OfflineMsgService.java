package com.silkroad.core.service;

import com.silkroad.core.action.AbstractAction;
import com.silkroad.core.action.IMAction;
import com.silkroad.core.bean.args.IMSocketRequest;
import com.silkroad.core.bean.args.IMSocketResponse;
import com.silkroad.core.event.IMSocketListener;
import com.silkroad.core.manager.SmackManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * Created by caoyong on 2014/7/13.
 */
@Service
public class OfflineMsgService extends AbstractAction implements IMAction {
    private static final Logger Log = LoggerFactory.getLogger(LoginService.class);
    private SmackManager smackManager;
    public OfflineMsgService(){
        this.smackManager = SmackManager.getInstance();
    }

    @Override
    public Future<IMSocketResponse> onExecute(IMSocketRequest request, IMSocketListener listener) {
        Future<IMSocketResponse> futureResponse = null;
        switch (request.getType()){
            case GET_OFFLINE_MSG_REQUEST:
                futureResponse = smackManager.doGetOfflineMessage(request.getType(), listener);
                break;
        }
        return futureResponse;
    }
}
