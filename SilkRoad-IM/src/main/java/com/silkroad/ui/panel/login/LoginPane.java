package com.silkroad.ui.panel.login;

import com.alee.extended.image.WebDecoratedImage;
import com.alee.extended.image.WebImage;
import com.alee.extended.label.WebLinkLabel;
import com.alee.extended.panel.CenterPanel;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebPasswordField;
import com.silkroad.ui.event.UIActionHandlerProxy;
import com.silkroad.ui.componet.StatusButton;
import com.silkroad.ui.componet.TitleComponent;
import com.silkroad.ui.frame.login.LoginFrame;
import com.silkroad.ui.panel.UIContentPane;
import com.silkroad.ui.panel.UIPanel;
import com.silkroad.ui.service.SkinService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Project  : iqq-projects
 * Author   : 承∮诺 < 6208317@qq.com >
 * Created  : 14-5-5
 * License  : Apache License 2.0
 */
public class LoginPane extends UIContentPane {
	private static final long serialVersionUID = 1L;
	private LoginFrame frame;
    private UIPanel headerPanel = new UIPanel();
    private UIPanel middlePanel = new UIPanel();
    private UIPanel footerPanel = new UIPanel();
    private WebButton loginBtn;
    private WebComboBox cbxAccount;
    private WebPasswordField pwdPassword;
    private WebDecoratedImage userface;
    private WebLinkLabel lblRegister;
    private WebLabel lblFindPassword;
    private WebCheckBox chkRePassword;
    private WebCheckBox chkAutoLogin;

    public LoginPane(LoginFrame frame) {
        this.frame = frame;
        this.add(createHeader(), BorderLayout.NORTH);
        this.add(createFooter(), BorderLayout.SOUTH);
        this.add(createMiddle(), BorderLayout.CENTER);
        this.setRound(0);
    }

    /**
     * 安装皮肤
     *
     * @param skinService
     */
    @Override
    public void installSkin(SkinService skinService) {
        // 登录界面的背景，可以处理QQ印象图片
        this.setPainter(skinService.getPainterByKey("login/background"));

        // 密码框小图标
        WebImage keyIcon = new WebImage(skinService.getIconByKey("login/keyIcon"));
        pwdPassword.setTrailingComponent(keyIcon);

        // 标签字体颜色
        lblRegister.setForeground(skinService.getColorByKey("login/labelColor"));
        lblFindPassword.setForeground(skinService.getColorByKey("login/labelColor"));

        // 选择框字体颜色
        chkRePassword.setForeground(skinService.getColorByKey("login/checkBoxColor"));
        chkAutoLogin.setForeground(skinService.getColorByKey("login/checkBoxColor"));

        // 底部背景
        footerPanel.setPainter(skinService.getPainterByKey("login/footerBackground"));
    }

    // 上面部分在里面添加上去的
    private WebPanel createHeader() {
        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new Dimension(-1, 90));

        // 我自己写了个标题组件，透明的，可以添加到每个窗口上，可以封装为默认继承
        TitleComponent titleComponent = new TitleComponent(frame);
        titleComponent.setShowSkinButton(false);
        titleComponent.setShowMaximizeButton(false);
        titleComponent.getSettingButton().addActionListener(new UIActionHandlerProxy(frame, "loginSet"));
        //frame.setTitleComponent(titleComponent);
        headerPanel.add(titleComponent, BorderLayout.NORTH);

        return headerPanel;
    }

    // 中间，头像和输入框在这里面实现
    private WebPanel createMiddle() {
        Dimension dimFld = new Dimension(170, 30);

        middlePanel.setOpaque(false);
        middlePanel.setPreferredSize(new Dimension(320, 120));
        //middlePanel.setBackground(Color.BLUE);

        WebPanel left = new WebPanel();
        WebPanel right = new WebPanel();
        left.setOpaque(false);
        left.setMargin(0, 18, 0, 8);
        right.setMargin(19, 0, 10, 0);
        right.setOpaque(false);


        userface = new WebDecoratedImage();
        // 头像
        ImageIcon icon = frame.getResourceService().getIcon("icons/login/qq_icon.png");
        userface.setImage(icon.getImage().getScaledInstance(80, 80, 100));
        userface.setShadeWidth(2);
        userface.setRound(3);
        userface.setBorderColor(Color.WHITE);
        left.add(userface);

        // 账号输入一行
        String[] items = { "aijiacy"};
        lblRegister = new WebLinkLabel(frame.getI18nService().getMessage("login.regAccount"));
        lblRegister.addActionListener(new UIActionHandlerProxy(frame, "regUser"));
        cbxAccount = new WebComboBox(items);
        cbxAccount.setEditable(true);
        cbxAccount.setPreferredSize(dimFld);

        // 间隙5, 水平true，排为一组过去
        GroupPanel accountGroup = new GroupPanel(5, true, cbxAccount, lblRegister);
        accountGroup.setOpaque(false);

        // 密码输入一行
        lblFindPassword = new WebLabel(frame.getI18nService().getMessage("login.forgetPwd"));
        pwdPassword = new WebPasswordField();
        pwdPassword.setPreferredSize(dimFld);

        // 间隙5, 水平true，排为一组过去
        GroupPanel pwdGroup = new GroupPanel(5, true, pwdPassword, lblFindPassword);

        // 选项一行
        chkRePassword = new WebCheckBox(frame.getI18nService().getMessage("login.rememberPwd"));
        chkAutoLogin = new WebCheckBox(frame.getI18nService().getMessage("login.autoogin"));
        final StatusButton btnStatus = new StatusButton(null);
        chkRePassword.setFontSize(13);
        chkAutoLogin.setFontSize(13);

        // 间隙5, 水平true，排为一组过去
        GroupPanel optionGroup = new GroupPanel(5, true,  btnStatus, chkRePassword, chkAutoLogin);

        // 把三行，垂直来放进去
        right.add(new GroupPanel(5, false, accountGroup, pwdGroup, optionGroup), BorderLayout.NORTH);

        middlePanel.add(left, BorderLayout.WEST);
        middlePanel.add(right, BorderLayout.CENTER);

        loginBtn.setEnabled(false);
        // 登录处理
        loginBtn.addActionListener(new UIActionHandlerProxy(frame, "login", cbxAccount, pwdPassword, btnStatus, chkRePassword.isSelected(), chkAutoLogin.isSelected()));
        /*
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IMAccount account = new IMAccount();
                account.setLoginName(cbxAccount.getSelectedItem().toString());
                account.setPassword(new String(pwdPassword.getPassword()));
                account.setRememberPwd(chkRePassword.isSelected());
                frame.login(account);
            }
        });L
        */
        return middlePanel;
    }

    private WebPanel createFooter() {

        //footerPanel.setOpaque(false);
        footerPanel.setPreferredSize(new Dimension(-1, 40));

        loginBtn = new WebButton(frame.getI18nService().getMessage("login.login"));
        loginBtn.setPreferredHeight(30);
        loginBtn.setPreferredWidth(150);
        loginBtn.setMargin(5);

        footerPanel.add(new CenterPanel(loginBtn), BorderLayout.CENTER);

        return footerPanel;
    }

    public void setLoginBtnEnable(boolean flag){
        loginBtn.setEnabled(flag);
    }
}
