package im.smack.core;

import im.smack.actor.IMSmkActor;
import im.smack.bean.IMSmkAccount;
import im.smack.event.IMSmkNotifyEvent;

public interface IMSmkContext {
	public void pushActor(IMSmkActor actor);
	public void fireNotify(IMSmkNotifyEvent event);
	public <T extends IMSmkModule> T getModule(IMSmkModule.Type type);
	public <T extends IMSmkService> T getSerivce(IMSmkService.Type type);
	public IMSmkAccount getAccount();
	public IMSmkSession getSession();
    public IMSmkStore getStore();
}
