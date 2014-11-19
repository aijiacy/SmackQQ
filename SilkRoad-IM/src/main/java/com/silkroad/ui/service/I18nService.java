package com.silkroad.ui.service;

import java.util.Locale;

/**
 * i18n 国际化
 * Created by caoyong on 2014/7/6.
 */
public interface I18nService {
    /**
     * 根据设置的语言获取国际化资源
     *
     * @param messageKey
     * @return
     */
    public String getMessage(String messageKey);

    /**
     * 根据设置的语言获取国际化资源，支持格式化串
     *
     * @param messageKey
     * @param params
     * @return
     */
    public String getMessage(String messageKey, Object... params);

    /**
     * 根据固定语言获取国际化资源
     *
     * @param messageKey
     * @param locale
     * @return
     */
    public String getMessage(String messageKey, Locale locale);

    /**
     * 根据固定语言获取国际化资源，支持格式化串
     *
     * @param messageKey
     * @param locale
     * @param params
     * @return
     */
    public String getMessage(String messageKey, Locale locale, Object... params);

    /**
     * 获取当前语言
     *
     * @return
     */
    public Locale getCurrentLocale();

    /**
     * 获取国际化资源文件目录
     *
     * @return
     */
    public String getI18nDirectory();
}
