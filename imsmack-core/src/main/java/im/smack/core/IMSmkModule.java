package im.smack.core;

/**
 * 模块是功能单元水平的划分，一个模块负责某一个单独的相对独立的逻辑，如好友管理，分组管理，消息管理等
 * @author CaoYong
 *
 */
public interface IMSmkModule extends IMSmkLifeCycle {
	public enum Type{
		PROC,			//登陆和退出流程执行
		REG,			    //注册模块
		LOGIN,			//核心模块，处理登录和退出的逻辑
		USER,			//个人信息管理模块
		BUDDY,			//好友管理模块
		GROUP,			//群管理模块
		DISCUZ,			//讨论组模块
		CHAT,			//聊天模块
		EMAIL			//邮件模块
	}
}
