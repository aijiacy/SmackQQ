package com.silkroad.core.bean;

/**
 * 群成员
 * 
 * @author ChenZhiHui
 * @create-time 2013-2-23
 */
public class IMGroupMember extends IMStranger {

	private static final long serialVersionUID = 786179576556539800L;

	private IMGroup group;
	private String card;

	public IMGroup getGroup() {
		return group;
	}

	public void setGroup(IMGroup group) {
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
