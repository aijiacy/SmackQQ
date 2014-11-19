package com.silkroad.ui.bean;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Project  : iqq-projects
 * Author   : 承∮诺 < 6208317@qq.com >
 * Created  : 14-5-8
 * License  : Apache License 2.0
 */
public class UIBuddyCategory extends UICategory implements Serializable {
    private List<UIBuddy> buddyList = new LinkedList<UIBuddy>();

    public List<UIBuddy> getBuddyList() {
        return buddyList;
    }

    public void setBuddyList(List<UIBuddy> buddyList) {
        this.buddyList = buddyList;
    }
}
