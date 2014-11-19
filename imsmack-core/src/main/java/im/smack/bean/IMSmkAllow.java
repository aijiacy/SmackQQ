package im.smack.bean;

/**
 * 对方设置的加好友策略
 * @author CaoYong
 */
public enum IMSmkAllow {
	/**允许所有人添加*/
	ALLOW_ALL, //0
	/**需要验证信息*/
	NEED_CONFIRM, //1
	/**拒绝任何人加好友*/
	REFUSE_ALL,	//2
	/**需要回答问题*/
	NEED_ANSWER, //3
	/**需要验证和回答问题*/
	NEED_ANSWER_AND_CONFIRM //4
}
