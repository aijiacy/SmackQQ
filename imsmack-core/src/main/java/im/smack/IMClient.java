package im.smack;

import java.io.OutputStream;
import im.smack.bean.IMSmkMsg;
import im.smack.bean.IMSmkStatus;
import im.smack.event.IMSmkActionFuture;


/**
 * 客户端动作接口
 * @author CaoYong
 *
 */
public interface IMClient {
	public void destory();
	public IMSmkActionFuture login(String username, String password, IMSmkStatus status, final IMSmkActionListener listener);
	public IMSmkActionFuture relogin(IMSmkStatus status, final IMSmkActionListener listener);
	public IMSmkActionFuture loginout(final IMSmkActionListener listener);
	public IMSmkActionFuture changeStatus(IMSmkStatus status, final IMSmkActionListener listener);
	public IMSmkActionFuture getUserFace(IMSmkMsg msg, OutputStream picout, final IMSmkActionListener listener);
    public IMSmkActionFuture getBundyList(final IMSmkActionListener listener);
}
