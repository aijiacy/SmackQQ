package com.silkroad.core.service;

import com.silkroad.core.action.AbstractAction;
import com.silkroad.core.action.IMAction;
import com.silkroad.core.bean.IMBuddy;
import com.silkroad.core.bean.IMCategory;
import com.silkroad.core.bean.args.IMSocketRequest;
import com.silkroad.core.bean.args.IMSocketResponse;
import com.silkroad.core.event.IMActionEvent;
import com.silkroad.core.event.IMSocketListener;
import com.silkroad.core.exception.IMException;
import com.silkroad.core.manager.SmackManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by caoyong on 2014/7/12.
 */
@Service
public class BuddyService extends AbstractAction implements IMAction {
    private static final Logger Log = LoggerFactory.getLogger(BuddyService.class);
    private SmackManager smackManager;
    public BuddyService(){this.smackManager = SmackManager.getInstance();}

    @Override
    public Future<IMSocketResponse> onExecute(IMSocketRequest request, IMSocketListener listener) {
        Future<IMSocketResponse> futureResponse = null;
        switch (request.getType()){
            case QUERY_BUDDY_LIST:
                futureResponse = smackManager.doGetBuddyList(request.getType(), listener);
                break;
            case BUDDY_ADD_REQUEST:
                IMBuddy imBuddy = (IMBuddy) request.getRequestData();
                futureResponse = smackManager.addBuddy(imBuddy.getJid(),imBuddy.getUsername(), imBuddy.getCategory().getName(),request.getType(), listener);
                break;
            case REQUEST_ADDED_BUDDY:
                IMBuddy imBuddyTo = (IMBuddy) request.getRequestData();
                futureResponse = smackManager.addedBuddy(imBuddyTo.getJid(),imBuddyTo.getUsername(), imBuddyTo.getCategory().getName(),request.getType(), listener);
                break;
        }
        return futureResponse;
    }

    @Override
    public void onSuccess(IMSocketResponse response) {
        switch (response.getType()){
            case QUERY_BUDDY_LIST:
                switch (response.getRetCode()){
                    case 0:
                        List<IMCategory> imCategories = (List<IMCategory>) response.getRetData();
                        for (IMCategory imCategory : imCategories) {
                            getCoreStore().getStore().addCategory(imCategory);
                            List<IMBuddy> imBuddies = imCategory.getBuddyList();
                            for (IMBuddy imBuddy : imBuddies){
                                imBuddy.setJid(imBuddy.getJid() + "/" + getCoreStore().getAccount().getClientRes());
                                getCoreStore().getStore().addBuddy(imBuddy);
                            }
                        }
                        notifyActionEvent(IMActionEvent.Type.EVT_OK, response.getRetData());
                        break;
                    default:
                        notifyActionEvent(IMActionEvent.Type.EVT_ERROR, new IMException(IMException.IMErrorCode.UNKNOWN_ERROR, "UNKNOWN_ERROR."));
                        break;
                }
                break;
            case BUDDY_ADD_REQUEST:
                switch (response.getRetCode()){
                    case 0:
                        notifyActionEvent(IMActionEvent.Type.EVT_OK, response.getRetData());
                        break;
                    default:
                        break;
                }
                break;
            case REQUEST_ADDED_BUDDY:
                switch (response.getRetCode()){
                    case 0:
                        notifyActionEvent(IMActionEvent.Type.EVT_OK, response.getRetData());
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }
}
