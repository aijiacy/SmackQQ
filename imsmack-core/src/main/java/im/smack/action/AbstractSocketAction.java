package im.smack.action;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.AlreadyLoggedInException;
import org.jivesoftware.smack.SmackException.ConnectionException;
import org.jivesoftware.smack.SmackException.FeatureNotSupportedException;
import org.jivesoftware.smack.SmackException.IllegalStateChangeException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.SmackException.ResourceBindingNotOfferedException;
import org.jivesoftware.smack.SmackException.SecurityRequiredException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.StreamErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import im.smack.IMSmkActionListener;
import im.smack.IMSmkException;
import im.smack.IMSmkException.IMErrorCode;
import im.smack.actor.IMSmkActor;
import im.smack.core.IMSmkAction;
import im.smack.core.IMSmkConstants;
import im.smack.core.IMSmkContext;
import im.smack.core.IMSmkService;
import im.smack.core.IMSmkService.Type;
import im.smack.event.IMSmkActionEvent;
import im.smack.event.IMSmkActionFuture;
import im.smack.socket.IMSmkSocketRequest;
import im.smack.socket.IMSmkSocketResponse;

/**
 * 抽象的动作类，实现大部分共有方法
 * 
 * @author CaoYong
 * 
 */
public abstract class AbstractSocketAction implements IMSmkAction {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractSocketAction.class);
	private IMSmkContext context;
    private IMSmkActionListener listener;
	private IMSmkService.Type type;// 服务类型
	private Future<IMSmkSocketResponse> futureResponse;
	private IMSmkActionFuture futureAction;
	private int retryTimes;

	public AbstractSocketAction(IMSmkContext context, IMSmkActionListener listener, IMSmkService.Type type) {
		this.context = context;
		this.listener = listener;
		this.type = type;
		this.retryTimes = 0;
	}

	public IMSmkContext getContext() {
		return context;
	}

	@Override
	public IMSmkActionListener getActionListener() {
		return listener;
	}

	@Override
	public void setActionListener(IMSmkActionListener listener) {
		this.listener = listener;
	}

	public void setFutureResponse(Future<IMSmkSocketResponse> futureResponse) {
		this.futureResponse = futureResponse;
	}

	@Override
	public void setFutureAction(IMSmkActionFuture futureAction) {
		this.futureAction = futureAction;
	}
	
	@Override
	public Type getServiceType() {
		// TODO Auto-generated method stub
		return type;
	}

	@Override
	public IMSmkSocketRequest buildRequest() throws IMSmkException {
		LOG.debug("buildRequest 方法没有具体的实现类");
		return null;
	}

	@Override
	public void onSocketError(Throwable t) {
		if (!doRetryIt(getErrorCode(t), t)) {
			notifyActionEvent(IMSmkActionEvent.Type.EVT_ERROR, new IMSmkException(getErrorCode(t), t));
		}
	}

	@Override
	public void onSocketFinish(IMSmkSocketResponse response) {
		try {
			// 这里与http不同需要调试后处理 200 服务类如果未出现XMPPException 将回调设置为200;
			switch (response.getResponseCode()) {
			case IMSmkSocketResponse.S_OK:
				onStatusOk(response);
				break;
			case IMSmkSocketResponse.S_NOT_AUTHORIZED:
				onStatusOk(response);
				break;
			case IMSmkSocketResponse.S_SERVICE_PROCESS_FAILED:
				onStatusOk(response);
				break;
			case IMSmkSocketResponse.S_NOT_MODIFIED:
				onStatusOk(response);
				break;
			case IMSmkSocketResponse.S_FORBIDDEN:
				onStatusOk(response);
				break;
			default:
				onStatusError(response);
				break;
			}
		} catch (IMSmkException ex) {
			notifyActionEvent(IMSmkActionEvent.Type.EVT_ERROR, ex);
		} catch (Throwable ex) {
			notifyActionEvent(IMSmkActionEvent.Type.EVT_ERROR, new IMSmkException(getErrorCode(ex), ex));
		}
	}

	protected void onStatusError(IMSmkSocketResponse response) throws IMSmkException {
		if (!doRetryIt(IMErrorCode.ERROR_SOCKET_STATUS, null)) {
			throw new IMSmkException(IMErrorCode.ERROR_SOCKET_STATUS);
		}
	}

	protected void onStatusOk(IMSmkSocketResponse response) {
		notifyActionEvent(IMSmkActionEvent.Type.EVT_OK, response);
	}

	@Override
	public void cancelRequest() throws IMSmkException {
		futureResponse.cancel(true);
		notifyActionEvent(IMSmkActionEvent.Type.EVT_CANCELED, null);
	}

	@Override
	public void notifyActionEvent(IMSmkActionEvent.Type type, Object target) {
		if (null != listener) {
			listener.onActionEvent(new IMSmkActionEvent(type, target, futureAction));
		}
	}

	@Override
	public boolean isCancelable() {
		return false;
	}

	private boolean doRetryIt(IMErrorCode errorCode, Throwable t) {
		if (futureAction.isCanceled()) {
			return true;
		}
		++retryTimes;
		if (retryTimes < IMSmkConstants.MAX_RETRY_TIMES) {
			notifyActionEvent(IMSmkActionEvent.Type.EVT_RETRY, new IMSmkException(errorCode, t));
			try {
				// 等待几秒再重试
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				LOG.error("Sleep error...", e);
			}
			getContext().pushActor(new IMSmkActor(IMSmkActor.Type.BUILD_REQUEST, getContext(), this));
			return true;
		}

		return false;
	}

	private IMErrorCode getErrorCode(Throwable ex) {
		if (ex instanceof SocketTimeoutException || ex instanceof TimeoutException) {
			return IMErrorCode.IO_TIMEOUT;
		} else if (ex instanceof IOException) {
			return IMErrorCode.IO_ERROR;
		} else if (ex instanceof SmackException) {
			if (ex instanceof AlreadyLoggedInException) {
				return IMErrorCode.USER_IS_LOGGEDIN;
			} else if (ex instanceof ConnectionException) {
				return IMErrorCode.INVALID_CONNECTION;
			} else if (ex instanceof FeatureNotSupportedException) {
				return IMErrorCode.FEATURE_NOT_SUPPORTED;
			} else if (ex instanceof IllegalStateChangeException) {
				return IMErrorCode.INVALID_STATE_CHANGE;
			} else if (ex instanceof NoResponseException) {
				return IMErrorCode.INVALID_RESPONSE;
			} else if (ex instanceof NotConnectedException) {
				return IMErrorCode.NOT_CONNECTED;
			} else if (ex instanceof NotLoggedInException) {
				return IMErrorCode.USER_NOT_LOGGEDIN;
			} else if (ex instanceof ResourceBindingNotOfferedException) {
				return IMErrorCode.UNKNOWN_RES_BINDING;
			} else if (ex instanceof SecurityRequiredException) {
				return IMErrorCode.INVALID_LOGIN_AUTH;
			} else {
				return IMErrorCode.UNKNOWN_ERROR;
			}
		} else if (ex instanceof XMPPException) {
			if (ex instanceof StreamErrorException) {
				return IMErrorCode.IO_ERROR;
			} else {
				return IMErrorCode.UNKNOWN_ERROR;
			}
		} else {
			return IMErrorCode.UNKNOWN_ERROR;
		}

	}
}
