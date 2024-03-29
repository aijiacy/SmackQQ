package com.silkroad.ui.panel.main;

import com.alee.extended.image.WebDecoratedImage;
import com.alee.extended.image.WebImage;
import com.alee.extended.panel.CenterPanel;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.label.WebLabel;
import com.alee.laf.text.WebTextField;
import com.silkroad.ui.bean.UIStatus;
import com.silkroad.ui.componet.StatusButton;
import com.silkroad.ui.componet.TitleComponent;
import com.silkroad.ui.frame.SkinFrame;
import com.silkroad.ui.frame.main.MainFrame;
import com.silkroad.ui.panel.UIPanel;
import com.silkroad.ui.service.SkinService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Project  : iqq-projects
 * Author   : 承∮诺 < 6208317@qq.com >
 * Created  : 14-5-8
 * License  : Apache License 2.0
 */
public class HeaderPanel extends UIPanel {
    private MainFrame frame;
    WebDecoratedImage avatar;
    WebLabel nickLbl;
    WebLabel sigLbl;
    StatusButton statusButton;
    public HeaderPanel(MainFrame frame) {
        super();
        this.frame = frame;

        initTitle();
        initUserInfo();
        initSearcher();
    }

    private void initTitle() {
        // 标题控件
        TitleComponent titleComponent = new TitleComponent(frame);
        titleComponent.setShowSettingButton(false);
        titleComponent.setShowMaximizeButton(false);
        titleComponent.getSkinButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SkinFrame skinFrame = new SkinFrame();
                skinFrame.setVisible(true);
            }
        });
        this.add(titleComponent, BorderLayout.NORTH);
    }

    private void initUserInfo() {


        UIPanel headerInfo = new UIPanel();
        // 头像
        avatar = new WebDecoratedImage(frame.getResourceService().getIcon("icons/login/face.jpg", 50, 50) );
        avatar.setRound(5);
        avatar.setShadeWidth(1);
        avatar.setDrawBorder(false);
        GroupPanel avatarWrap = new GroupPanel(10, avatar);
        avatarWrap.setMargin(10);
        headerInfo.add(avatarWrap, BorderLayout.WEST);
        this.add(headerInfo, BorderLayout.CENTER);

        // 信息
        nickLbl = new WebLabel("");
        sigLbl = new WebLabel("...");
        nickLbl.setForeground(Color.decode("#000000"));
        nickLbl.setDrawShade(true);
        nickLbl.setShadeColor(Color.lightGray);
        nickLbl.setFontSize(14);
        sigLbl.setForeground(Color.decode("#151515"));
        sigLbl.setFontSize(12);
        statusButton = new StatusButton(UIStatus.ONLINE, 13);
        GroupPanel nickAndStatus = new GroupPanel(3,
                new CenterPanel(statusButton, false ,true), nickLbl);
        GroupPanel userInfo = new GroupPanel(5, false, nickAndStatus, sigLbl);
        userInfo.setMargin(0, 0, 5, 5);
        headerInfo.add(new CenterPanel(userInfo, false, true), BorderLayout.CENTER);
    }

    private void initSearcher() {
        // 搜索
        WebTextField searchFld = new WebTextField();
        WebImage searcheImg = new WebImage(frame.getSkinService().getIconByKey("main/searchIcon", 20, 20));
        searcheImg.setMargin(0, 3, 0, 3);
        searchFld.setPreferredSize(new Dimension(frame.getWidth(), 25));
        searchFld.setLeadingComponent(searcheImg);
        searchFld.setOpaque(false);
        searchFld.setInputPrompt(frame.getI18nService().getMessage("findContact"));
        searchFld.setInputPromptForeground(Color.decode("#D8D8D8"));
        searchFld.setPainter(frame.getSkinService().getPainterByKey("main/searchBg"));
        this.add(searchFld, BorderLayout.SOUTH);
    }

    @Override
    public void installSkin(SkinService skinService) {
        // 背景
        this.setPainter(skinService.getPainterByKey("skin/background"));
    }

    public void updateSelfFace(Image face) {
         avatar.setImage(face);
    }

    public void updateSelfSign(String sign){
        sigLbl.setText(sign);
    }

    public void updateSelfNick(String nick){
        nickLbl.setText(nick);
    }

    public void updateSelfStatus(UIStatus status){
        statusButton.setStatus(status);
    }
}
