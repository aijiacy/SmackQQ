package im.smack.module;

import im.smack.IMSmkActionListener;
import im.smack.action.LoginAction;
import im.smack.core.IMSmkService;
import im.smack.event.IMSmkActionFuture;

public class LoginModule extends AbstractModule{
	public IMSmkActionFuture login(IMSmkActionListener listener){
		return this.pushAction(new LoginAction(getContext(),listener, IMSmkService.Type.SOCKET_LOGIN));
	}
}
