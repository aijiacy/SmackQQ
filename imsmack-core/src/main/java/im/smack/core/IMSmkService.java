package im.smack.core;

public interface IMSmkService extends IMSmkLifeCycle {
	public enum Type{
		TIMER,		//定时服务
		SOCKET_LOGIN,		//SOCKET
		SOCKET_USER,		//用户操作
		SOCKET_BUDDY,		//好友操作
		SOCKET_CATALOG,		//分组操作
		SOCKET_GROUP,		//群组操作
		SOCKET_DISCUZ,		//讨论组操作
		SOCKET_CHAT,		//聊天操作
		TASK,		//异步任务，可以执行比较耗时的操作
	}
}
