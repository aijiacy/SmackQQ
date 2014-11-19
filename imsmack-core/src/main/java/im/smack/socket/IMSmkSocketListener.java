package im.smack.socket;

/**
 * 响应
 * @author CaoYong
 *
 */
public interface IMSmkSocketListener {
	/**
	 * Socket 连接请求错误处理
	 * @param t
	 */
	public void onSocketError(Throwable t);
	/**
	 * Socket 连接请求完成处理
	 * @param response
	 */
	public void onSocketFinish(IMSmkSocketResponse response);
}
