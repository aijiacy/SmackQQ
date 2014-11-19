package com.silkroad.core.context;

import com.silkroad.core.actor.IMActor;
import com.silkroad.core.bean.IMAccount;
import com.silkroad.core.bean.IMCategory;
import com.silkroad.core.bean.IMGroup;
import com.silkroad.core.event.IMNotifyEvent;

import java.util.List;

/**
 * Created by caoyong on 2014/7/8.
 */
public interface IMCoreStore {
    public void pushActor(IMActor actor);
    public void fireNotify(IMNotifyEvent event);
    public IMAccount getAccount();
    public IMSession getSession();
    public IMStore getStore();
}
