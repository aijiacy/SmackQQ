package com.silkroad.ui.bean;

/**
 * Created with IntelliJ IDEA.
 * User: solosky
 * Date: 4/19/14
 * Time: 8:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class UIBuddy extends UIUser {
    private String remark;

    private String categoryName;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
