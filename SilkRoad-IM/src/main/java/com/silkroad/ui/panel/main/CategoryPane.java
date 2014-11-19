package com.silkroad.ui.panel.main;

import com.alee.extended.layout.TableLayout;
import com.alee.extended.panel.CenterPanel;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextField;
import com.silkroad.ui.componet.TitleComponent;
import com.silkroad.ui.frame.main.CategoryFrame;
import com.silkroad.ui.panel.UIContentPane;
import com.silkroad.ui.service.SkinService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by caoyong on 2014/7/22.
 */
public class CategoryPane extends UIContentPane {
    private CategoryFrame frame;

    private WebPanel headerPanel;
    private WebPanel middlePanel;

    private WebLabel lblCategory;
    private WebTextField txtCategory;
    private WebButton btnOK;
    private WebButton btnCancel;
    public CategoryPane(CategoryFrame frame){
        this.frame = frame;
        this.add(createHeader(), BorderLayout.NORTH);
        this.add(createMiddel(), BorderLayout.CENTER);
    }

    @Override
    public void installSkin(SkinService skinService) {
        this.setPainter(skinService.getPainterByKey("skin/background"));
    }

    private WebPanel createHeader(){
        headerPanel = new WebPanel();
        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new Dimension(-1, 32));
        TitleComponent titleComponent = new TitleComponent(frame);
        titleComponent.setShowSettingButton(false);
        titleComponent.setShowSkinButton(false);
        titleComponent.setShowMaximizeButton(false);
        titleComponent.setShowMinimizeButton(false);
        titleComponent.getCloseButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        headerPanel.add(titleComponent, BorderLayout.NORTH);
        return headerPanel;
    }

    private WebPanel createMiddel(){
        middlePanel = new WebPanel();
        middlePanel.setOpaque(true);

        lblCategory = new WebLabel("名称", WebLabel.TRAILING);
        txtCategory = new WebTextField( 15 );

        btnOK = new WebButton("确定");
        btnCancel = new WebButton("取消");

        middlePanel.add(new WebPanel(new TableLayout( new double[][]{ { TableLayout.PREFERRED, TableLayout.FILL },
                { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED } }, 5, 5 ))
        {
            {
                setRound(10);
                setMargin(10,10,5,10);
                add ( lblCategory, "0,0" );
                add ( txtCategory, "1,0" );
                add ( new CenterPanel( new GroupPanel( 5, btnOK, btnCancel ) ), "0,1,1,1" );
            }
        }, BorderLayout.CENTER);
        return middlePanel;
    }
}
