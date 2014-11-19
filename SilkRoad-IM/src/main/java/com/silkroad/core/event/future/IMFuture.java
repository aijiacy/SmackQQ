package com.silkroad.core.event.future;


import com.silkroad.core.event.IMActionEvent;
import com.silkroad.core.exception.IMException;

/**
 * Created by XJNT-CY on 2014/7/8.
 */
public interface IMFuture {
    /**
     * 判断这个操作是否可以取消
     *
     * @return
     */
    public abstract boolean isCancelable();

    /**
     * 判断操作是否取消
     *
     * @return
     */
    public abstract boolean isCanceled();

    /**
     * 尝试取消操作
     *
     * @throws com.silkroad.core.exception.IMException
     *             如果不能取消，抛出UNABLE_CANCEL异常
     */
    public abstract void cancel() throws IMException;

    /**
     * 等待事件的到来 Note:可能有最终会产生多个事件如EVT_READ, EVT_WRITE等，此时应该反复调用waitEvent来获得需要的事件
     *
     * @return
     * @throws com.silkroad.core.exception.IMException
     *             等待被中断抛出WAIT_INTERUPPTED
     */
    public abstract IMActionEvent waitEvent() throws IMException;

    /**
     * 给定一个超时时间，等待事件到来
     *
     * @param timeoutMs
     *            超时时间，毫秒为单位
     * @return
     * @throws com.silkroad.core.exception.IMException
     *             超时抛出 WAIT_TIMEOUT， 等待被中断抛出WAIT_INTERUPPTED
     */
    public abstract IMActionEvent waitEvent(long timeoutMs)
            throws IMException;

    /**
     * 等待最终的事件，通常是EVT_CANCELED,EVT_ERROR,EVT_OK
     *
     * @return
     * @throws com.silkroad.core.exception.IMException
     */
    public abstract IMActionEvent waitFinalEvent() throws IMException;

    /**
     * 给定一个超时时间，等待最终的事件
     *
     * @return
     * @throws com.silkroad.core.exception.IMException
     */
    public abstract IMActionEvent waitFinalEvent(long timeoutMs)
            throws IMException;
}
