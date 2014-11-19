package im.smack.actor;

import im.smack.core.IMSmkLifeCycle;

/**
 * 内部操作分发器接口
 * @author CaoYong
 *
 */
public interface IMSmkActorDispatcher extends IMSmkLifeCycle {
	/**
	 * 把一个event放入事件队列的末尾等待处理
	 * 线程安全的
	 * @param event
	 */
	public void pushActor(IMActor actor);
}
