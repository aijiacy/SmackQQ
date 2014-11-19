package com.silkroad.core.event.future;


import com.silkroad.core.action.IMAction;
import com.silkroad.core.exception.IMException;

/**
 * Created by XJNT-CY on 2014/7/8.
 */
public class IMActionFuture extends AbstractFuture {

    private IMAction action;
    public IMActionFuture(IMAction action) {
        super(action.getActionListener());
        this.action = action;
        this.action.setActionListener(this);
        this.action.setFutureAction(this);
    }

    @Override
    public boolean isCancelable() {
        return this.action.isCancelable();
    }

    @Override
    public void cancel() throws IMException {
        this.action.cancelAction();
    }
}
