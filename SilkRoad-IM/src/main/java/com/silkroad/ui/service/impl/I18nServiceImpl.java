package com.silkroad.ui.service.impl;

import com.silkroad.ui.service.I18nService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;

/**
 * 实现国际化
 * Created by caoyong on 2014/7/6.
 */
@Service
public class I18nServiceImpl implements I18nService {

    /**
     * 资源文件名称
     */
    public static final String I18N_BUNDLE = "appBundle";
    /**
     * 资源文件目录
     */
    public static final String I18N_DIR = System.getProperty("app.dir",
            System.getProperty("user.dir")) + File.separator + "res" + File.separator + "i18n" + File.separator;
    private static final Logger LOG = LoggerFactory.getLogger(I18nServiceImpl.class);
    private Map<String, ResourceBundle> resourceBundleMap = new HashMap<String, ResourceBundle>();

    /**
     * 根据设置的语言获取国际化资源
     *
     * @param messageKey
     * @return
     */
    @Override
    public String getMessage(String messageKey) {
        return getMessage(messageKey, getCurrentLocale());
    }

    /**
     * 根据设置的语言获取国际化资源，支持格式化串
     *
     * @param messageKey
     * @param params
     * @return
     */
    @Override
    public String getMessage(String messageKey, Object... params) {
        return getMessage(messageKey, getCurrentLocale(), params);
    }

    /**
     * 根据固定语言获取国际化资源
     *
     * @param messageKey
     * @param locale
     * @return
     */
    @Override
    public String getMessage(String messageKey, Locale locale) {
        return getResourceBundle(locale).getString(messageKey);
    }

    /**
     * 根据固定语言获取国际化资源，支持格式化串
     *
     * @param messageKey
     * @param locale
     * @param params
     * @return
     */
    @Override
    public String getMessage(String messageKey, Locale locale, Object... params) {
        String message = getMessage(messageKey, locale);
        if (message != null && params != null) {
            message = MessageFormat.format(message, params);
        }
        return message;
    }

    /**
     * 获取当前语言
     *
     * @return
     */
    @Override
    public Locale getCurrentLocale() {
        return Locale.getDefault();
    }

    /**
     * 获取国际化资源文件目录
     *
     * @return
     */
    @Override
    public String getI18nDirectory() {
        return I18N_DIR;
    }

    private ResourceBundle getResourceBundle(Locale locale) {
        InputStreamReader isr = null;
        try {
            // 自定义名_语言代码_国别代码.properties
            String flag = locale.getLanguage() + "_" + locale.getCountry();
            String filename = I18N_DIR + I18N_BUNDLE + "_" + flag + ".properties";
            ResourceBundle resourceBundle = resourceBundleMap.get(flag);
            // 如果已经读取过该文件，直接从缓存中取
            if (resourceBundle != null) return resourceBundle;
            if (!new File(filename).exists()) {
                filename = I18N_DIR + I18N_BUNDLE + ".properties";
            }
            LOG.debug(filename);
            isr = new InputStreamReader(new FileInputStream(filename), "UTF-8");
            resourceBundle = new PropertyResourceBundle(isr);
            resourceBundleMap.put(flag, resourceBundle);
            return resourceBundle;
        } catch (FileNotFoundException e) {
            LOG.error("国际化资源文件没有找到", e);
        } catch (IOException e) {
            LOG.error("读取国际化资源文件出错", e);
        } finally {
            if (isr != null)
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }
}
