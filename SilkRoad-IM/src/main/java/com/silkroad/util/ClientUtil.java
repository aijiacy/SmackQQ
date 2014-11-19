package com.silkroad.util;

import org.jivesoftware.smack.util.StringUtils;

/**
 * Created by caoyong on 2014/8/12.
 */
public class ClientUtil {
    public static String convLocalJID(String localRes, String src){
        String bareAddress = StringUtils.parseBareAddress(src);
        return bareAddress + "/" + localRes;
    }
}
