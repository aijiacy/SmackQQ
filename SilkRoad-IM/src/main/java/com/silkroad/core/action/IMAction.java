package com.silkroad.core.action;


import com.silkroad.core.bean.args.IMSocketRequest;
import com.silkroad.core.bean.args.IMSocketResponse;
import com.silkroad.core.context.IMCoreStore;
import com.silkroad.core.event.IMActionEvent;
import com.silkroad.core.event.IMActionListener;
import com.silkroad.core.event.IMSocketListener;
import com.silkroad.core.event.future.IMFuture;
import com.silkroad.core.exception.IMException;

import java.util.concurrent.Future;

/**
 * Created by XJNT-CY on 2014/7/8.
 */
public interface IMAction extends IMSocketListener {
    /**
     * 取消请求
     * @throws com.silkroad.core.exception.IMException
     */
    public void cancelAction() throws IMException;
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
    public void notifyActionEvent(IMActionEvent.Type type, Object target);
    /**
     * 获取动作监听器
     * @return
     */
    public IMActionListener getActionListener();
    /**
     * 设置动作监听器
     * @param listener
     */
    public void setActionListener(IMActionListener listener);
    /**
     * 存储响应信息
     * @param future
     */
    public void setFutureResponse(Future<IMSocketResponse> future);
    /**
     * 未知
     * @param futureAction
     */
    public void setFutureAction(IMFuture futureAction);
    /**
     * 获取交互会话
     * @return
     */
    public IMCoreStore getCoreStore();
    /**
     * 设置交互会话
     * @param coreStore
     */
    public void setCoreStore(IMCoreStore coreStore);

    public Future<IMSocketResponse> onExecute(IMSocketRequest request, IMSocketListener listener);
}
