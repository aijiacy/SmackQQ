package com.silkroad.core.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 一个注解可以简化NotifyEvent的监听，分发和处理
 * 只需在处理事件的方法上加入@QQNotifyHandler即可
 * 如
 * @QQNotifyHandler(QQNotifyEvent.Type.BUDDY_MSG)
 *
 * @author solosky <solosky772@qq.com>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IMNotifyHandler {
    IMNotifyEvent.Type value();
}
