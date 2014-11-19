package com.silkroad.ui.service;

import com.alee.extended.painter.*;

import javax.swing.*;
import java.awt.*;

/**
 * Created by caoyong on 2014/7/6.
 */
public interface SkinService {
    /**
     * 获取颜色
     *
     * @return
     */
    public Color getColorByKey(String key);

    /**
     * 获取图标
     *
     * @param key
     * @return
     */
    public ImageIcon getIconByKey(String key);

    /**
     * 获取资源文件图片，调整为固定大小
     *
     * @param key
     * @param width
     * @param height
     * @return
     */
    public ImageIcon getIconByKey(String key, int width, int height);

    /**
     * 获取点9图的Painter
     *
     * @param key
     * @return
     */
    public com.alee.extended.painter.Painter getPainterByKey(String key);

    /**
     * 获取默认的皮肤配置文件
     *
     * @return
     */
    public String getDefaultConfig();

    /**
     * 获取自定义皮肤目录
     *
     * @return
     */
    public String getDirectory();

    /**
     * 设置自定义皮肤目录
     *
     * @param path
     */
    public void setDirectory(String path);

    /**
     * 是否启用了自定义皮肤
     * @return
     */
    public boolean isEnabledCustom();

    /**
     * 设置启用自定义皮肤
     *
     * @param enable
     */
    public void setEnableCustom(boolean enable);

    /**
     * 设置默认字体
     *
     * @param vFont
     */
    public void setDefaultFont(Font vFont);
}
