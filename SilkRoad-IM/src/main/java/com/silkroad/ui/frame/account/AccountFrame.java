package com.silkroad.ui.frame.account;

import com.alee.laf.optionpane.WebOptionPane;
import com.silkroad.core.exception.IMException;
import com.silkroad.ui.event.UIActionHandler;
import com.silkroad.ui.event.UIEvent;
import com.silkroad.ui.event.UIEventHandler;
import com.silkroad.ui.event.UIEventType;
import com.silkroad.ui.frame.UIFrame;
import com.silkroad.ui.frame.login.LoginFrame;
import com.silkroad.ui.panel.account.AccountPane;
import com.silkroad.ui.service.SkinService;
import com.silkroad.ui.skin.Skin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 * Created by caoyong on 2014/7/28.
 */
public class AccountFrame extends UIFrame implements Skin {

    private static final Logger log = LoggerFactory.getLogger(AccountFrame.class);

    private LoginFrame loginFrame;

    private AccountPane contentWrap;

    public AccountFrame(LoginFrame loginFrame){
        this.loginFrame = loginFrame;
        initUI();
    }

    public void initUI(){
        contentWrap = new AccountPane(this);
        setUIContentPane(contentWrap);
        setTitle("用户注册");
        setPreferredSize(new Dimension(400,280));
        pack();
        setLocationRelativeTo(null);
        setRound(10);
    }

    @Override
    public void installSkin(SkinService skinService) {
        setIconImage(getSkinService().getIconByKey("window/titleWIcon").getImage());
        contentWrap.installSkin(skinService);
        super.installSkin(skinService);
    }
    @UIEventHandler(UIEventType.USER_REG_SUCCESS)
    protected void regAccountSuccess(UIEvent uiEvent){
        WebOptionPane.showMessageDialog(this,"注册成功", "提示", WebOptionPane.INFORMATION_MESSAGE);
    }

    @UIEventHandler(UIEventType.USER_REG_ERROR)
    protected void regAccountError(UIEvent uiEvent){
        WebOptionPane.showMessageDialog(this,"注册失败：" + uiEvent.getTarget(), "提示", WebOptionPane.ERROR_MESSAGE);
    }

    @UIActionHandler
    private void closeToLogin(){
        loginFrame.setVisible(true);
        this.dispose();
    }
}
