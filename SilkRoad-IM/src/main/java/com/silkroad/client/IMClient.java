package com.silkroad.client;

import com.silkroad.core.bean.IMAccount;
import com.silkroad.core.bean.IMBuddy;
import com.silkroad.core.bean.IMMsg;
import com.silkroad.core.bean.IMStatus;
import com.silkroad.core.event.IMActionListener;
import com.silkroad.core.event.future.IMFuture;

/**
 * Created by caoyong on 2014/7/11.
 */
public interface IMClient {

    public void destroy();
    public IMFuture login(IMStatus status, IMActionListener listener);
    public IMFuture relogin(IMStatus status, IMActionListener listener);
    public IMFuture logout(IMActionListener listener);
    public IMFuture changeStatus(IMStatus status, IMActionListener listener);
    public IMFuture getBuddyList(IMActionListener listener);
    public IMFuture sendShakeRequest(IMActionListener listener);
    public IMFuture sendChatRequest(IMMsg imMsg, IMActionListener listener);
    public IMFuture sendInputRequest(String jid, IMActionListener listener);
    public IMFuture getOfflineMsg();
    public IMFuture doPollMsg();
    public IMFuture createAccount(IMAccount imAccount, IMActionListener listener);
    public IMFuture findUser(String userName, IMActionListener listener);
    public IMFuture sendAddBuddyRequest(IMBuddy imBuddy, IMActionListener listener);
    public IMFuture sendAddedBuddy(IMBuddy imBuddy, IMActionListener listener);
}
