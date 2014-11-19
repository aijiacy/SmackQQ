package com.silkroad.ui.panel.chat.msg;

import com.alee.extended.panel.GroupPanel;
import com.silkroad.ui.bean.UIMsg;
import com.silkroad.ui.bean.UIUser;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChenZhiHui <6208317@qq.com>
 * @create-time 2013-3-24
 */
public class MsgGroupPanel extends GroupPanel {
	private static final long serialVersionUID = -3757572461881253014L;

	public MsgGroupPanel() {
		super(false);
		setOpaque(true);
		setBackground(new Color(245, 245, 245));
	}
	
	public void updateMsg(UIMsg msg){
		for(Component comp: getComponents()) {
			if(comp instanceof MsgPane){
				MsgPane pane = (MsgPane) comp;
                UIMsg cur = pane.getMsg();
				long msgId = cur.getId();
				if(msgId == msg.getId()) {
					pane.invalidate();
					return;
				}
			}
		}
	}
	
	public void updateUser(UIUser user) {
		for(Component comp: getComponents()) {
			if(comp instanceof MsgPane){
				MsgPane pane = (MsgPane) comp;
				UIMsg cur = pane.getMsg();
                UIUser sender = cur.getSender();
				if( user.equals(sender)){
					pane.invalidate();
				}
			}
		}
	}
	
	public List<UIMsg> getMsgList(){
		List<UIMsg> list = new ArrayList<UIMsg>();
		for(Component comp: getComponents()){
			if(comp instanceof MsgPane){
				MsgPane pane = (MsgPane) comp;
				list.add(pane.getMsg());
			}
		}
		return list;
	}

}
