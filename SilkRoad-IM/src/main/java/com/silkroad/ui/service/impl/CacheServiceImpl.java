package com.silkroad.ui.service.impl;

import com.silkroad.ui.bean.UIAccount;
import com.silkroad.ui.bean.UIBuddyCategory;
import com.silkroad.ui.bean.UICategory;
import com.silkroad.ui.bean.UIUser;
import com.silkroad.ui.service.CacheService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by caoyong on 2014/7/16.
 */
@Service
public class CacheServiceImpl implements CacheService {
    private UIUser uiUser;
    private List<UIBuddyCategory> uiCategories;
    private String language;

    public CacheServiceImpl(){
        uiUser = new UIAccount();
    }

    @Override
    public void setUser(UIUser uiUser) {
        this.uiUser = uiUser;
    }

    @Override
    public UIUser getUser() {
        return uiUser;
    }

    @Override
    public void setUICategories(List<UIBuddyCategory> uiCategories) {
        this.uiCategories = uiCategories;
    }

    @Override
    public List<UIBuddyCategory> getUICategories() {
        return uiCategories;
    }

    @Override
    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String getLanguage() {
        return language;
    }
}
