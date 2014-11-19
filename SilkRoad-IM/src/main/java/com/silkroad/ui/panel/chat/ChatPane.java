package com.silkroad.ui.panel.chat;


import com.alee.extended.panel.GroupPanel;
import com.alee.laf.radiobutton.WebRadioButton;
import com.alee.managers.popup.PopupStyle;
import com.alee.managers.popup.WebPopup;
import com.alee.utils.swing.UnselectableButtonGroup;
import com.silkroad.ui.border.TabContentPanelBorder;
import com.silkroad.ui.componet.TitleComponent;
import com.silkroad.ui.context.UIContext;
import com.silkroad.ui.event.UIActionHandlerProxy;
import com.silkroad.ui.event.UIEvent;
import com.silkroad.ui.event.UIEventType;
import com.silkroad.ui.frame.chat.ChatFrame;
import com.silkroad.ui.panel.UIContentPane;
import com.silkroad.ui.service.CacheService;
import com.silkroad.ui.service.SkinService;
import org.sexydock.tabs.jhrome.JhromeTabBorderAttributes;
import org.sexydock.tabs.jhrome.JhromeTabbedPaneUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Project  : iqq-projects
 * Author   : 承∮诺 < 6208317@qq.com >
 * Created  : 14-5-10
 * License  : Apache License 2.0
 */
public class ChatPane extends UIContentPane {
    private static final Logger LOG = LoggerFactory.getLogger(ChatPane.class);
    private ChatFrame frame;
    private JTabbedPane tabbedPane;
    private JButton settingButton;

    private WebPopup languagePopup;
    private WebRadioButton rb_zh;
    private WebRadioButton rb_we;
    private WebRadioButton rb_en;
    private WebRadioButton rb_ru;


    public ChatPane(ChatFrame chatFrame) {
        //super();
        LOG.debug("ChatPane 初始化！");
        frame = chatFrame;

        initTabbedPane();
        initLanguagePane();
        initListener();
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    private void initTabbedPane() {
        TitleComponent titleComponent = new TitleComponent(frame);
        titleComponent.setShowSkinButton(false);
        titleComponent.setShowSettingButton(false);
        //titleComponent.setShowTitle(false);
        add(titleComponent, BorderLayout.NORTH);

        JhromeTabBorderAttributes.SELECTED_BORDER.topColor = new Color(255, 255, 255, 120);
        JhromeTabBorderAttributes.SELECTED_BORDER.bottomColor = new Color(255, 255, 255, 0);
        JhromeTabBorderAttributes.SELECTED_BORDER.shadowColor = new Color( 55 , 55 , 55 , 50 );
        JhromeTabBorderAttributes.SELECTED_BORDER.outlineColor = new Color( 55 , 55 , 55 , 100);
        JhromeTabBorderAttributes.UNSELECTED_BORDER.topColor = new Color(255, 255, 255, 40);
        JhromeTabBorderAttributes.UNSELECTED_BORDER.bottomColor = new Color(255, 255, 255, 0);
        JhromeTabBorderAttributes.UNSELECTED_BORDER.shadowColor = new Color( 55 , 55 , 55 , 20 );
        JhromeTabBorderAttributes.UNSELECTED_BORDER.outlineColor = new Color( 55 , 55 , 55 , 100 );
        JhromeTabBorderAttributes.UNSELECTED_ROLLOVER_BORDER.topColor = new Color( 255 , 255 , 255 , 160);
        JhromeTabBorderAttributes.UNSELECTED_ROLLOVER_BORDER.bottomColor = new Color( 255 , 255 , 255 , 50);
        JhromeTabbedPaneUI ui = new JhromeTabbedPaneUI();
        tabbedPane = new JTabbedPane();
        tabbedPane.setUI(ui);
        tabbedPane.setOpaque(false);
        tabbedPane.putClientProperty(JhromeTabbedPaneUI.TAB_CLOSE_BUTTONS_VISIBLE, true);
        tabbedPane.putClientProperty(JhromeTabbedPaneUI.NEW_TAB_BUTTON_VISIBLE, true);
        tabbedPane.putClientProperty(JhromeTabbedPaneUI.CONTENT_PANEL_BORDER, new TabContentPanelBorder());
        settingButton = ui.getNewTabButton();
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void initLanguagePane(){
        languagePopup = new WebPopup();
        languagePopup.setMargin(5);
        languagePopup.setPopupStyle((PopupStyle.light));
        languagePopup.packPopup ();

        rb_zh = new WebRadioButton("汉语");
        rb_zh.setSelected(true);
        rb_zh.setAnimated(true);
        rb_we = new WebRadioButton("维语");
        rb_we.setAnimated(true);
        rb_en = new WebRadioButton("English");
        rb_en.setAnimated(true);
        rb_ru = new WebRadioButton("русский");
        rb_ru.setAnimated(true);
        UnselectableButtonGroup.group(rb_zh, rb_we, rb_en, rb_ru);
        languagePopup.add(new GroupPanel(5,false, rb_zh, rb_we, rb_en, rb_ru));
    }

    private void initListener(){
        settingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (languagePopup.isShowing()) {
                    languagePopup.hidePopup();
                } else {
                    languagePopup.showAsPopupMenu(settingButton);
                }
            }
        });

        rb_zh.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                WebRadioButton rb = (WebRadioButton) e.getSource();
                if (rb.isSelected()) {
                    UIContext.me().getBean(CacheService.class).setLanguage("zh");
                    languagePopup.hidePopup();
                    frame.getEventService().broadcast(new UIEvent(UIEventType.CHANGE_MSG_LANGUAGE, "zh"));
                }
            }
        });

        rb_we.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                WebRadioButton rb = (WebRadioButton) e.getSource();
                if (rb.isSelected()) {
                    UIContext.me().getBean(CacheService.class).setLanguage("we");
                    languagePopup.hidePopup();
                    frame.getEventService().broadcast(new UIEvent(UIEventType.CHANGE_MSG_LANGUAGE, "we"));
                }
            }
        });

        rb_en.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                WebRadioButton rb = (WebRadioButton) e.getSource();
                if (rb.isSelected()) {
                    UIContext.me().getBean(CacheService.class).setLanguage("en");
                    languagePopup.hidePopup();
                    frame.getEventService().broadcast(new UIEvent(UIEventType.CHANGE_MSG_LANGUAGE, "en"));
                }
            }
        });

        rb_ru.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                WebRadioButton rb = (WebRadioButton) e.getSource();
                if (rb.isSelected()) {
                    UIContext.me().getBean(CacheService.class).setLanguage("ru");
                    languagePopup.hidePopup();
                    frame.getEventService().broadcast(new UIEvent(UIEventType.CHANGE_MSG_LANGUAGE, "ru"));
                }
            }
        });
    }

    @Override
    public void installSkin(SkinService skinService) {
        super.installSkin(skinService);
        // 背景
        this.setPainter(skinService.getPainterByKey("skin/background"));
        settingButton.setIcon(skinService.getIconByKey("chat/settingIcon", 16, 16));
    }

}
