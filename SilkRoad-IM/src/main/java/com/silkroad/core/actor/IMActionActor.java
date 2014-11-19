package com.silkroad.core.actor;


import com.silkroad.core.action.IMAction;
import com.silkroad.core.bean.args.IMSocketRequest;
import com.silkroad.core.bean.args.IMSocketResponse;
import com.silkroad.core.context.IMCoreStore;
import com.silkroad.core.event.IMActionEvent;
import com.silkroad.core.exception.IMException;

import java.util.concurrent.Future;

public class IMActionActor implements IMActor {
	private Type procType;
	private IMCoreStore context;
	private IMAction action;
	private IMSocketResponse response;
	private Throwable throwable;
    private IMSocketRequest request;

	public IMActionActor(Type type, IMCoreStore context, IMAction action, IMSocketRequest request) {
		this.procType = type;
		this.context = context;
		this.action = action;
        this.request = request;
	}

	public IMActionActor(Type type, IMCoreStore context, IMAction action, IMSocketRequest request,
                         IMSocketResponse response) {
		this.procType = type;
		this.context = context;
		this.action = action;
        this.request = request;
		this.response = response;
	}

	public IMActionActor(Type type, IMCoreStore context, IMAction action, IMSocketRequest request, Throwable throwable) {
		this.procType = type;
		this.context = context;
		this.action = action;
        this.request = request;
		this.throwable = throwable;
	}

	@Override
	public void execute() {
		try {
			switch (procType) {
			case EXECUTE_REQUEST:
				// 调用SmackAPI
                Future<IMSocketResponse> futureResponse = action.onExecute(request, action);
                action.setFutureResponse(futureResponse);
				break;
			case CANCEL_REQUEST:
				action.cancelAction();
				break;
			case ON_SOCKET_FAILED:
				action.onFailed(throwable);
				break;
			case ON_SOCKET_SUCCESS:
				action.onSuccess(response);
				break;
			}
		} catch (IMException ex) {
			action.notifyActionEvent(IMActionEvent.Type.EVT_ERROR, ex);
		}
	}

	public static enum Type {
		EXECUTE_REQUEST, CANCEL_REQUEST, ON_SOCKET_FAILED, ON_SOCKET_SUCCESS
	}
}
