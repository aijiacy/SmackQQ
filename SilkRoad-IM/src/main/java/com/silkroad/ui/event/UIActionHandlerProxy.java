package com.silkroad.ui.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

/**
 *
 */
public class UIActionHandlerProxy implements ActionListener {
    private static final Logger logger = LoggerFactory.getLogger(UIActionHandlerProxy.class.getName());
    private Object proxyObject;
    private String actionName;
    private Method method;
    private Object[] params;

    public UIActionHandlerProxy(Object proxyObject, String actionName, Object... params) {
        this.proxyObject = proxyObject;
        this.actionName = actionName;
        this.params = params;

        for (Method m : proxyObject.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(UIActionHandler.class)) {
                UIActionHandler handler = m
                        .getAnnotation(UIActionHandler.class);
                if(!handler.name().isEmpty() && actionName.equals(handler.name())) {
                    this.method = m;
                } else if(handler.name().isEmpty() && actionName.equals(m.getName())) {
                    this.method = m;
                }
                if (!m.isAccessible()) {
                    m.setAccessible(true);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if(method == null) {
            throw new IllegalArgumentException("invoke IMActionHandlerProxy Error!! no method: " + actionName);
        }
        try {
            if (method.getParameterTypes().length == 0) {
                method.invoke(proxyObject);
                return;
            }
            Class<?>[] types = method.getParameterTypes();
            if(types[0].equals(ActionEvent.class)) {
                if(types.length == 1) {
                    method.invoke(proxyObject, evt);
                } else {
                    Object[] ps = new Object[params.length+1];
                    ps[0] = evt;
                    for(int i=1; i<params.length + 1; i++) {
                        ps[i] = params[i-1];
                    }
                    method.invoke(proxyObject, ps);
                }
            } else {
                method.invoke(proxyObject, params);
            }
        } catch (Throwable e) {
            logger.warn("invoke IMActionHandlerProxy Error!!" , e);
        }
    }
}
