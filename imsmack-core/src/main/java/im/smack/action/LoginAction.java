package im.smack.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import im.smack.IMSmkActionListener;
import im.smack.IMSmkException;
import im.smack.IMSmkException.IMErrorCode;
import im.smack.bean.IMSmkAccount;
import im.smack.core.IMSmkAction;
import im.smack.core.IMSmkContext;
import im.smack.core.IMSmkService.Type;
import im.smack.event.IMSmkActionEvent;
import im.smack.socket.IMSmkSocketRequest;
import im.smack.socket.IMSmkSocketResponse;

public class LoginAction extends AbstractSocketAction implements IMSmkAction {

	private static final Logger LOG = LoggerFactory.getLogger(LoginAction.class);

	public LoginAction(IMSmkContext context, IMSmkActionListener listener, Type type) {
		super(context, listener, type);
	}

	@Override
	public IMSmkSocketRequest buildRequest() throws IMSmkException {
		IMSmkSocketRequest req = new IMSmkSocketRequest();
		req.setType(IMSmkSocketRequest.Type.LOGIN);
		LOG.debug("登陆请求");
		return req;
	}

	@Override
	protected void onStatusOk(IMSmkSocketResponse response) {
		IMSmkAccount account = getContext().getAccount();
		switch (response.getResponseCode()) {
		case IMSmkSocketResponse.S_OK:
			notifyActionEvent(IMSmkActionEvent.Type.EVT_OK, account);
			break;
		case IMSmkSocketResponse.S_NOT_AUTHORIZED:
			notifyActionEvent(IMSmkActionEvent.Type.EVT_ERROR, new IMSmkException(IMErrorCode.INVALID_LOGIN_AUTH, "登陆不是有效的!"));
			break;
		case IMSmkSocketResponse.S_SERVICE_PROCESS_FAILED:
			notifyActionEvent(IMSmkActionEvent.Type.EVT_ERROR, new IMSmkException(IMErrorCode.ERROR_SOCKET_STATUS, "错误的连接状态!"));
			break;
		case IMSmkSocketResponse.S_NOT_MODIFIED:
			notifyActionEvent(IMSmkActionEvent.Type.EVT_ERROR, new IMSmkException(IMErrorCode.ERROR_SOCKET_STATUS, "错误的连接状态!"));
			break;
		case IMSmkSocketResponse.S_FORBIDDEN:
			notifyActionEvent(IMSmkActionEvent.Type.EVT_ERROR, new IMSmkException(IMErrorCode.ERROR_SOCKET_STATUS, "错误的连接状态!"));
			break;
		default:
			notifyActionEvent(IMSmkActionEvent.Type.EVT_ERROR, new IMSmkException(IMErrorCode.UNKNOWN_ERROR, "发生未知错误!"));
			break;
		}
	}
}
