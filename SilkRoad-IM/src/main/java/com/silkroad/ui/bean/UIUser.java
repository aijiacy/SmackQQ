package com.silkroad.ui.bean;

import com.silkroad.core.bean.base.IMEntity;

/**
 * Created with IntelliJ IDEA.
 * User: solosky
 * Date: 4/19/14
 * Time: 7:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class UIUser extends IMEntity {
	private static final long serialVersionUID = 1L;
	private UIStatus status;

    public UIStatus getStatus() {
        return status;
    }

    public void setStatus(UIStatus status) {
        this.status = status;
    }
}
