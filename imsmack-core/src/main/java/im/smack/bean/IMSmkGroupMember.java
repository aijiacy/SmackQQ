package im.smack.bean;

/**
 * 群成员
 * 
 * @author ChenZhiHui
 * @create-time 2013-2-23
 */
public class IMSmkGroupMember extends IMSmkStranger {

	private static final long serialVersionUID = 786179576556539800L;

	private IMSmkGroup group;
	private String card;

	public IMSmkGroup getGroup() {
		return group;
	}

	public void setGroup(IMSmkGroup group) {
		this.group = group;
	}

	/**
	 * @return the card
	 */
	public String getCard() {
		return card;
	}

	/**
	 * @param card
	 *            the card to set
	 */
	public void setCard(String card) {
		this.card = card;
	}

}
