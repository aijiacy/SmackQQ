package com.silkroad.ui.panel.setting;

import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.painter.TitledBorderPainter;
import com.alee.extended.panel.BorderPanel;
import com.alee.extended.panel.CenterPanel;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebPasswordField;
import com.alee.laf.text.WebTextField;
import com.silkroad.ui.componet.TitleComponent;
import com.silkroad.ui.event.UIActionHandlerProxy;
import com.silkroad.ui.frame.setting.SettingFrame;
import com.silkroad.ui.panel.UIContentPane;
import com.silkroad.ui.panel.UIPanel;
import com.silkroad.ui.service.SkinService;

import java.awt.*;

/**
 * Created by caoyong on 2014/7/6.
 */
public class SettingPane extends UIContentPane {
    private SettingFrame frame;
    private UIPanel headerPanel = new UIPanel();
    private UIPanel middlePanel = new UIPanel();
    private UIPanel footerPanel = new UIPanel();

    private WebButton btnSave;
    private WebButton btnClose;
    private WebButton btnTestProxy;

    private WebLabel lblHost;
    private WebLabel lblPort;
    private WebTextField txtHost;
    private WebTextField txtPost;

    private WebCheckBox chkUseProxy;
    private WebLabel lblProxyType;
    private WebComboBox cbxProxyType;
    private WebLabel lblProxyHost;
    private WebTextField txtProxyHost;
    private WebLabel lblProxyPort;
    private WebTextField txtProxyPort;
    private WebLabel lblProxyUser;
    private WebTextField txtProxyUser;
    private WebLabel lblProxyPass;
    private WebPasswordField pwdProxyPass;

    public SettingPane(SettingFrame frame) {
        this.frame = frame;
        this.add(createHeader(), BorderLayout.NORTH);
        this.add(createFooter(), BorderLayout.SOUTH);
        this.add(createMiddle(), BorderLayout.CENTER);

        this.setRound(50);
    }

    @Override
    public void installSkin(SkinService skinService) {
        // 背景，可以处理QQ印象图片
        this.setPainter(skinService.getPainterByKey("loginSet/background"));

        this.lblHost.setForeground(skinService.getColorByKey("loginSet/labelColor"));
        this.lblPort.setForeground(skinService.getColorByKey("loginSet/labelColor"));
        this.lblProxyType.setForeground(skinService.getColorByKey("loginSet/labelColor"));
        this.lblProxyHost.setForeground(skinService.getColorByKey("loginSet/labelColor"));
        this.lblProxyPort.setForeground(skinService.getColorByKey("loginSet/labelColor"));
        this.lblProxyUser.setForeground(skinService.getColorByKey("loginSet/labelColor"));
        this.lblProxyPass.setForeground(skinService.getColorByKey("loginSet/labelColor"));

        this.chkUseProxy.setForeground(skinService.getColorByKey("loginSet/checkBoxColor"));

        // 底部背景
        footerPanel.setPainter(skinService.getPainterByKey("loginSet/footerBackground"));
    }

    // 上面部分在里面添加上去的
    private WebPanel createHeader() {
        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new Dimension(-1, 32));

        // 我自己写了个标题组件，透明的，可以添加到每个窗口上，可以封装为默认继承
        TitleComponent titleComponent = new TitleComponent(frame);
        titleComponent.setShowSkinButton(false);
        titleComponent.setShowMaximizeButton(false);
        titleComponent.setShowMinimizeButton(false);
        titleComponent.setShowSettingButton(false);
        titleComponent.setShowCloseButton(false);
        headerPanel.add(titleComponent, BorderLayout.NORTH);

        return headerPanel;
    }

    // 中间，头像和输入框在这里面实现
    private WebPanel createMiddle() {
        Dimension dimTxt = new Dimension(120, 28);
        middlePanel.setOpaque(false);
        middlePanel.setPreferredSize(new Dimension(460, 280));

        this.lblHost = new WebLabel(frame.getI18nService().getMessage("setting.host"));
        this.txtHost = new WebTextField();
        this.txtHost.setPreferredSize(dimTxt);
        this.txtHost.setText("127.0.0.1");
        this.txtHost.setEditable(false);
        this.lblPort = new WebLabel(frame.getI18nService().getMessage("setting.port"));
        this.txtPost = new WebTextField();
        this.txtPost.setPreferredSize(dimTxt);
        this.txtPost.setText("5223");
        this.txtPost.setEditable(false);

        this.lblProxyHost = new WebLabel(frame.getI18nService().getMessage("setting.proxyHost"));
        this.txtProxyHost = new WebTextField();
        this.txtProxyHost.setPreferredSize(dimTxt);

        this.lblProxyPort = new WebLabel(frame.getI18nService().getMessage("setting.proxyPort"));
        this.txtProxyPort = new WebTextField();
        this.txtProxyPort.setPreferredSize(dimTxt);

        this.lblProxyUser = new WebLabel(frame.getI18nService().getMessage("setting.username"));
        this.txtProxyUser = new WebTextField();
        this.txtProxyUser.setPreferredSize(dimTxt);

        this.lblProxyPass = new WebLabel(frame.getI18nService().getMessage("setting.password"));
        this.pwdProxyPass = new WebPasswordField();
        this.pwdProxyPass.setPreferredSize(dimTxt);

        this.chkUseProxy = new WebCheckBox(frame.getI18nService().getMessage("setting.isProxy"));
        this.chkUseProxy.setFontSize(13);

        this.lblProxyType = new WebLabel(frame.getI18nService().getMessage("setting.proxyType"));
        String[] items = {"HTTP", "SOCKET4", "SOCKET5"};
        this.cbxProxyType = new WebComboBox(items);

        this.btnTestProxy = new WebButton(frame.getI18nService().getMessage("setting.proxyTest"));
        this.btnTestProxy.setPreferredSize(new Dimension(80,30));

        middlePanel.add(new BorderPanel(new WebPanel(new VerticalFlowLayout(5, 5)) {
            {
                this.setPreferredSize(new Dimension(-1, 60));
                setMargin(5, 18, 0, 10);
                final TitledBorderPainter ntbp = new TitledBorderPainter(frame.getI18nService().getMessage("setting.serverset"));
                ntbp.setTitleOffset(10);
                ntbp.setRound(Math.max(0, 8));
                setPainter(ntbp);
                add(new GroupPanel(10, true, lblHost, txtHost, lblPort, txtPost), BorderLayout.LINE_START);
            }
        }), BorderLayout.NORTH);


        middlePanel.add(new BorderPanel(new WebPanel(new VerticalFlowLayout(5, 5)) {
            {
                setMargin(5, 18, 0, 10);
                final TitledBorderPainter stbp = new TitledBorderPainter(frame.getI18nService().getMessage("setting.proxyset"));
                stbp.setTitleOffset(10);
                stbp.setRound(Math.max(0, 8));
                setPainter(stbp);
                add(chkUseProxy, BorderLayout.LINE_START);
                add(new GroupPanel(10, true, lblProxyType, cbxProxyType));
                add(new GroupPanel(10, true, lblProxyHost, txtProxyHost, lblProxyPort, txtProxyPort), BorderLayout.LINE_END);
                add(new GroupPanel(10, true, lblProxyUser, txtProxyUser, lblProxyPass, pwdProxyPass), BorderLayout.LINE_END);
                add(new WebPanel(){
                    {
                        add(btnTestProxy,BorderLayout.EAST);
                    }
                },BorderLayout.LINE_END);
            }
        }), BorderLayout.CENTER);

        return middlePanel;
    }

    private WebPanel createFooter() {

        //footerPanel.setOpaque(false);
        footerPanel.setPreferredSize(new Dimension(-1, 40));

        btnSave = new WebButton(frame.getI18nService().getMessage("setting.save"));
        btnSave.setPreferredHeight(30);
        btnSave.setPreferredWidth(80);
        btnSave.setMargin(5);

        btnClose = new WebButton(frame.getI18nService().getMessage("setting.close"));
        btnClose.setPreferredHeight(30);
        btnClose.setPreferredWidth(80);
        btnClose.setMargin(5);
        btnClose.addActionListener(new UIActionHandlerProxy(frame, "closeToLogin"));

        GroupPanel btnGroups = new GroupPanel(true, btnSave, btnClose);
        //btnGroups.setOpaque(false);
        CenterPanel centerPanel = new CenterPanel(btnGroups);
        footerPanel.add(centerPanel, BorderLayout.CENTER);
        return footerPanel;
    }
}
