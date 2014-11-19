package im.smack.event;

import im.smack.IMSmkException;
import im.smack.core.IMSmkEvent;

public class IMSmkActionEvent extends IMSmkEvent {
	private Type type;
	private Object target;
	private IMSmkActionFuture actionFuture;

	public IMSmkActionEvent(Type type, Object target, IMSmkActionFuture actionFuture) {
		this.type = type;
		this.target = target;
		this.actionFuture = actionFuture;
	}

	public Type getType() {
		return type;
	}
	
	public Object getTarget() {
		return target;
	}
	
	public void cancelAction() throws IMSmkException{
		actionFuture.cancel();
	}
	
	public static enum Type {
		EVT_OK, EVT_ERROR, EVT_CANCELED, EVT_RETRY
	}

	@Override
	public String toString() {
		return "QQActionEvent [type=" + type + ", target=" + target + "]";
	}
}
