package com.silkroad.ui.frame.chat;


import com.alee.utils.ImageUtils;
import com.silkroad.core.bean.base.IMEntity;
import com.silkroad.ui.bean.UIBuddy;
import com.silkroad.ui.bean.UIBuddyCategory;
import com.silkroad.ui.bean.UIMsg;
import com.silkroad.ui.bean.UIRoom;
import com.silkroad.ui.context.UIContext;
import com.silkroad.ui.event.UIActionHandler;
import com.silkroad.ui.event.UIEvent;
import com.silkroad.ui.event.UIEventHandler;
import com.silkroad.ui.event.UIEventType;
import com.silkroad.ui.frame.UIFrame;
import com.silkroad.ui.manager.ChatManager;
import com.silkroad.ui.panel.chat.BasicPanel;
import com.silkroad.ui.panel.chat.ChatPane;
import com.silkroad.ui.panel.chat.RoomPanel;
import com.silkroad.ui.panel.chat.UserPanel;
import com.silkroad.ui.service.SkinService;
import com.silkroad.ui.service.impl.CacheServiceImpl;
import org.sexydock.tabs.ITabCloseButtonListener;
import org.sexydock.tabs.jhrome.JhromeTabbedPaneUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

/**
 * Project  : silk road im
 * Author   : cao.yong
 * Created  : 14-5-10
 * License  : Apache License 2.0
 */
public class ChatFrame extends UIFrame {
    private static final Logger LOG = LoggerFactory.getLogger(ChatFrame.class);

    private ChatPane contentPane;
    private JTabbedPane tabbedPane;

    public ChatFrame() {
        initUI();
        initTabListener();
    }

    private void initUI() {
        contentPane = new ChatPane(this);
        tabbedPane = contentPane.getTabbedPane();
        setUIContentPane(contentPane);
        setTitle("");
        setPreferredSize(new Dimension(660, 580));        // 首选大小
        pack();
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    /**
     * 安装皮肤
     *
     * @param skinService
     */
    @Override
    public void installSkin(SkinService skinService) {
        super.installSkin(skinService);
        this.contentPane.installSkin(skinService);
        setIconImage(skinService.getIconByKey("window/titleWIcon").getImage());

        // 更新每个tab中panel的皮肤
        for(int i=0; i<tabbedPane.getTabCount(); i++) {
            BasicPanel entityPanel = (BasicPanel) tabbedPane.getComponentAt(i);
            entityPanel.installSkin(skinService);
        }
    }


    private void initTabListener() {
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(isVisible()) {
                    if( tabbedPane.getTabCount( ) == 0)
                    {
                        // 如是没有了，直接关闭窗口
                        dispose();
                    } else {
                        BasicPanel entityPanel = (BasicPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
                        entityPanel.installSkin(getSkinService());
                        String title = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
                        setTitle(getI18nService().getMessage("conversationTitle", title));
                    }
                }
            }
        });
        tabbedPane.putClientProperty(JhromeTabbedPaneUI.TAB_CLOSE_BUTTON_LISTENER
                , new ITabCloseButtonListener() {

            @Override
            public void tabCloseButtonPressed(JTabbedPane tabbedPane, int tabIndex) {
                // 关闭了一个tab，相当于关闭了一个对话
                BasicPanel entityPanel = (BasicPanel) tabbedPane.getComponentAt(tabIndex);

                tabbedPane.removeTabAt(tabIndex);
                ChatManager.removeChat(entityPanel.getEntity());
            }
        });
    }

    public void addBuddyPane(UIBuddy buddy, UserPanel entityPanel) {
        /**改变头像大小*/
        ImageIcon scaleIcon = new ImageIcon(ImageUtils.createPreviewImage(buddy.getAvatar(), 16,16));
        tabbedPane.addTab(buddy.getNick(), scaleIcon, entityPanel);
        tabbedPane.setSelectedComponent(entityPanel);
        setTitle(getI18nService().getMessage("conversationTitle", buddy.getNick()));
        /** add chat listener **/
    }

    public void addRoomPane(UIRoom room, RoomPanel entityPanel) {
        ImageIcon avatar =new ImageIcon(room.getAvatar());
        tabbedPane.addTab(room.getNick(), avatar, entityPanel);
        tabbedPane.setSelectedComponent(entityPanel);
        setTitle(getI18nService().getMessage("conversationTitle", room.getNick()));
    }

    public void setSelectedChat(BasicPanel entityPanel) {
        tabbedPane.setSelectedComponent(entityPanel);
    }

    public void updBuddyIcon(UIBuddy uiBuddy){
        for (int i = 0; i < tabbedPane.getTabCount(); i++){
            BasicPanel userPanel = (BasicPanel) tabbedPane.getComponentAt(i);
            IMEntity entity = userPanel.getEntity();
            if(uiBuddy.getJid().equals(entity.getJid())){
                ImageIcon scaleIcon = new ImageIcon(ImageUtils.createPreviewImage(uiBuddy.getAvatar(), 16,16));
                tabbedPane.setIconAt(i, scaleIcon);
                entity.setAvatar(uiBuddy.getAvatar());
                userPanel.setEntity(entity);
                userPanel.update();
                break;
            }
        }
    }

    @Override
    public void hide() {
        // 隐藏时清除当然所有对话
        clearChats();
        super.hide();
    }

    private void clearChats() {
        // 在管理器中清除当然所有对话
        ChatManager.clearChats();

        int count = tabbedPane.getTabCount();
        for (int i = 0; i < count; i++) {
            tabbedPane.removeTabAt(0);
        }
    }

    @UIEventHandler(UIEventType.SYNC_CHATMSG_STATE)
    private void SyncState(UIEvent event){
        UIBuddy uiBuddy = (UIBuddy) event.getTarget();
        this.updBuddyIcon(uiBuddy);
    }

    @UIEventHandler(UIEventType.SEND_CHATMSG_SUCCESS)
    private void sendChatMsgSuccess(UIEvent event){
        BasicPanel userPanel = (BasicPanel) tabbedPane.getSelectedComponent();
        UIMsg msg = (UIMsg) event.getTarget();
        userPanel.getMsgGroupPanel().updateMsg(msg);
    }

    @UIEventHandler(UIEventType.SEND_CHATMSG_ERROR)
    private void sendChatMsgError(UIEvent event){
        BasicPanel userPanel = (BasicPanel) tabbedPane.getSelectedComponent();
        UIMsg msg = (UIMsg) event.getTarget();
        userPanel.getMsgGroupPanel().updateMsg(msg);
    }
}
