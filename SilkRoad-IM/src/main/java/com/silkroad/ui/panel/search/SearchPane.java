package com.silkroad.ui.panel.search;

import com.alee.extended.layout.TableLayout;
import com.alee.extended.panel.CenterPanel;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextField;
import com.silkroad.ui.bean.UIBuddyCategory;
import com.silkroad.ui.componet.TitleComponent;
import com.silkroad.ui.event.UIActionHandlerProxy;
import com.silkroad.ui.frame.search.SearchFrame;
import com.silkroad.ui.panel.UIContentPane;
import com.silkroad.ui.service.SkinService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by caoyong on 2014/7/28.
 */
public class SearchPane extends UIContentPane {

    private SearchFrame frame;

    private WebPanel headerPanel;
    private WebPanel middlePanel;
    private WebPanel footerPanel;

    private WebLabel lb_searchUser;
    private WebLabel lb_searchName;
    private WebLabel lb_groups;
    private WebTextField txt_searchUser;
    private WebTextField txt_searchName;
    private WebComboBox cbx_groups;
    private WebButton btn_search;
    private WebButton btn_addBuddy;
    private WebButton btn_Accept;

    public SearchPane(SearchFrame frame) {
        this.frame = frame;
        initUI();
    }

    private void initUI(){

        add(createHeader(), BorderLayout.NORTH);
        add(createMiddle(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    private WebPanel createHeader(){
        headerPanel = new WebPanel();
        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new Dimension(-1, 32));

        // 我自己写了个标题组件，透明的，可以添加到每个窗口上，可以封装为默认继承
        TitleComponent titleComponent = new TitleComponent(frame);
        titleComponent.setShowSkinButton(false);
        titleComponent.setShowMaximizeButton(false);
        titleComponent.setShowMinimizeButton(false);
        titleComponent.setShowSettingButton(false);
        titleComponent.setShowCloseButton(true);
        titleComponent.getCloseButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        headerPanel.add(titleComponent, BorderLayout.NORTH);
        return headerPanel;
    }

    private WebPanel createMiddle(){
        middlePanel = new WebPanel();
        middlePanel.setOpaque(false);

        lb_searchUser = new WebLabel("用户账号");
        lb_searchName = new WebLabel("用户昵称");
        lb_groups = new WebLabel("联系人组");

        txt_searchUser = new WebTextField();
        txt_searchName = new WebTextField();

        List<UIBuddyCategory> uiBuddyCategoryList = frame.getCacheService().getUICategories();
        List<String> lstGroupNames = new ArrayList<String>();
        for (UIBuddyCategory uiBuddyCategory : uiBuddyCategoryList){
            lstGroupNames.add(uiBuddyCategory.getName());
        }

        if(lstGroupNames.size() > 0) {
            String[] items = lstGroupNames.toArray(new String[]{});
            cbx_groups = new WebComboBox(items);
        }else{
            cbx_groups = new WebComboBox(new String[]{"Friends"});
        }

        btn_search = new WebButton("搜索");
        btn_search.addActionListener(new UIActionHandlerProxy(frame, "searchUser", txt_searchUser));

        middlePanel.add(new WebPanel(new TableLayout( new double[][]{ { TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED },
                { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED } }, 5, 5 )){{
            setMargin(10);
            add(lb_searchUser,"0,0");
            add(txt_searchUser,"1,0");
            add(btn_search,"2,0");

            add(lb_searchName,"0,1");
            add(txt_searchName,"1,1");

            add(lb_groups,"0,2");
            add(cbx_groups,"1,2");

        }}, BorderLayout.CENTER);

        return middlePanel;
    }

    private WebPanel createFooter(){
        footerPanel = new WebPanel();
        footerPanel.setOpaque(true);

        btn_addBuddy = new WebButton("添加");
        btn_addBuddy.setEnabled(false);
        btn_addBuddy.addActionListener(new UIActionHandlerProxy(frame, "addBuddy", txt_searchUser, txt_searchName, cbx_groups));
        btn_Accept = new WebButton("同意");
        btn_Accept.addActionListener(new UIActionHandlerProxy(frame, "addedBuddy", txt_searchUser, txt_searchName, cbx_groups));

        if(frame.isSearch()) {
            footerPanel.add(new CenterPanel(new GroupPanel(5, btn_addBuddy)), BorderLayout.CENTER);
        }else{
            footerPanel.add(new CenterPanel(new GroupPanel(5, btn_Accept)), BorderLayout.CENTER);
        }

        return footerPanel;
    }

    @Override
    public void installSkin(SkinService skinService) {
        // 背景，可以处理QQ印象图片
        this.setPainter(skinService.getPainterByKey("skin/background"));
    }

    public void setUIData(String jid, String username){
        txt_searchUser.setText(jid);
        txt_searchName.setText(username);
        btn_addBuddy.setEnabled(true);
    }
}
