package com.silkroad.ui.panel;

import com.alee.laf.panel.WebPanel;
import com.silkroad.ui.service.SkinService;
import com.silkroad.ui.skin.Skin;

/**
 * 面板封装类
 *
 * Project  : iqq-projects
 * Author   : 承∮诺 < 6208317@qq.com >
 * Created  : 14-5-7
 * License  : Apache License 2.0
 */
public class UIPanel extends WebPanel implements Skin {
    public UIPanel() {
        this.setOpaque(false);
    }

    @Override
    public void installSkin(SkinService skinService) {

    }
}
