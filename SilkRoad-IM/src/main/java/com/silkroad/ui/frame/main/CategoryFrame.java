package com.silkroad.ui.frame.main;

import com.silkroad.ui.frame.UIFrame;
import com.silkroad.ui.panel.main.CategoryPane;
import com.silkroad.ui.service.SkinService;
import com.silkroad.ui.skin.Skin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 * Created by caoyong on 2014/7/22.
 */
public class CategoryFrame extends UIFrame implements Skin{
    private static final Logger LOG = LoggerFactory.getLogger(CategoryFrame.class);
    private String name;
    private String action;
    private CategoryPane contentWrap;
    private int sx;
    private int sy;
    public CategoryFrame(String name, String action,int sx, int sy){
        this.name = name;
        this.action = action;
        this.sx = sx;
        this.sy = sy;
        initUI();
    }

    private void initUI(){
        this.contentWrap = new CategoryPane(this);
        setUIContentPane(contentWrap);
        setTitle(getI18nService().getMessage("category.title"));
        setPreferredSize(new Dimension(300, 160));        // 首选大小
        setAlwaysOnTop(true);
        pack();
        setLocation(sx,sy);
    }

    @Override
    public void installSkin(SkinService skinService) {
        setIconImage(getSkinService().getIconByKey("window/titleWIcon").getImage());
        contentWrap.installSkin(skinService);
        super.installSkin(skinService);
    }

}
