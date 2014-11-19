package com.silkroad.ui.frame;

import com.alee.laf.rootpane.WebFrame;
import com.silkroad.ui.context.UIContext;
import com.silkroad.ui.event.UIEventDispatcher;
import com.silkroad.ui.manager.SkinManager;
import com.silkroad.ui.panel.UIContentPane;
import com.silkroad.ui.service.*;
import com.silkroad.ui.service.impl.*;
import com.silkroad.ui.skin.Skin;
import com.sun.awt.AWTUtilities;


/**
 * IM窗口抽象类，带阴影背景
 * 实现了皮肤接口
 *
 * Project  : iqq-projects
 * Author   : 承∮诺 < 6208317@qq.com >
 * Created  : 14-4-15
 * License  : Apache License 2.0
 */
public abstract class UIFrame extends WebFrame implements Skin {
    protected UIFrameWrap contentWrap;
    protected UIEventDispatcher uiEventDispatcher;

    protected I18nService i18nService;
    protected SkinService skinService;
    protected ResourceService resourceService;
    protected CacheService cacheService;
    protected EventService eventService;

    @SuppressWarnings("restriction")
	public UIFrame() {

        i18nService = UIContext.me().getBean(I18nServiceImpl.class);
        skinService = UIContext.me().getBean(SkinServiceImpl.class);
        resourceService = UIContext.me().getBean(ResourceServiceImpl.class);
        eventService = UIContext.me().getBean(EventServiceImpl.class);
        cacheService = UIContext.me().getBean(CacheServiceImpl.class);

        // 创建wrap，并设置为默认面板(该面板为窗口阴影面板)
        contentWrap = new UIFrameWrap();
        contentWrap.installSkin(getSkinService());
        super.setContentPane(contentWrap);

        // 去了默认边框
        setUndecorated(true);
        getRootPane().setDoubleBuffered(true);
        setDefaultCloseOperation(WebFrame.DISPOSE_ON_CLOSE);
        // 把窗口设置为透明
        setWindowOpaque(false);
        AWTUtilities.setWindowOpaque(this, false);

        uiEventDispatcher = new UIEventDispatcher(this);

    }

    /**
     * 设置窗口内容面板
     * @param contentPane
     */
    public void setUIContentPane(UIContentPane contentPane) {
        // 设置一个边框
        contentPane.setMargin(0, 2, 2, 2);
        contentWrap.add(contentPane);
    }

    /**
     * 安装窗口需要的皮肤
     *
     * @param skinService
     */
    @Override
    public void installSkin(SkinService skinService) {

    }

    @Override
    public void setVisible(boolean flag){
        if(flag){
            // 注册皮肤管理
            SkinManager.register(this);
            installSkin(getSkinService());
            eventService.register(uiEventDispatcher.getEventTypes(), uiEventDispatcher);
        }else{
            // 取消注册皮肤管理
            SkinManager.unregister(this);
            eventService.unregister(uiEventDispatcher.getEventTypes(), uiEventDispatcher);
        }
        super.setVisible(flag);
    }

    @Override
    public void dispose() {
        super.dispose();
        eventService.unregister(uiEventDispatcher.getEventTypes(), uiEventDispatcher);
    }

    //    @Override
//    //@Deprecated
//    public void show() {
//        // 注册皮肤管理
//        SkinManager.register(this);
//        installSkin(getSkinService());
//        super.show();
//    }
//
//    @Override
//    //@Deprecated
//    public void hide() {
//        // 取消注册皮肤管理
//        SkinManager.unregister(this);
//        super.hide();
//    }

    /**
     * 获取皮肤服务
     *
     * @return
     */
    public SkinService getSkinService() {
        return skinService;
    }

    /**
     * 获取资源文件服务
     *
     * @return
     */
    public ResourceService getResourceService() {
        return resourceService;
    }

    /**
     * 获取I18N服务
     *
     * @return
     */
    public I18nService getI18nService() {
        return i18nService;
    }

    /**
     * 获取事件服务
     * @return
     */
    public EventService getEventService() {
        return eventService;
    }

    public CacheService getCacheService() {
        return cacheService;
    }
}
