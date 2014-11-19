package com.silkroad.core.event.base;

import com.silkroad.core.event.IMEventListener;

/**
 * Created by caoyong on 2014/7/11.
 */
public interface IMBridge extends IMEventListener {
    public void setIMApp(IMApp imApp);
}
