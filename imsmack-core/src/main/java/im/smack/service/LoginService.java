package im.smack.service;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import im.smack.IMSmkException;
import im.smack.bean.IMSmkAccount;
import im.smack.socket.IMSmkSocketListener;
import im.smack.socket.IMSmkSocketRequest;
import im.smack.socket.IMSmkSocketResponse;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.packet.XMPPError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通过代理进行服务器连接
 * 
 * @author CaoYong
 * 
 */
public class LoginService extends AbstractService implements IMSmkSocketService {
	private static final Logger LOG = LoggerFactory.getLogger(LoginService.class);
	private IMSmkAccount account;
	private ExecutorService exec;

	@SuppressWarnings("unchecked")
	@Override
	public Future<IMSmkSocketResponse> executeHttpRequest(IMSmkSocketRequest request, final IMSmkSocketListener listener)
			throws IMSmkException {
		// TODO 这里实现Smack的连接请求
		LOG.debug("执行服务器登录请求" + request.getType());
		exec = Executors.newSingleThreadExecutor();
		Future<IMSmkSocketResponse> futureResponse = null;
		switch (request.getType()) {
		case LOGIN: {
			futureResponse = (Future<IMSmkSocketResponse>) exec.submit(new Runnable() {
				@Override
				public void run() {

					IMSmkSocketResponse response = new IMSmkSocketResponse(IMSmkSocketResponse.S_OK, "处理成功！");
					account = getContext().getAccount();
					try {
						getContext().getSession().getConnection().login(account.getUsername(), account.getPassword());
						listener.onSocketFinish(response);
					} catch (XMPPException e) {
						if (e instanceof XMPPErrorException) {
							XMPPError xmppError = ((XMPPErrorException) e).getXMPPError();
							switch (xmppError.getType()) {
							case AUTH:
								response = new IMSmkSocketResponse(IMSmkSocketResponse.S_NOT_AUTHORIZED, "失效的用户证书!");
								listener.onSocketFinish(response);
								break;
							case CONTINUE:
								response = new IMSmkSocketResponse(IMSmkSocketResponse.S_SERVICE_PROCESS_FAILED, "请求处理失败!");
								listener.onSocketFinish(response);
								break;
							case MODIFY:
								response = new IMSmkSocketResponse(IMSmkSocketResponse.S_NOT_MODIFIED, "请求无效！");
								listener.onSocketFinish(response);
								break;
							case CANCEL:
								response = new IMSmkSocketResponse(IMSmkSocketResponse.S_FORBIDDEN, "发现不可恢复的错误!");
								listener.onSocketFinish(response);
								break;
							default:
								break;
							}
						}
						listener.onSocketError(e);
					} catch (SmackException e) {
						listener.onSocketError(e);
					} catch (IOException e) {
						listener.onSocketError(e);
					} catch (Exception e) {
						listener.onSocketError(e);
					}
				}
			});
		}
			break;
		case LOGOUT: {
		}
			break;
		case CHANGE_STATUS: {

		}
			break;
		default:
			break;
		}
		return futureResponse;
	}
}
