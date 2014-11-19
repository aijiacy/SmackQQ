package com.silkroad.ui.manager;

import com.silkroad.ui.service.SkinService;
import com.silkroad.ui.skin.Skin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * 皮肤管理器，可以更新新皮肤
 *
 * Project  : iqq-projects
 * Author   : 承∮诺 < 6208317@qq.com >
 * Created  : 14-5-5
 * License  : Apache License 2.0
 */
public class SkinManager {
    private static final Logger LOG = LoggerFactory.getLogger(SkinManager.class);
    private static List<Skin> skinList = new LinkedList<Skin>();

    /**
     * 注册皮肤管理
     *
     * @param skin
     */
    public static void register(Skin skin) {
        skinList.add(skin);
        LOG.debug("register " + skin.getClass().getName());
    }

    /**
     * 取消注册皮肤管理，放弃持有引用，用来释放对象
     *
     * @param skin
     */
    public static void unregister(Skin skin) {
        skinList.remove(skin);
        LOG.debug("unregister " + skin.getClass().getName());
    }

    /**
     * 安装所有皮肤，更新覆盖原来的
     *
     * @param skinService
     */
    public static void installAll(SkinService skinService) {
        for(Skin skin : skinList) {
            skin.installSkin(skinService);
            LOG.debug("install " + skin.getClass().getName());
        }
    }
}
