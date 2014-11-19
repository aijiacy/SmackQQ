package im.smack.bean;

/**
 * 登录账号信息
 * @author CaoYong
 */
public class IMSmkAccount extends IMSmkUser {
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private String loginServer;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLoginServer() {
		return loginServer;
	}
	public void setLoginServer(String loginServer) {
		this.loginServer = loginServer;
	}
}
