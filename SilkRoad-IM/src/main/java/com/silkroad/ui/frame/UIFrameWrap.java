package com.silkroad.ui.frame;

import com.alee.extended.painter.NinePatchIconPainter;
import com.alee.extended.painter.Painter;
import com.alee.laf.panel.WebPanel;
import com.alee.utils.NinePatchUtils;
import com.silkroad.ui.service.SkinService;
import com.silkroad.ui.skin.Skin;

/**
 * 窗口阴影背景
 *
 * Project  : iqq-projects
 * Author   : 承∮诺 < 6208317@qq.com >
 * Created  : 14-5-5
 * License  : Apache License 2.0
 */
public class UIFrameWrap extends WebPanel implements Skin {

    @Override
    public void installSkin(SkinService skinService) {
        // 背景只带了阴影，就只是一个底而已
        //Painter painter = skinService.getPainterByKey("window/background");
        Painter painter = new NinePatchIconPainter(NinePatchUtils.getShadeIcon(20, 1, 0.75f));
        setPainter(painter);
    }

}
