package im.smack.actor;

import java.util.concurrent.Future;

import im.smack.IMSmkException;
import im.smack.core.IMSmkAction;
import im.smack.core.IMSmkContext;
import im.smack.event.IMSmkActionEvent;
import im.smack.service.IMSmkSocketService;
import im.smack.socket.IMSmkSocketListener;
import im.smack.socket.IMSmkSocketRequest;
import im.smack.socket.IMSmkSocketResponse;

public class IMSmkActor implements IMActor {
	private Type procType;
	private IMSmkContext context;
	private IMSmkAction action;
	private IMSmkSocketResponse response;
	private Throwable throwable;

	public IMSmkActor(Type type, IMSmkContext context, IMSmkAction action) {
		this.procType = type;
		this.context = context;
		this.action = action;
	}

	public IMSmkActor(Type type, IMSmkContext context, IMSmkAction action,
			IMSmkSocketResponse response) {
		this.procType = type;
		this.context = context;
		this.action = action;
		this.response = response;
	}

	public IMSmkActor(Type type, IMSmkContext context, IMSmkAction action, Throwable throwable) {
		this.procType = type;
		this.context = context;
		this.action = action;
		this.throwable = throwable;
	}

	@Override
	public void execute() {
		try {
			switch (procType) {
			case BUILD_REQUEST:
				// 调用SmackAPI
				IMSmkSocketService service = (IMSmkSocketService) this.context.getSerivce(action.getServiceType());
				IMSmkSocketRequest request = action.buildRequest();
				Future<IMSmkSocketResponse> future = service.executeHttpRequest(request, new IMSmkSocketAdapter(context, action));
				action.setFutureResponse(future);
				break;
			case CANCEL_REQUEST:
				action.cancelRequest();
				break;
			case ON_SOCKET_ERROR:
				action.onSocketError(throwable);
				break;
			case ON_SOCKET_FINISH:
				action.onSocketFinish(response);
				break;
			}
		} catch (IMSmkException ex) {
			action.notifyActionEvent(IMSmkActionEvent.Type.EVT_ERROR, ex);
		}
	}

	public static enum Type {
		BUILD_REQUEST, CANCEL_REQUEST, ON_SOCKET_ERROR, ON_SOCKET_FINISH
	}

	public static class IMSmkSocketAdapter implements IMSmkSocketListener {
		private IMSmkContext context;
		private IMSmkAction action;

		public IMSmkSocketAdapter(IMSmkContext context,	IMSmkAction action) {
			this.context = context;
			this.action = action;
		}

		@Override
		public void onSocketError(Throwable throwable) {
			this.context.pushActor(new IMSmkActor(Type.ON_SOCKET_ERROR, context, action, throwable));
		}

		@Override
		public void onSocketFinish(IMSmkSocketResponse response) {
			this.context.pushActor(new IMSmkActor(Type.ON_SOCKET_FINISH, context, action, response));
		}

	}
}
