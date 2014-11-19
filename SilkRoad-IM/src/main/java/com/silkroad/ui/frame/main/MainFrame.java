package com.silkroad.ui.frame.main;


import com.alee.laf.optionpane.WebOptionPane;
import com.silkroad.core.bean.base.IMEntity;
import com.silkroad.ui.bean.*;
import com.silkroad.ui.context.UIContext;
import com.silkroad.ui.event.UIActionHandler;
import com.silkroad.ui.event.UIEvent;
import com.silkroad.ui.event.UIEventHandler;
import com.silkroad.ui.event.UIEventType;
import com.silkroad.ui.frame.UIFrame;
import com.silkroad.ui.frame.chat.ChatFrame;
import com.silkroad.ui.frame.search.SearchFrame;
import com.silkroad.ui.manager.ChatManager;
import com.silkroad.ui.panel.chat.BasicPanel;
import com.silkroad.ui.panel.main.MainPane;
import com.silkroad.ui.service.SkinService;
import com.silkroad.ui.service.impl.CacheServiceImpl;

import java.awt.*;
import java.util.List;
import java.util.UUID;


/**
 * 主界面，分为上/中/下的内容面板
 *
 * Project  : iqq-projects
 * Author   : 承∮诺 < 6208317@qq.com >
 * Created  : 14-5-4
 * License  : Apache License 2.0
 */
public class MainFrame extends UIFrame {
    private MainPane contentPane;

    public MainFrame() {
        this(null);
    }

    public MainFrame(MainPane contentPane) {

        this.contentPane = contentPane;
        // 主面板，放所有显示内容
        if(contentPane == null) this.contentPane = new MainPane(this);

        initUI();
    }

    private void initUI() {
        setTitle(getI18nService().getMessage("app.name"));
        setUIContentPane(contentPane);
        setPreferredSize(new Dimension(300, 650));        // 首选大小
        pack();
        setLocationRelativeTo(null);                      // 居中
        setDefaultCloseOperation(UIFrame.EXIT_ON_CLOSE);
    }

    public void addCategory(String name, int sx, int sy){
        new CategoryFrame(name,"add", sx, sy).setVisible(true);
    }

    public void updCategory(String oldName, int sx, int sy){
        new CategoryFrame(oldName,"add", sx, sy).setVisible(true);
    }

    public void delCategory(String name, int sx, int sy){
        new CategoryFrame(name,"del", sx, sy).setVisible(true);
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
    }

    @UIActionHandler
    public void showSearchPane(){
        new SearchFrame().setVisible(true);
    }

    @UIEventHandler(UIEventType.SELF_FACE_UPDATE)
    public void processSelfFaceUpdate(UIEvent uiEvent){
        contentPane.getHeaderPanel().updateSelfFace((Image) uiEvent.getTarget());
    }

    @UIEventHandler(UIEventType.SELF_STATUS_UPDATE)
    public void processSelfStatusUpdate(UIEvent uiEvent){
        contentPane.getHeaderPanel().updateSelfStatus((UIStatus) uiEvent.getTarget());
    }

    @UIEventHandler(UIEventType.SELF_INFO_UPDATE)
    public void processSelfInfoUpdate(UIEvent uiEvent){
        UIUser user = (UIUser) uiEvent.getTarget();
        contentPane.getHeaderPanel().updateSelfNick(user.getNick());
    }

    @UIEventHandler(UIEventType.SELF_SIGN_UPDATE)
    public void processSelfSignUpdate(UIEvent uiEvent){
        UIUser user = (UIUser) uiEvent.getTarget();
        contentPane.getHeaderPanel().updateSelfSign(user.getSign());
    }

    @UIEventHandler(UIEventType.BUDDY_LIST_UPDATE)
    public void processBuddyUpdate(UIEvent uiEvent){
        List<UIBuddyCategory> uiCategories = (List<UIBuddyCategory>) uiEvent.getTarget();
        contentPane.getMiddlePanel().updateBuddyList(uiCategories);
    }

    @UIEventHandler(UIEventType.USER_FACE_UPDATE)
    public void processUserFaceUpdate(UIEvent uiEvent){
        UIUser UIUser = (UIUser) uiEvent.getTarget();
        contentPane.getMiddlePanel().updateUserFace(UIUser);
    }

    @UIEventHandler(UIEventType.RECV_CHATMSG_SUCCESS)
    private void recvChatMsg(UIEvent event){
        UIMsg uiMsg = (UIMsg) event.getTarget();
        ChatFrame chatFrame = ChatManager.getChatFrame();
        BasicPanel targetUserPanel = null;
        if(chatFrame != null) {
            for (int i = 0; i < chatFrame.getTabbedPane().getTabCount(); i++) {
                BasicPanel userPanel = (BasicPanel) chatFrame.getTabbedPane().getComponentAt(i);
                IMEntity entity = userPanel.getEntity();
                if (entity.getJid().equals(uiMsg.getSender().getJid())) {
                    targetUserPanel = userPanel;
                }
            }
        }
        if(targetUserPanel != null){
            uiMsg.setState(UIMsg.State.READ);
            uiMsg.setId(UUID.randomUUID().getLeastSignificantBits());
            targetUserPanel.showMsg(uiMsg);
        } else {
            // TODO : 增加消息队列用于消息提醒。类似QQ托盘闪烁
            String fromJid = uiMsg.getSender().getJid();
            java.util.List<UIBuddyCategory> uiBuddyCategories = UIContext.me().getBean(CacheServiceImpl.class).getUICategories();
            for (UIBuddyCategory uiBuddyCategory : uiBuddyCategories){
                for (UIBuddy uiBuddy : uiBuddyCategory.getBuddyList()){
                    if(uiBuddy.getJid().equals(fromJid)){
                        ChatManager.addChat(uiBuddy);
                        BasicPanel userPanel = (BasicPanel) ChatManager.getChatFrame().getTabbedPane().getSelectedComponent();
                        uiMsg.setState(UIMsg.State.READ);
                        uiMsg.setId(UUID.randomUUID().getLeastSignificantBits());
                        userPanel.showMsg(uiMsg);
                    }
                }
            }
        }
    }

    @UIEventHandler(UIEventType.REQUEST_ADD_BUDDY)
    public void processRequestAddBuddy(UIEvent uiEvent){
        UIBuddy uiBuddy = (UIBuddy) uiEvent.getTarget();
        int r = WebOptionPane.showConfirmDialog(this,uiBuddy.getUser() + "请求添加您为好友!", "请求消息", WebOptionPane.YES_OPTION, WebOptionPane.QUESTION_MESSAGE);
        if(r  == WebOptionPane.YES_OPTION){
            SearchFrame searchFrame = new SearchFrame(false);
            searchFrame.getContentWrap().setUIData(uiBuddy.getJid(), uiBuddy.getUser());
            searchFrame.setVisible(true);
        }
    }

    @UIEventHandler(UIEventType.BUDDY_ADD_SUCCESS)
    protected void addBuddySuccess(UIEvent uiEvent){
        WebOptionPane.showMessageDialog(this, "发送添加好友请求成功", "确认", WebOptionPane.INFORMATION_MESSAGE);
    }

    @UIEventHandler(UIEventType.BUDDY_ADD_ERROR)
    protected void addBuddyError(UIEvent uiEvent){
        WebOptionPane.showMessageDialog(this,"发送添加好友请求失败:" + uiEvent.getTarget(), "错误", WebOptionPane.ERROR_MESSAGE);
    }

}
