package com.silkroad.core.bean.args;

import com.silkroad.core.event.IMEventType;

/**
 * Created by caoyong on 2014/7/8.
 */
public class IMSocketResponse {
    private int retCode;
    private IMEventType type;
    private Object retData;

    public IMSocketResponse() {
    }

    public IMSocketResponse(int retCode, IMEventType responseType, Object retData) {
        this.setRetCode(retCode);
        this.setType(responseType);
        this.setRetData(retData);
    }

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public Object getRetData() {
        return retData;
    }

    public void setRetData(Object retData) {
        this.retData = retData;
    }

    public IMEventType getType() {
        return type;
    }

    public void setType(IMEventType type) {
        this.type = type;
    }
}
