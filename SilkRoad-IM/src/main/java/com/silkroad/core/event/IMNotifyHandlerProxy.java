package com.silkroad.core.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by caoyong on 2014/7/8.
 */
public class IMNotifyHandlerProxy implements IMNotifyListener{
    private static final Logger LOG = LoggerFactory.getLogger(IMNotifyHandlerProxy.class);
    private Object proxyObject;
    private Map<IMNotifyEvent.Type, Method> methodMap;
    public IMNotifyHandlerProxy(Object proxyObject){
        this.proxyObject = proxyObject;
        this.methodMap = new HashMap<IMNotifyEvent.Type, Method>();
        for (Method m : proxyObject.getClass().getDeclaredMethods()) {
            if(m.isAnnotationPresent(IMNotifyHandler.class)){
                IMNotifyHandler handler = m.getAnnotation(IMNotifyHandler.class);
                this.methodMap.put(handler.value(), m);
                if(!m.isAccessible()){
                    m.setAccessible(true);
                }
            }
        }
    }

    @Override
    public void onNotifyEvent(IMNotifyEvent event) {
        Method m =  methodMap.get(event.getType());
        if(m != null){
            try {
                m.invoke(proxyObject, event);
            } catch (Throwable e) {
                LOG.warn("invoke IMNotifyHandler Error!!", e);
            }
        }else{
            LOG.warn("Not found IMNotifyHandler for IMNotifyEvent = " + event);
        }
    }
}
