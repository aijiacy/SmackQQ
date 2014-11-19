package im.smack.core;

import im.smack.IMSmkException;


/**
 * 
 * @author CaoYong
 *
 */
public interface IMSmkLifeCycle {
	/**
	 * 初始化，在使用之前调用
	 * @param context	初始化失败抛出
	 */
	public void init(IMSmkContext context) throws IMSmkException;
	/**
	 * 销毁，在使用完毕之后调用
	 * @throws IMSmkException
	 */
	public void destroy() throws IMSmkException;
}
