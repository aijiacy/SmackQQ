package im.smack.socket;

/**
 * 请求
 * @author CaoYong
 *
 */
public class IMSmkSocketRequest {
	private Type type;
	public enum Type{
		LOGIN, //登录
		LOGOUT, //登出
		CHANGE_STATUS,//改变状态
		USER_REG, 	//注册操作
		USER_DEL,    //删除操作
		USER_UPD,    //更新操作
		USER_SEARCH,  //搜索操作
		BUDDY_LIST //获取分组好友列表
	}
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
}
