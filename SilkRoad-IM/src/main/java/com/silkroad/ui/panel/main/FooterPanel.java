package com.silkroad.ui.panel.main;

import com.alee.laf.button.WebButton;
import com.alee.laf.toolbar.ToolbarStyle;
import com.alee.laf.toolbar.WebToolBar;
import com.silkroad.ui.event.UIActionHandlerProxy;
import com.silkroad.ui.frame.main.MainFrame;
import com.silkroad.ui.panel.UIPanel;
import com.silkroad.ui.service.SkinService;

import java.awt.*;

/**
 * Project  : iqq-projects
 * Author   : 承∮诺 < 6208317@qq.com >
 * Created  : 14-5-8
 * License  : Apache License 2.0
 */
public class FooterPanel extends UIPanel {
    private MainFrame frame;
    private WebToolBar toolBar = new WebToolBar();

    private WebButton add = new WebButton();
    private WebButton mail = new WebButton();
    private WebButton qzone = new WebButton();
    private WebButton weibo = new WebButton();
    private WebButton msg = new WebButton();
    private WebButton setting = new WebButton();


    public FooterPanel(MainFrame frame) {
        super();
        this.frame = frame;

        initButtons();
        initToolbar();
        initListener();
    }

    private void initButtons() {

    }

    private void initToolbar() {
        toolBar.setFloatable(false);
        toolBar.setToolbarStyle(ToolbarStyle.attached);
        toolBar.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        toolBar.setTopBgColor(new Color(255, 255, 255, 130));
        toolBar.setBottomBgColor(new Color(255, 255, 255, 130));
        toolBar.setOpaque(false);

        toolBar.add(setting);
        toolBar.add(msg);
        toolBar.addSeparator();
        toolBar.add(weibo);
        toolBar.add(qzone);
        toolBar.add(mail);
        toolBar.addSeparator();
        toolBar.addToEnd(add);
        toolBar.setPreferredSize(new Dimension(-1, 26));

        setButtonStyle(add, mail, qzone, weibo, msg, setting);

        add(toolBar);
    }

    protected void setButtonStyle(WebButton... bs) {
        for(WebButton b : bs) {
            b.setPreferredSize(new Dimension(40, 40));
            b.setRound(2);
            b.setRolloverDecoratedOnly(true);
            b.setBackground(new Color(0, 0, 0, 0));
        }
    }

    @Override
    public void installSkin(SkinService skinService) {
        super.installSkin(skinService);

        add.setIcon(skinService.getIconByKey("main/toolbar/add"));
        mail.setIcon(skinService.getIconByKey("main/toolbar/mail"));
        qzone.setIcon(skinService.getIconByKey("main/toolbar/qzone"));
        weibo.setIcon(skinService.getIconByKey("main/toolbar/weibo"));
        msg.setIcon(skinService.getIconByKey("main/toolbar/msg"));
        setting.setIcon(skinService.getIconByKey("main/toolbar/setting"));

    }

    public void initListener(){
        add.addActionListener(new UIActionHandlerProxy(frame,"showSearchPane"));
    }
}
