/**
 * 
 */
package im.smack.bean;

/**
 * 陌生人
 * @author ZhiHui_Chen<6208317@qq.com>
 * @create date 2013-4-11
 */
public class IMSmkDiscuzMember extends IMSmkStranger {
	private static final long serialVersionUID = -9123961549383958201L;
	
	private IMSmkDiscuz discuz;

	public IMSmkDiscuz getDiscuz() {
		return discuz;
	}

	public void setDiscuz(IMSmkDiscuz discuz) {
		this.discuz = discuz;
	}
	
}
