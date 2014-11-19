package im.smack.event.future;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import im.smack.IMSmkActionListener;
import im.smack.IMSmkException;
import im.smack.IMSmkException.IMErrorCode;
import im.smack.event.IMSmkActionEvent;
import im.smack.event.IMSmkActionEvent.Type;
import im.smack.event.IMSmkActionFuture;

public abstract class AbstractActionFuture implements IMSmkActionFuture,
		IMSmkActionListener {
	private IMSmkActionListener proxyListener;
	private BlockingQueue<IMSmkActionEvent> eventQueue;
	private volatile boolean isCanceled;
	private volatile boolean hasEvent;

	public AbstractActionFuture(IMSmkActionListener proxyListener) {
		this.hasEvent = true;
		this.proxyListener = proxyListener;
		this.eventQueue = new LinkedBlockingQueue<IMSmkActionEvent>();
	}

	@Override
	public void onActionEvent(IMSmkActionEvent event) {
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
	public IMSmkActionEvent waitEvent() throws IMSmkException {
		if( !hasEvent ) {
			return null;
		}
		try {
			IMSmkActionEvent event = eventQueue.take();
			hasEvent = !isFinalEvent(event);
			return event;
		} catch (InterruptedException e) {
			throw new IMSmkException(IMErrorCode.WAIT_INTERUPPTED, e);
		}
	}

	@Override
	public IMSmkActionEvent waitEvent(long timeoutMs) throws IMSmkException {
		IMSmkActionEvent event = null;
		if( !hasEvent ) {
			return null;
		}
		try {
			event = eventQueue.poll(timeoutMs, TimeUnit.MICROSECONDS);
		} catch (InterruptedException e) {
			throw new IMSmkException(IMErrorCode.WAIT_INTERUPPTED, e);
		}
		if(event == null){
			throw new IMSmkException(IMErrorCode.WAIT_TIMEOUT);
		}else{
			hasEvent = !isFinalEvent(event);
			return event;
		}
	}

	@Override
	public IMSmkActionEvent waitFinalEvent() throws IMSmkException {
		IMSmkActionEvent event = null;
		while( (event = waitEvent()) != null){
			if( isFinalEvent(event) ){
				return event;
			}
		}
		throw new IMSmkException(IMErrorCode.UNKNOWN_ERROR);
	}

	@Override
	public IMSmkActionEvent waitFinalEvent(long timeoutMs)
			throws IMSmkException {
		IMSmkActionEvent event = null;
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
		throw new IMSmkException(IMErrorCode.UNKNOWN_ERROR);
	}

	private boolean isFinalEvent(IMSmkActionEvent event){
		IMSmkActionEvent.Type type = event.getType();
		return type==Type.EVT_CANCELED 
				|| type==Type.EVT_ERROR 
				|| type==Type.EVT_OK;
	}
}
