package com.silkroad.core.actor;


import com.silkroad.core.context.IMLifeCycle;

/**
 * 内部操作分发器接口
 * @author CaoYong
 *
 */
public interface IMActorDispatcher extends IMLifeCycle {
	/**
	 * 把一个event放入事件队列的末尾等待处理
	 * 线程安全的
	 * @param actor
	 */
	public void pushActor(IMActor actor);
}
