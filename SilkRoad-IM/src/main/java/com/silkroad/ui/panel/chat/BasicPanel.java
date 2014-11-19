package com.silkroad.ui.panel.chat;

import com.alee.extended.image.WebDecoratedImage;
import com.alee.extended.painter.ColorPainter;
import com.alee.extended.panel.CenterPanel;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.toolbar.ToolbarStyle;
import com.alee.laf.toolbar.WebToolBar;
import com.silkroad.core.bean.base.IMEntity;
import com.silkroad.ui.bean.UIMsg;
import com.silkroad.ui.bean.UIUser;
import com.silkroad.ui.context.UIContext;
import com.silkroad.ui.event.UIEvent;
import com.silkroad.ui.event.UIEventType;
import com.silkroad.ui.panel.UIPanel;
import com.silkroad.ui.panel.chat.msg.MsgGroupPanel;
import com.silkroad.ui.panel.chat.msg.MsgPane;
import com.silkroad.ui.panel.chat.rich.RichTextPane;
import com.silkroad.ui.panel.chat.rich.UIRichItem;
import com.silkroad.ui.service.SkinService;
import com.silkroad.ui.service.impl.CacheServiceImpl;
import com.silkroad.ui.service.impl.EventServiceImpl;
import com.silkroad.util.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Project  : iqq-projects
 * Author   : 承∮诺 < 6208317@qq.com >
 * Created  : 14-5-11
 * License  : Apache License 2.0
 */
public abstract class BasicPanel extends UIPanel {
    /**
     * 面板聊天对象
     */
    protected IMEntity entity;

    /**
     * 输入面板
     */
    protected RichTextPane contentInput = new RichTextPane();
    /**
     * 输入面板工具条
     */
    protected WebToolBar inputToolbar = new WebToolBar();


    /**
     * 昵称
     */
    protected WebLabel nickLabel = new WebLabel();
    /**
     * 签名
     */
    protected WebLabel signLabel = new WebLabel();

    /**
     * 头像
     */
    protected WebDecoratedImage avatarImage = new WebDecoratedImage();

    protected WebButton textBtn = new WebButton();
    protected WebButton emoticonBtn = new WebButton();
    protected WebButton screenCaptureBtn = new WebButton();
    protected WebButton picturesBtn = new WebButton();
    protected WebButton historyBtn = new WebButton();
    /** send button **/
    protected WebButton sendBtn = new WebButton();

    protected UIPanel headerPanel = new UIPanel();
    protected UIPanel inputPanel = new UIPanel();
    protected MsgGroupPanel msgGroupPanel = new MsgGroupPanel();
    protected boolean isAppendMsg = false;

    public BasicPanel(IMEntity entity) {
        this.entity = entity;

        createHeader();
        createContent();
        createInput();

        update();
    }

    public IMEntity getEntity() {
        return entity;
    }

    public void setEntity(IMEntity entity) {
        this.entity = entity;
    }

    private void createHeader() {
        avatarImage.setShadeWidth(1);
        avatarImage.setRound(4);
        avatarImage.setDrawGlassLayer(false);

        GroupPanel textGroup = new GroupPanel(0, false, nickLabel, signLabel);
        textGroup.setMargin(0, 10, 0, 5);

        headerPanel.add(avatarImage, BorderLayout.WEST);
        headerPanel.add(new CenterPanel(textGroup, false , true), BorderLayout.CENTER);
        headerPanel.setMargin(8);
        add(headerPanel, BorderLayout.NORTH);
    }

    protected void createContent() {
        msgGroupPanel.setOpaque(false);
        WebScrollPane msgScroll = new WebScrollPane(msgGroupPanel) {
            {
                setHorizontalScrollBarPolicy(WebScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                setBorder(null);
                setMargin(0);
                setShadeWidth(0);
                setRound(0);
                setDrawBorder(false);
                setOpaque(false);
            }
        };

        // 获取JScrollPane中的纵向JScrollBar
        JScrollBar bar = msgScroll.getVerticalScrollBar();
        bar.setUnitIncrement(30);
        bar.addAdjustmentListener(new AdjustmentListener() {

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (isAppendMsg) {
                    e.getAdjustable().setValue(e.getAdjustable().getMaximum());
                    isAppendMsg = false;
                }
            }
        });
        add(msgScroll, BorderLayout.CENTER);
    }

    protected void createInput() {
        initInputToolbar(inputToolbar);
        initInputToolbarListener();
        contentInput.setPreferredSize(new Dimension(-1, 80));
        WebScrollPane textPaneScroll = new WebScrollPane(contentInput) {
            private static final long serialVersionUID = 1L;

            {
                setOpaque(false);
                setHorizontalScrollBarPolicy(WebScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                setBorder(null);
                setMargin(5, 5, 5, 5);
                setDrawBorder(false);
            }
        };
        inputPanel.add(inputToolbar, BorderLayout.NORTH);
        inputPanel.add(textPaneScroll, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
    }

    protected void initInputToolbar(WebToolBar inputToolbar) {
        setInputButtonStyle(textBtn);
        setInputButtonStyle(emoticonBtn);
        setInputButtonStyle(screenCaptureBtn);
        setInputButtonStyle(picturesBtn);
        setInputButtonStyle(historyBtn);
        setInputButtonStyle(sendBtn);

        // add to Toolbar
        inputToolbar.addSpacing(10);
        // toolbar.add(text);
        inputToolbar.add(emoticonBtn);
        inputToolbar.add(screenCaptureBtn);
        inputToolbar.add(picturesBtn);
        inputToolbar.add(historyBtn);

        inputToolbar.addToEnd(sendBtn);
        inputToolbar.addSpacingToEnd(30);

        // 输入小工具栏
        inputToolbar.setFloatable(false);
        inputToolbar.setToolbarStyle(ToolbarStyle.attached);
        inputToolbar.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    }

    private void initInputToolbarListener() {
        screenCaptureBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO screenCaptureBtn;
            }
        });
        sendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMsg(contentInput.getRichItems());
                contentInput.setText("");
            }
        });
    }

    private void sendMsg(List<UIRichItem> richItems) {
        if(null != richItems && richItems.size() > 0){
            String currLang = UIContext.me().getBean(CacheServiceImpl.class).getLanguage();
            UIMsg msg = new UIMsg();
            msg.setId(UUID.randomUUID().getLeastSignificantBits());
            UIUser sender = UIContext.me().getBean(CacheServiceImpl.class).getUser();
            msg.setSender(sender);
            msg.setOwner(entity);
            msg.setContents(UIUtils.Bean.toUIItem(richItems));
            msg.setDate(new Date());
            msg.setCategory(UIMsg.Category.CHAT);
            msg.setState(UIMsg.State.PENDING);
            msg.setDirection(UIMsg.Direction.SEND);
            msg.setLanguage(currLang);
            showMsg(msg);
            UIContext.me().getBean(EventServiceImpl.class).broadcast(new UIEvent(UIEventType.SEND_CHATMSG_REQUEST, msg));
        }else{
            WebOptionPane.showMessageDialog(this,"不能发送空消息！","错误", WebOptionPane.ERROR_MESSAGE);
        }
    }

    public void showMsg(UIMsg msg) {
        isAppendMsg = true;
        msgGroupPanel.add(new MsgPane(msg));
        msgGroupPanel.revalidate();
    }

    protected void setInputButtonStyle(WebButton webButton) {
        webButton.setRound(2);
        webButton.setRolloverDecoratedOnly(true);
    }

    public void update() {
        ImageIcon icon = new ImageIcon(entity.getAvatar());
        avatarImage.setIcon(icon);
        nickLabel.setText(entity.getNick());
        signLabel.setText(entity.getSign());
    }

    @Override
    public void installSkin(SkinService skinService) {
        super.installSkin(skinService);

        // input toolbar com.alee.extended.breadcrumb.icons
        textBtn.setIcon(skinService.getIconByKey("chat/toolbar/text"));
        emoticonBtn.setIcon(skinService.getIconByKey("chat/toolbar/emoticon"));
        screenCaptureBtn.setIcon(skinService.getIconByKey("chat/toolbar/screenCapture"));
        picturesBtn.setIcon(skinService.getIconByKey("chat/toolbar/pictures"));
        historyBtn.setIcon(skinService.getIconByKey("chat/toolbar/history"));
        sendBtn.setIcon(skinService.getIconByKey("chat/toolbar/send"));

        inputToolbar.setPainter(new ColorPainter(new Color(230, 230, 230)));
        inputPanel.setPainter(new ColorPainter(new Color(250, 250, 250)));
        msgGroupPanel.setPainter(new ColorPainter(new Color(250, 250, 250)));
        contentInput.setBackground(new Color(250, 250, 250));
        headerPanel.setPainter(skinService.getPainterByKey("chat/navBg"));

    }

    public MsgGroupPanel getMsgGroupPanel() {
        return msgGroupPanel;
    }
}
