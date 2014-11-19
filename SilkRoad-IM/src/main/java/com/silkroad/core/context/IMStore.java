package com.silkroad.core.context;

import com.silkroad.core.bean.IMBuddy;
import com.silkroad.core.bean.IMCategory;
import com.silkroad.core.bean.IMGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 存储好友，分组，群，讨论组 信息。
 * Created by XJNT-CY on 2014/7/15.
 */
public class IMStore implements IMLifeCycle {

    private String language; //设置语言
    private Map<String, IMCategory> categoryMap;//分组信息
    private Map<String, IMBuddy> buddyMap; //好友列表
    private Map<String, IMGroup> groupMap; //群组列表

    public IMStore() {
        language = "zh";
        categoryMap = new HashMap<String, IMCategory>();
        buddyMap = new HashMap<String, IMBuddy>();
        groupMap = new HashMap<String, IMGroup>();
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void addCategory(IMCategory category) {
        getCategoryMap().put(category.getName(), category);
    }

    public void addBuddy(IMBuddy buddy) {
        getBuddyMap().put(buddy.getJid(), buddy);
    }

    public void addGroup(IMGroup group) {
        getGroupMap().put(group.getName(), group);
    }

    public void deleteCategory(IMCategory category) {
        getCategoryMap().remove(category.getName());
    }

    public void deleteBuddy(IMBuddy buddy) {
        getBuddyMap().remove(buddy.getJid());
    }

    public void deleteGroup(IMGroup group) {
        getGroupMap().remove(group.getName());
    }

    public Map<String, IMCategory> getCategoryMap() {
        return categoryMap;
    }

    public Map<String, IMBuddy> getBuddyMap() {
        return buddyMap;
    }

    public Map<String, IMGroup> getGroupMap() {
        return groupMap;
    }

    public IMCategory getCategory(String name){
        return getCategoryMap().get(name);
    }

    public IMBuddy getBuddy(String jid){
        return getBuddyMap().get(jid);
    }

    public IMGroup getGroup(String name){
        return getGroupMap().get(name);
    }


}
