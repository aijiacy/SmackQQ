package com.silkroad.ui.manager;

import com.silkroad.ui.context.UIContext;
import com.silkroad.ui.event.UIEvent;
import com.silkroad.ui.event.UIEventType;
import com.silkroad.ui.service.impl.EventServiceImpl;
import com.silkroad.ui.service.impl.I18nServiceImpl;
import com.silkroad.ui.frame.main.MainFrame;

import java.awt.*;
import java.awt.event.*;
import java.awt.peer.SystemTrayPeer;

/**
 * Project  : iqq-projects
 * Author   : 承∮诺 < 6208317@qq.com >
 * Created  : 14-5-12
 * License  : Apache License 2.0
 */
public class MainManager {
    private static SystemTray tray;
    private static TrayIcon icon;
    private static MainFrame mainFrame;

    public static void show() {
        if(mainFrame == null) {
            mainFrame = new MainFrame();
            mainFrame.setVisible(true);
            //放置托盘
            enableTray();
            mainFrame.addWindowStateListener(new WindowStateListener() {
                @Override
                public void windowStateChanged(WindowEvent e) {
                    if (e.getNewState() == Frame.ICONIFIED) {
                        hide();
                    }
                }
            });
        }
        if(!mainFrame.isVisible()){
            mainFrame.setVisible(true);
            mainFrame.setExtendedState(Frame.NORMAL);
            mainFrame.toFront();
        }
    }

    public static void hide() {
        mainFrame.setVisible(false);
    }

    public static void disableTray(){
        if(tray != null) {
            tray.remove(icon);
        }
    }

    public static void enableTray() {
        if(SystemTray.isSupported()) {

            PopupMenu pop = new PopupMenu();
            MenuItem restore = new MenuItem(UIContext.me().getBean(I18nServiceImpl.class).getMessage("showMain"));
            restore.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    show();
                }
            });
            pop.add(restore);

            tray = SystemTray.getSystemTray();
            icon = new TrayIcon(mainFrame.getIconImage(), UIContext.me().getBean(I18nServiceImpl.class).getMessage("app.name"), pop);
            icon.setImageAutoSize(true);
            icon.addMouseListener(new TrayMouseListener(mainFrame));
            try {
                tray.add(icon);
            } catch (AWTException e) {
            }
        }

    }

    static class TrayMouseListener extends MouseAdapter{
        private MainFrame frame;
        public TrayMouseListener(MainFrame frame){
            this.frame = frame;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2){
                show();
            }
        }
    }
}
