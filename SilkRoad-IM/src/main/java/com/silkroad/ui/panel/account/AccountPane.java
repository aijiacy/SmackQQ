package com.silkroad.ui.panel.account;

import com.alee.extended.layout.TableLayout;
import com.alee.extended.panel.CenterPanel;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebPasswordField;
import com.alee.laf.text.WebTextField;
import com.silkroad.ui.bean.UIAccount;
import com.silkroad.ui.componet.TitleComponent;
import com.silkroad.ui.event.UIActionHandlerProxy;
import com.silkroad.ui.event.UIEvent;
import com.silkroad.ui.event.UIEventType;
import com.silkroad.ui.frame.account.AccountFrame;
import com.silkroad.ui.frame.login.LoginFrame;
import com.silkroad.ui.panel.UIContentPane;
import com.silkroad.ui.service.SkinService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by caoyong on 2014/7/28.
 */
public class AccountPane extends UIContentPane {

    private AccountFrame frame;

    private WebPanel middlePanel;
    private WebPanel footerPanel;

    private WebLabel lb_Account;
    private WebLabel lb_Password;
    private WebLabel lb_RePassword;
    private WebTextField txt_Account;
    private WebPasswordField tpf_Password;
    private WebPasswordField tpf_RePassword;

    private WebButton btnRegister;

    public AccountPane(AccountFrame frame) {
        this.frame = frame;
        add(createHeader(),BorderLayout.NORTH);
        add(createMiddle(),BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    @Override
    public void installSkin(SkinService skinService) {
        // 背景，可以处理QQ印象图片
        this.setPainter(skinService.getPainterByKey("loginSet/background"));
        // 底部背景
        footerPanel.setPainter(skinService.getPainterByKey("loginSet/footerBackground"));
    }

    private WebPanel createHeader(){
        WebPanel headerPanel = new WebPanel();
        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new Dimension(-1, 32));
        // 我自己写了个标题组件，透明的，可以添加到每个窗口上，可以封装为默认继承
        TitleComponent titleComponent = new TitleComponent(frame);
        titleComponent.setShowSkinButton(false);
        titleComponent.setShowMaximizeButton(false);
        titleComponent.setShowMinimizeButton(false);
        titleComponent.setShowSettingButton(false);
        titleComponent.setShowCloseButton(true);
        titleComponent.getCloseButton().addActionListener(new UIActionHandlerProxy(frame,"closeToLogin"));
        headerPanel.add(titleComponent, BorderLayout.NORTH);
        return headerPanel;
    }

    private WebPanel createMiddle(){
        middlePanel = new WebPanel();
        middlePanel.setOpaque(false);
        middlePanel.setPreferredSize(new Dimension(380, 180));

        lb_Account = new WebLabel("用户账号");
        lb_Password = new WebLabel("用户密码");
        lb_RePassword = new WebLabel("确认密码");

        txt_Account = new WebTextField();
        txt_Account.setInputPrompt("请输入用户账号");
        tpf_Password = new WebPasswordField();
        tpf_Password.setInputPrompt("请输入用户密码");
        tpf_RePassword = new WebPasswordField();
        tpf_RePassword.setInputPrompt("再次输入确认密码");

        middlePanel.add(new WebPanel(new TableLayout( new double[][]{ { TableLayout.PREFERRED, TableLayout.FILL },
                { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED } }, 5, 5 ))
        {
            {
                setRound(10);
                setMargin(35,20,5,20);
                add ( lb_Account, "0,0" );
                add ( txt_Account, "1,0" );

                add ( lb_Password, "0,1" );
                add ( tpf_Password, "1,1" );

                add ( lb_RePassword, "0,2" );
                add ( tpf_RePassword, "1,2" );
            }
        }, BorderLayout.CENTER);

        return middlePanel;
    }
    private WebPanel createFooter(){
        footerPanel = new WebPanel();
        footerPanel.setPreferredSize(new Dimension(-1, 40));
        btnRegister = new WebButton("注  册");
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String account = txt_Account.getText();
                String password = new String(tpf_Password.getPassword());
                String rePassword = new String(tpf_RePassword.getPassword());
                if(account.length() >= 6){
                    if(password.length() >= 6){
                        if(password.equals(rePassword)){
                            UIAccount uiAccount = new UIAccount();
                            uiAccount.setLoginName(account);
                            uiAccount.setPassword(password);
                            frame.getEventService().broadcast(new UIEvent(UIEventType.USER_REG_REQUEST, uiAccount));
                        }else{
                            WebOptionPane.showMessageDialog(frame, "两次输入的密码不一致");
                        }
                    }else{
                        WebOptionPane.showMessageDialog(frame, "请输入用户密码(6位以上)");
                    }
                }else{
                    WebOptionPane.showMessageDialog(frame, "请输入用户账号(6位以上)");
                }
            }
        });
        footerPanel.setMargin(5);
        footerPanel.add(new CenterPanel(btnRegister), BorderLayout.CENTER);
        return footerPanel;
    }
}
