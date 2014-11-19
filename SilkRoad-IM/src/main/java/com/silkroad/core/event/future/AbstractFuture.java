package com.silkroad.core.event.future;


import com.silkroad.core.event.IMActionEvent;
import com.silkroad.core.event.IMActionListener;
import com.silkroad.core.exception.IMException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by XJNT-CY on 2014/7/8.
 */
public abstract class AbstractFuture implements IMFuture, IMActionListener {

    private IMActionListener proxyListener;
    private BlockingQueue<IMActionEvent> eventQueue;
    private volatile boolean isCanceled;
    private volatile boolean hasEvent;

    public AbstractFuture(IMActionListener proxyListener) {
        this.hasEvent = true;
        this.proxyListener = proxyListener;
        this.eventQueue = new LinkedBlockingQueue<IMActionEvent>();
    }

    @Override
    public void onActionEvent(IMActionEvent event) {
        if (proxyListener != null) {
            proxyListener.onActionEvent(event);
        }
        eventQueue.add(event);

    }

    @Override
    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean isCanceled){
        this.isCanceled = isCanceled;
    }

    @Override
    public IMActionEvent waitEvent() throws IMException {
        if( !hasEvent ) {
            return null;
        }
        try {
            IMActionEvent event = eventQueue.take();
            hasEvent = !isFinalEvent(event);
            return event;
        } catch (InterruptedException e) {
            throw new IMException(IMException.IMErrorCode.WAIT_INTERUPPTED, e);
        }
    }

    @Override
    public IMActionEvent waitEvent(long timeoutMs) throws IMException {
        IMActionEvent event = null;
        if( !hasEvent ) {
            return null;
        }
        try {
            event = eventQueue.poll(timeoutMs, TimeUnit.MICROSECONDS);
        } catch (InterruptedException e) {
            throw new IMException(IMException.IMErrorCode.WAIT_INTERUPPTED, e);
        }
        if(event == null){
            throw new IMException(IMException.IMErrorCode.WAIT_TIMEOUT);
        }else{
            hasEvent = !isFinalEvent(event);
            return event;
        }
    }

    @Override
    public IMActionEvent waitFinalEvent() throws IMException {
        IMActionEvent event = null;
        while( (event = waitEvent()) != null){
            if( isFinalEvent(event) ){
                return event;
            }
        }
        throw new IMException(IMException.IMErrorCode.UNKNOWN_ERROR);
    }

    @Override
    public IMActionEvent waitFinalEvent(long timeoutMs)
            throws IMException {
        IMActionEvent event = null;
        long start = System.currentTimeMillis();
        while( (event = waitEvent(timeoutMs)) != null){
            long end = System.currentTimeMillis();
            if( isFinalEvent(event) ){
                return event;
            }else{
                timeoutMs -= end - start;
                start = System.currentTimeMillis();
            }
        }
        throw new IMException(IMException.IMErrorCode.UNKNOWN_ERROR);
    }

    private boolean isFinalEvent(IMActionEvent event){
        IMActionEvent.Type type = event.getType();
        return type== IMActionEvent.Type.EVT_CANCELED
                || type== IMActionEvent.Type.EVT_ERROR
                || type== IMActionEvent.Type.EVT_OK;
    }
}
