package com.silkroad.ui.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * application context
 *
 * singleton mode
 *
 * Created by caoyong on 2014/7/6.
 */
public class UIContext {
    /**
     * Declare a static and singleton of the context.
     */
    private static final UIContext singleton = new UIContext();

    /**
     * ioc object.
     */
    //private static NutIoc ioc;

    private ApplicationContext applicationContext;

    private UIContext() {
        //ioc = new NutIoc(new AnnotationIocLoader("com.silkroad.im"));
        applicationContext = new ClassPathXmlApplicationContext("classpath:spring-ui-config.xml");
    }

    /**
     * get class instance object.
     *
     * @return self instance.
     */
    public static UIContext me() {
        return singleton;
    }

    /**
     * use class and name get the object.
     *
     * @param clazz    class's type.
     * @param beanName class's name.
     * @param <T>      any class.
     * @return
     */
    public <T> T getBean(Class<T> clazz, String beanName) {
        //return ioc.get(clazz, beanName);
        return applicationContext.getBean(beanName,clazz);
    }

    /**
     * use class get the object.
     *
     * @param clazz class's type.
     * @param <T>   any class.
     * @return
     */
    public <T> T getBean(Class<T> clazz) {
        //return ioc.get(clazz);
        return applicationContext.getBean(clazz);
    }

}
