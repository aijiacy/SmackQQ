package com.silkroad;

import com.alee.laf.WebLookAndFeel;
import com.alee.utils.SwingUtils;
import com.silkroad.ui.frame.login.LoginFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * AppMain
 * Created by caoyong on 2014/7/6.
 */
public final class SilkRoadIMStartup {

    private static final Logger Log = LoggerFactory.getLogger(SilkRoadIMStartup.class);

    /**
     * application init...
     */
    public static void init() {
        String path = System.getProperty("user.dir");
        System.setProperty("app.dir", new File(path).getAbsolutePath());
        System.setProperty("sun.java2d.noddraw", "true");//防止输入法激活白屏
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                exit();
            }
        }));
        Log.debug("application is startup.");
    }

    /**
     * application run.
     */
    public static void startup() {
        new LoginFrame().setVisible(true);
    }

    /**
     * application quit.
     */
    public static void exit() {
        Log.debug("application is exit.");
        //System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtils.invokeLater(new Runnable() {
            @Override
            public void run() {
                Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
                init();
                startup();
            }
        });
    }

    static class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        public void uncaughtException(Thread t, Throwable e) {
            Log.debug("uncaughtException() 捕获自线程 " + t.getName() + " 抛出的异常:" , e);
        }
    }
}
