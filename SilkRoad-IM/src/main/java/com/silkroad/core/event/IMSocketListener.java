package com.silkroad.core.event;


import com.silkroad.core.bean.args.IMSocketResponse;

/**
 * Created by XJNT-CY on 2014/7/8.
 */
public interface IMSocketListener {
    public void onSuccess(IMSocketResponse response);
    public void onFailed(Throwable te);
}
