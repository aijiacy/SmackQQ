package im.smack.core;

import java.util.concurrent.Future;

import im.smack.IMSmkActionListener;
import im.smack.IMSmkException;
import im.smack.event.IMSmkActionEvent;
import im.smack.event.IMSmkActionFuture;
import im.smack.socket.IMSmkSocketListener;
import im.smack.socket.IMSmkSocketRequest;
import im.smack.socket.IMSmkSocketResponse;

/**
 * 
 * @author CaoYong
 *
 */
public interface IMSmkAction extends IMSmkSocketListener{
	/**
	 * 创建Socket请求并完成请求
	 * @return
	 * @throws IMSmkException
	 */
	public IMSmkSocketRequest buildRequest() throws IMSmkException;
	/**
	 * 取消请求
	 * @throws IMSmkException
	 */
	public void cancelRequest() throws IMSmkException;
	/**
	 * 是否能够取消请求
	 * @return
	 */
	public boolean isCancelable();
	/**
	 * 向UI发送通知事件
	 * @param type
	 * @param target
	 */
	public void notifyActionEvent(IMSmkActionEvent.Type type, Object target);
	/**
	 * 获取动作监听器
	 * @return
	 */
	public IMSmkActionListener getActionListener();
	/**
	 * 设置动作监听器
	 * @param listener
	 */
	public void setActionListener(IMSmkActionListener listener);
	/**
	 * 存储响应信息
	 * @param future
	 */
	public void setFutureResponse(Future<IMSmkSocketResponse> future);
	/**
	 * 未知
	 * @param actionFuture
	 */
	public void setFutureAction(IMSmkActionFuture futureAction);
	/**
	 * 获取处理服务
	 * @return
	 */
	public IMSmkService.Type getServiceType();
}
