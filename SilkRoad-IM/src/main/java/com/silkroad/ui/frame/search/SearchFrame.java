package com.silkroad.ui.frame.search;

import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.text.WebTextField;
import com.silkroad.ui.bean.UIBuddy;
import com.silkroad.ui.bean.UIUser;
import com.silkroad.ui.event.UIActionHandler;
import com.silkroad.ui.event.UIEvent;
import com.silkroad.ui.event.UIEventHandler;
import com.silkroad.ui.event.UIEventType;
import com.silkroad.ui.frame.UIFrame;
import com.silkroad.ui.panel.search.SearchPane;
import com.silkroad.ui.service.SkinService;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;

/**
 * Created by caoyong on 2014/7/28.
 */
public class SearchFrame extends UIFrame {

    private SearchPane contentWrap;
    private boolean search = true;
    public SearchFrame() {
        this(true);
    }

    public SearchFrame(boolean search){
        this.search = search;
        contentWrap = new SearchPane(this);
        setUIContentPane(contentWrap);
        setTitle("添加好友");
        setPreferredSize(new Dimension(380, 200));
        pack();
        setLocationRelativeTo(null);
    }

    public SearchPane getContentWrap() {
        return contentWrap;
    }


    public boolean isSearch() {
        return search;
    }

    @Override
    public void installSkin(SkinService skinService) {
        setIconImage(getSkinService().getIconByKey("window/titleWIcon").getImage());
        contentWrap.installSkin(skinService);
        super.installSkin(skinService);
    }

    @UIActionHandler
    protected void searchUser(WebTextField txt_User){
        String userName = txt_User.getText();
        if(StringUtils.isEmpty(userName)){
            WebOptionPane.showMessageDialog(this, "请输入用户账号","提示", WebOptionPane.INFORMATION_MESSAGE);
        }else{
            eventService.broadcast(new UIEvent(UIEventType.USER_FIND_REQUEST, userName));
        }
    }

    @UIActionHandler
    protected void addBuddy(WebTextField txt_User, WebTextField txt_Name, WebComboBox cbx_group){
        UIBuddy uiBuddy = new UIBuddy();
        uiBuddy.setJid(txt_User.getText());
        uiBuddy.setUser(txt_Name.getText());
        uiBuddy.setNick(txt_Name.getText());
        uiBuddy.setCategoryName(cbx_group.getSelectedItem().toString());
        eventService.broadcast(new UIEvent(UIEventType.BUDDY_ADD_REQUEST, uiBuddy));
        this.dispose();
    }

    @UIActionHandler
    protected void addedBuddy(WebTextField txt_User, WebTextField txt_Name, WebComboBox cbx_group){
        UIBuddy uiBuddy = new UIBuddy();
        uiBuddy.setJid(txt_User.getText());
        uiBuddy.setUser(txt_Name.getText());
        uiBuddy.setNick(txt_Name.getText());
        uiBuddy.setCategoryName(cbx_group.getSelectedItem().toString());
        eventService.broadcast(new UIEvent(UIEventType.REQUEST_ADDED_BUDDY, uiBuddy));
        this.dispose();
    }

    @UIEventHandler(UIEventType.USER_FIND_SUCCESS)
    protected void searchSuccess(UIEvent uiEvent){
        UIUser uiUser = (UIUser) uiEvent.getTarget();
        String uName = uiUser.getNick().equals("") ? uiUser.getUser() : uiUser.getNick();
        contentWrap.setUIData(uiUser.getJid(), uName);
    }

    @UIEventHandler(UIEventType.USER_FIND_ERROR)
    protected void searchError(UIEvent uiEvent){
        WebOptionPane.showMessageDialog(this,"搜索失败:" + uiEvent.getTarget(), "错误", WebOptionPane.ERROR_MESSAGE);
    }

}
