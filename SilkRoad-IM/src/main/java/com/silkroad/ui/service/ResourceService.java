package com.silkroad.ui.service;

import javax.swing.*;
import java.io.File;

/**
 *
 */
public interface ResourceService {
    /**
     * 获取绝对资源目录
     *
     * @return
     */
    public String getResourcePath();

    /**
     * 获取资源文件
     *
     * @param filename
     * @return
     */
    public File getFile(String filename);


    /**
     * 获取资源文件图片
     *
     * @param filename
     * @return
     */
    public ImageIcon getIcon(String filename);


    /**
     * 获取资源文件图片，调整为固定大小
     *
     * @param filename
     * @param width
     * @param height
     * @return
     */
    public ImageIcon getIcon(String filename, int width, int height);


    /**
     * 获取用户目录
     *
     * @return
     */
    public String getUserPath();

    /**
     * 获取用户目录下面的文件
     *
     * @param filename
     * @return
     */
    public File getUserFile(String filename);


    /**
     * 获取资源文件图片
     *
     * @param filename
     * @return
     */
    public ImageIcon getUserIcon(String filename);


    /**
     * 获取资源文件图片，调整为固定大小
     *
     * @param filename
     * @param width
     * @param height
     * @return
     */
    public ImageIcon getUserIcon(String filename, int width, int height);


}
