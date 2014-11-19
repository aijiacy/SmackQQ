package im.smack.module;

import im.smack.IMSmkException;
import im.smack.actor.IMSmkActor;
import im.smack.core.IMSmkAction;
import im.smack.core.IMSmkContext;
import im.smack.core.IMSmkModule;
import im.smack.event.IMSmkActionFuture;
import im.smack.event.future.IMSocketActionFuture;

public class AbstractModule implements IMSmkModule{
	private IMSmkContext context;

	@Override
	public void init(IMSmkContext context) throws IMSmkException {
		this.context = context;
	}

	@Override
	public void destroy() throws IMSmkException {
		// TODO Auto-generated method stub
	}
	
	protected IMSmkContext getContext(){
		return this.context;
	}
	
	protected IMSmkActionFuture pushAction(IMSmkAction action){
		IMSmkActionFuture actionFuture = new IMSocketActionFuture(action);	 	//替换掉原始的ActionListener
		getContext().pushActor(new IMSmkActor(IMSmkActor.Type.BUILD_REQUEST, getContext(), action));
		return actionFuture;
	}
}
