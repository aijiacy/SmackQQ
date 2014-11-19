package im.smack.bean;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息
 * @author CaoYong
 *
 */
public class IMSmkUser implements Serializable{
	/**
	 * 序列化版本号
	 */
	private static final long serialVersionUID = 1L;
	
	private long uid; //标识
	private String nickName;//昵称
	private String signMark;//个性签名
	private String gender; // 性别
	private Date birthday; // 出生日期
	private String phone; // 电话
	private String mobile; // 手机
	private String email; // 邮箱
	private String country; // 国家
	private String province; // 省
	private String city; // 城市
	private transient BufferedImage face; // 头像,不能被序列化
	private IMSmkStatus status;
	private IMSmkAllow allow;
		
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getSignMark() {
		return signMark;
	}
	public void setSignMark(String signMark) {
		this.signMark = signMark;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public BufferedImage getFace() {
		return face;
	}
	public void setFace(BufferedImage face) {
		this.face = face;
	}
	public IMSmkStatus getStatus() {
		return status;
	}
	public void setStatus(IMSmkStatus status) {
		this.status = status;
	}
	public IMSmkAllow getAllow() {
		return allow;
	}
	public void setAllow(IMSmkAllow allow) {
		this.allow = allow;
	}
}
