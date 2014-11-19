package com.silkroad.ui.service;

import com.silkroad.ui.bean.UIBuddyCategory;
import com.silkroad.ui.bean.UIUser;

import java.util.List;

/**
 * Created by caoyong on 2014/7/16.
 */
public interface CacheService {

    public void setUser(UIUser uiUser);
    public UIUser getUser();

    public void setUICategories(List<UIBuddyCategory> uiBuddyCategories);
    public List<UIBuddyCategory> getUICategories();

    public void setLanguage(String name);

    public String getLanguage();
}
