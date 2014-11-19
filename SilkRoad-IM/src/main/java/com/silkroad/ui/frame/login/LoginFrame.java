package com.silkroad.ui.frame.login;

import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.text.WebPasswordField;
import com.silkroad.core.bean.args.LoginArgs;
import com.silkroad.ui.bean.UIUser;
import com.silkroad.ui.componet.StatusButton;
import com.silkroad.ui.context.UIContext;
import com.silkroad.ui.event.UIActionHandler;
import com.silkroad.ui.event.UIEvent;
import com.silkroad.ui.event.UIEventHandler;
import com.silkroad.ui.event.UIEventType;
import com.silkroad.ui.frame.UIFrame;
import com.silkroad.ui.frame.account.AccountFrame;
import com.silkroad.ui.frame.setting.SettingFrame;
import com.silkroad.ui.manager.MainManager;
import com.silkroad.ui.panel.login.LoginPane;
import com.silkroad.ui.service.SkinService;
import com.silkroad.ui.service.impl.CacheServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 这只是一个登录界面模型，使用代码布局，如果使用IDE部分是不可以的，不好控制和使用组件
 * 那个背景图片可以使用现在QQ平时更换的背景图片，处理一下就可以换了哈
 * 这个界面的好处是可以随时更新背景图片
 *
 * 存在问题：
 *  LINUX下放大后再还原会那个背景出现问题，正想办法解决，重新设置背景或者怎么的
 *  现在这个问题设置不能最大化，先这样子处理
 *
 * Project  : iqq-projects
 * Author   : 承∮诺 < 6208317@qq.com >
 * Created  : 14-4-15
 * License  : Apache License 2.0
 */
public class LoginFrame extends UIFrame {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(LoginFrame.class);
    private LoginPane contentWrap;

    public LoginFrame() {
        super();
        initUI();
    }

    private void initUI() {
        contentWrap = new LoginPane(this);
        // 登录面板
        setUIContentPane(contentWrap);
        setTitle(getI18nService().getMessage("app.name"));
        setPreferredSize(new Dimension(400, 290));        // 首选大小
        pack();
        setLocationRelativeTo(null);                      // 居中
        setDefaultCloseOperation(WebFrame.EXIT_ON_CLOSE);

        LoginArgs param = new LoginArgs();
        param.setHost("im.zyun168.com");
        param.setPort(5223);
        param.setUseProxy(false);
        param.setClientRes("SilkRoad");

        UIEvent event = new UIEvent();
        event.setType(UIEventType.LOGIN_READY_REQUEST);
        event.setTarget(param);
        eventService.broadcast(event);
    }

    private BufferedImage getDefaultAvatar(){
        try {
            File file = this.getResourceService().getFile("icons/login/avatar.png");
            return ImageIO.read(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void installSkin(SkinService skinService) {
        setIconImage(getSkinService().getIconByKey("window/titleWIcon").getImage());
        this.contentWrap.installSkin(skinService);
        super.installSkin(skinService);
    }

    @UIActionHandler
    public void loginSet(){
        new SettingFrame(this).setVisible(true);
        this.setVisible(false);
    }

    @UIActionHandler
    public void regUser(){
        new AccountFrame(this).setVisible(true);
        this.setVisible(false);
    }

    @UIActionHandler
    public void login(ActionEvent e, WebComboBox cbxAccount, WebPasswordField pwdPassword, final StatusButton statusBtn, boolean isRePassword, boolean isAutoLogon) {

        LoginArgs param = new LoginArgs();
        param.setLoginName(cbxAccount.getSelectedItem().toString());
        param.setPassword(new String(pwdPassword.getPassword()));
        param.setStatus(statusBtn.getStatus());
        param.setRememberPwd(isRePassword);
        param.setAutoLogin(isAutoLogon);

        //param.setHost("localhost");
        param.setHost("im.zyun168.com");
        param.setPort(5223);
        param.setUseProxy(false);
        param.setClientRes("SilkRoad");

        UIEvent event = new UIEvent();
        event.setType(UIEventType.LOGIN_REQUEST);
        event.setTarget(param);
        eventService.broadcast(event);
    }

    @UIEventHandler(UIEventType.LOGIN_READY_SUCCESS)
    public void processReadyOK(UIEvent uiEvent){
        contentWrap.setLoginBtnEnable(true);
    }

    @UIEventHandler(UIEventType.LOGIN_READY_ERROR)
    public void processReadyError(UIEvent uiEvent){
        WebOptionPane.showMessageDialog(contentWrap, "服务器连接失败: " + uiEvent.getTarget(), "提示", WebOptionPane.INFORMATION_MESSAGE);
    }

    @UIEventHandler(UIEventType.LOGIN_ERROR)
    public void processLoginError(UIEvent uiEvent){
        WebOptionPane.showMessageDialog(contentWrap, "登陆失败: " + uiEvent.getTarget(), "提示", WebOptionPane.INFORMATION_MESSAGE);
    }

    @UIEventHandler(UIEventType.LOGIN_SUCCESS)
    public void processLoginSuccess(UIEvent uiEvent){

        MainManager.show();
        this.setVisible(false);
        UIUser uiUser = (UIUser) uiEvent.getTarget();
        if(uiUser.getAvatar() == null){
            uiUser.setAvatar(getDefaultAvatar());
        }
        UIContext.me().getBean(CacheServiceImpl.class).setUser((UIUser)uiEvent.getTarget());
        eventService.broadcast(new UIEvent(UIEventType.SELF_INFO_UPDATE,uiEvent.getTarget()));
        eventService.broadcast(new UIEvent(UIEventType.SELF_SIGN_UPDATE,uiEvent.getTarget()));
        eventService.broadcast(new UIEvent(UIEventType.BUDDY_LIST_REQUEST));
    }

}
