package im.smack.action;

import im.smack.IMSmkActionListener;
import im.smack.IMSmkException;
import im.smack.core.IMSmkAction;
import im.smack.core.IMSmkContext;
import im.smack.core.IMSmkService.Type;
import im.smack.event.IMSmkActionEvent;
import im.smack.socket.IMSmkSocketRequest;
import im.smack.socket.IMSmkSocketResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuddyAction extends AbstractSocketAction implements IMSmkAction {

	private static final Logger LOG = LoggerFactory.getLogger(BuddyAction.class);

	public BuddyAction(IMSmkContext context, IMSmkActionListener listener, Type type) {
		super(context, listener, type);
	}
	
	@Override
	public IMSmkSocketRequest buildRequest() throws IMSmkException {
		IMSmkSocketRequest req = new IMSmkSocketRequest();
		req.setType(IMSmkSocketRequest.Type.BUDDY_LIST);
		LOG.debug("请求获取好友分组");
		return req;
	}
	
	@Override
	protected void onStatusOk(IMSmkSocketResponse response) {
        if(response.getResponseCode() == IMSmkSocketResponse.S_OK) {
            notifyActionEvent(IMSmkActionEvent.Type.EVT_OK, getContext().getStore().getCategoryList());
        }else{
            notifyActionEvent(IMSmkActionEvent.Type.EVT_ERROR, new IMSmkException(IMSmkException.IMErrorCode.UNKNOWN_ERROR,"获取好友分组失败!"));
        }
	}
}
