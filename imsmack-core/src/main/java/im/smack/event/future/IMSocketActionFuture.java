package im.smack.event.future;

import im.smack.IMSmkException;
import im.smack.core.IMSmkAction;

public class IMSocketActionFuture extends AbstractActionFuture{
	private IMSmkAction action;
	public IMSocketActionFuture(IMSmkAction action) {
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
	public void cancel() throws IMSmkException {
		this.action.cancelRequest();
	}

}
