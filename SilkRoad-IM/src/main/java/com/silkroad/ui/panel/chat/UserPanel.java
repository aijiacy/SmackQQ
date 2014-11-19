package com.silkroad.ui.panel.chat;

import com.alee.laf.button.WebButton;
import com.alee.laf.toolbar.WebToolBar;
import com.silkroad.ui.bean.UIMsg;
import com.silkroad.ui.bean.UIUser;
import com.silkroad.ui.bean.content.UIContentItem;
import com.silkroad.ui.bean.content.UITextItem;
import com.silkroad.ui.service.SkinService;

import java.util.ArrayList;
import java.util.Date;

/**
 * Project  : iqq-projects
 * Author   : 承∮诺 < 6208317@qq.com >
 * Created  : 14-5-11
 * License  : Apache License 2.0
 */
public class UserPanel extends BasicPanel {

    protected WebButton shakeBtn;

    public UserPanel(UIUser entity) {
        super(entity);

        //test();
    }

    private void test() {
        UIMsg msg = new UIMsg();
        msg.setSender((UIUser) entity);
        java.util.List<UIContentItem> contents = new ArrayList<UIContentItem>();
        UITextItem text = new UITextItem("test content...");
        contents.add(text);
        msg.setContents(contents);
        msg.setDate(new Date());
        msg.setState(UIMsg.State.READ);
        showMsg(msg);
        showMsg(msg);
        showMsg(msg);
        showMsg(msg);
        showMsg(msg);
    }

    public UIUser getUser() {
        return (UIUser) this.entity;
    }

    @Override
    protected void initInputToolbar(WebToolBar inputToolbar) {
        super.initInputToolbar(inputToolbar);

        shakeBtn = new WebButton();

        setInputButtonStyle(shakeBtn);

        // 震动
        inputToolbar.add(3, shakeBtn);
    }

    @Override
    public void installSkin(SkinService skinService) {
        super.installSkin(skinService);

        shakeBtn.setIcon(skinService.getIconByKey("chat/toolbar/shake"));
    }
}
