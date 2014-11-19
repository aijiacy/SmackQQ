package com.silkroad.ui.renderer.node;

import com.alee.extended.image.WebDecoratedImage;
import com.alee.extended.panel.CenterPanel;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.label.WebLabel;
import com.silkroad.ui.bean.UIBuddy;
import com.silkroad.ui.panel.UIPanel;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 好友显示节点
 *
 * Project  : iqq-projects
 * Author   : 承∮诺 < 6208317@qq.com >
 * Created  : 14-5-10
 * License  : Apache License 2.0
 */
public class BuddyNode extends EntityNode {
    private UIBuddy buddy;
    private UIPanel view = new UIPanel();
    // 头像
    private BufferedImage avatar;
    private WebDecoratedImage avatarImage;
    // 昵称
    WebLabel nickLbl;
    WebLabel signLbl;

    public BuddyNode(UIBuddy buddy) {
        super(buddy);
        this.buddy = buddy;

        // 头像
        avatarImage = new WebDecoratedImage();
        avatarImage.setShadeWidth(1);
        avatarImage.setRound(4);
        avatarImage.setDrawGlassLayer(false);

        // 昵称
        nickLbl = new WebLabel();
        signLbl = new WebLabel();
        nickLbl.setFontSize(14);
        signLbl.setFontSize(13);
        signLbl.setForeground(Color.GRAY);
        GroupPanel textGroup = new GroupPanel(0, false, nickLbl, signLbl);
        textGroup.setMargin(0, 5, 0, 5);

        view.add(avatarImage, BorderLayout.WEST);
        view.add(new CenterPanel(textGroup, false , true), BorderLayout.CENTER);
        view.setMargin(5);
    }

    public UIBuddy getBuddy() {
        return buddy;
    }

    public void setBuddy(UIBuddy buddy) {
        this.buddy = buddy;
    }

    /**
     * 这个方法特别频繁，一定要处理好
     *
     * @return
     */
    public UIPanel getView() {
        if(avatar == null || !avatar.equals(buddy.getAvatar())) {
            avatar = buddy.getAvatar();
            ImageIcon icon = new ImageIcon(avatar);
            avatarImage.setIcon(icon);
        }
        if(!StringUtils.equals(nickLbl.getText(), buddy.getNick())) {
            nickLbl.setText(buddy.getNick());
        }
        if(!StringUtils.equals(signLbl.getText(), buddy.getSign())) {
            signLbl.setText(buddy.getSign());
        }
        return view;
    }
}
