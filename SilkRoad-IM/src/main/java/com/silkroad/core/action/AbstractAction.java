package com.silkroad.core.action;

import com.silkroad.core.bean.args.IMSocketResponse;
import com.silkroad.core.context.IMCoreStore;
import com.silkroad.core.event.IMActionEvent;
import com.silkroad.core.event.IMActionListener;
import com.silkroad.core.event.future.IMFuture;
import com.silkroad.core.exception.IMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;

/**
 * Created by caoyong on 2014/7/8.
 */
public abstract class AbstractAction implements IMAction {

    private static final Logger Log = LoggerFactory.getLogger(AbstractAction.class);
    private IMCoreStore coreStore;
    private IMActionListener listener;
    private Future<IMSocketResponse> futureResponse;
    private IMFuture futureAction;
    private int retryTimes;

    public AbstractAction(){
        this.retryTimes = 0;
    }

    @Override
    public IMCoreStore getCoreStore() {
        return this.coreStore;
    }

    @Override
    public void setCoreStore(IMCoreStore coreStore) {
        this.coreStore = coreStore;
    }

    @Override
    public IMActionListener getActionListener() {
        return this.listener;
    }

    @Override
    public void setActionListener(IMActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void setFutureAction(IMFuture futureAction) {
        this.futureAction = futureAction;
    }

    @Override
    public void setFutureResponse(Future<IMSocketResponse> futureResponse) {
        this.futureResponse = futureResponse;
    }

    @Override
    public void cancelAction() throws IMException {
        futureResponse.cancel(true);
        notifyActionEvent(IMActionEvent.Type.EVT_CANCELED, null);
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

    @Override
    public void notifyActionEvent(IMActionEvent.Type type, Object target) {
        if(null != listener){
            listener.onActionEvent(new IMActionEvent(type, target));
        }
    }

    @Override
    public void onSuccess(IMSocketResponse response) {
        Log.debug("call success.");
        if(response != null){
            switch (response.getRetCode()){
                case 0:
                    notifyActionEvent(IMActionEvent.Type.EVT_OK, response.getRetData());
                    break;
                default:
                    notifyActionEvent(IMActionEvent.Type.EVT_ERROR, new IMException(IMException.IMErrorCode.UNKNOWN_ERROR, "UNKNOWN_ERROR."));
                    break;
            }
        }
    }

    @Override
    public void onFailed(Throwable te) {
        Log.debug("call error.");
        notifyActionEvent(IMActionEvent.Type.EVT_ERROR, te);
    }
}
