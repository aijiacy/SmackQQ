/**
 * 
 */
package com.silkroad.core.bean;

/**
 * 陌生人
 * @author ZhiHui_Chen<6208317@qq.com>
 * @create date 2013-4-11
 */
public class IMDiscuzMember extends IMStranger {
	private static final long serialVersionUID = -9123961549383958201L;
	
	private IMDiscuz discuz;

	public IMDiscuz getDiscuz() {
		return discuz;
	}

	public void setDiscuz(IMDiscuz discuz) {
		this.discuz = discuz;
	}
	
}
