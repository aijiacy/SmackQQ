package com.silkroad.ui.frame.setting;

import com.alee.laf.rootpane.WebFrame;
import com.silkroad.ui.event.UIActionHandler;
import com.silkroad.ui.frame.UIFrame;
import com.silkroad.ui.frame.login.LoginFrame;
import com.silkroad.ui.panel.setting.SettingPane;
import com.silkroad.ui.service.SkinService;
import com.silkroad.ui.skin.Skin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 * Created by caoyong on 2014/7/6.
 */
public class SettingFrame extends UIFrame implements Skin {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(SettingFrame.class);
    private LoginFrame loginFrame;
    private SettingPane contentWrap;

    public SettingFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        initUI();
    }

    private void initUI() {
        contentWrap = new SettingPane(this);
        setUIContentPane(contentWrap);
        setTitle(getI18nService().getMessage("setting.title"));
        setPreferredSize(new Dimension(500, 360));        // 首选大小
        pack();
        setLocationRelativeTo(null);                      // 居中
        setDefaultCloseOperation(WebFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void installSkin(SkinService skinService) {
        setIconImage(getSkinService().getIconByKey("window/titleWIcon").getImage());
        contentWrap.installSkin(skinService);
        super.installSkin(skinService);
    }

    @UIActionHandler
    public void closeToLogin(){
        loginFrame.setVisible(true);
        this.dispose();
    }
}
