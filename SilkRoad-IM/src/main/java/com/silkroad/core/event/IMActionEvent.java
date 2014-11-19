package com.silkroad.core.event;

import com.silkroad.core.event.base.CoreEvent;

/**
 * Created by caoyong on 2014/7/8.
 */
public class IMActionEvent extends CoreEvent {
    private Type type;
    private Object target;

    public IMActionEvent(Type type, Object target) {
        this.type = type;
        this.target = target;
    }

    public Type getType() {
        return type;
    }

    public Object getTarget() {
        return target;
    }

    public static enum Type {
        EVT_OK, EVT_ERROR, EVT_CANCELED, EVT_RETRY
    }

    @Override
    public String toString() {
        return "QQActionEvent [type=" + type + ", target=" + target + "]";
    }
}
