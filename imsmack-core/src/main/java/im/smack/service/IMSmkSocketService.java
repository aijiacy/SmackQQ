package im.smack.service;

import java.util.concurrent.Future;

import im.smack.IMSmkException;
import im.smack.core.IMSmkService;
import im.smack.socket.IMSmkSocketListener;
import im.smack.socket.IMSmkSocketRequest;
import im.smack.socket.IMSmkSocketResponse;


public interface IMSmkSocketService extends IMSmkService{
/**
	 * 执行一个HTTP请求		
	 * @param request			请求对象
	 * @param listener			请求回调
	 * @return	同步获取请求结果对象，可在这个对象上等待请求的完成
	 */
	public Future<IMSmkSocketResponse> executeHttpRequest(IMSmkSocketRequest request, IMSmkSocketListener listener) throws IMSmkException;
}
