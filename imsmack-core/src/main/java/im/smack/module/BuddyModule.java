package im.smack.module;

import im.smack.IMSmkActionListener;
import im.smack.action.BuddyAction;
import im.smack.core.IMSmkService;
import im.smack.event.IMSmkActionFuture;

/**
 * Created by caoyong on 2014/7/5.
 */
public class BuddyModule extends AbstractModule {
    public IMSmkActionFuture getBuddyList(IMSmkActionListener listener){
        return this.pushAction(new BuddyAction(getContext(),listener, IMSmkService.Type.SOCKET_BUDDY));
    }
}
