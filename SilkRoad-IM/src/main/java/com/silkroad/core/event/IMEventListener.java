package com.silkroad.core.event;

/**
 * IM事件监听器
 */
public interface IMEventListener {
    /**
     * 触发事件回调
     * @param imEvent
     */
    public void onIMEvent(IMEvent imEvent);
}
