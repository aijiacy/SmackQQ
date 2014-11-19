package com.silkroad.core.manager.listener;

import com.silkroad.core.bean.args.IMSocketResponse;
import com.silkroad.core.event.IMEventType;
import com.silkroad.core.event.IMSocketListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Packet;

/**
 * Created by caoyong on 2014/7/13.
 */
public class IMPacketListener implements PacketListener {
    private IMEventType actionType;
    private IMSocketListener listener;
    public IMPacketListener(IMEventType actionType, IMSocketListener listener){
        this.actionType = actionType;
        this.listener = listener;
    }
    @Override
    public void processPacket(Packet packet) throws SmackException.NotConnectedException {
        listener.onSuccess(new IMSocketResponse(0, actionType, packet));
    }
}
