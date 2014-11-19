package com.silkroad.core.bean.args;

import com.silkroad.core.event.IMEventType;

/**
 * Created by caoyong on 2014/7/14.
 */
public class IMSocketRequest {
    private IMEventType type;
    private Object requestData;

    public IMSocketRequest(IMEventType type) {
        this.type = type;
    }

    public IMSocketRequest(IMEventType type, Object requestData) {
        this.type = type;
        this.requestData = requestData;
    }

    public IMEventType getType() {
        return type;
    }

    public void setType(IMEventType type) {
        this.type = type;
    }

    public Object getRequestData() {
        return requestData;
    }

    public void setRequestData(Object requestData) {
        this.requestData = requestData;
    }
}
