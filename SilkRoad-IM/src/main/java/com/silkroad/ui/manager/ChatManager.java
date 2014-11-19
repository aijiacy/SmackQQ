package com.silkroad.ui.manager;

import com.silkroad.ui.bean.UIBuddy;
import com.silkroad.ui.bean.UIRoom;
import com.silkroad.ui.bean.UIUser;
import com.silkroad.core.bean.base.IMEntity;
import com.silkroad.ui.frame.chat.ChatFrame;
import com.silkroad.ui.panel.chat.BasicPanel;
import com.silkroad.ui.panel.chat.RoomPanel;
import com.silkroad.ui.panel.chat.UserPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 聊天窗口管理类，用于管理聊天对话
 *
 * Project  : iqq-projects
 * Author   : 承∮诺 < 6208317@qq.com >
 * Created  : 14-5-12
 * License  : Apache License 2.0
 */
public class ChatManager {
    private static final Logger LOG = LoggerFactory.getLogger(ChatManager.class);
    private static ChatFrame chatFrame;
    private static Map<IMEntity, BasicPanel> entityMap;

    public static void addChat(IMEntity entity) {
        if(chatFrame == null) {
            chatFrame = new ChatFrame();
            entityMap = new HashMap<IMEntity, BasicPanel>();
            LOG.debug("创建了一个聊天窗口界面");
        }

        if(!entityMap.containsKey(entity)) {
            if(entity instanceof UIUser) {
                // 用户对话
                UserPanel entityPanel = new UserPanel((UIUser) entity);
                entityMap.put(entity, entityPanel);
                chatFrame.addBuddyPane((UIBuddy) entity, entityPanel);
            } else if(entity instanceof UIRoom) {
                // 聊天室对话
                RoomPanel entityPanel = new RoomPanel((UIRoom) entity);
                entityMap.put(entity, entityPanel);
                chatFrame.addRoomPane((UIRoom) entity, entityPanel);
            } else {
                LOG.warn("未知的聊天实体类，无法打开聊天对话");
            }
        } else {
            // 已经打开了对话，直接进行选中状态
            chatFrame.setSelectedChat(entityMap.get(entity));
        }
        if(!chatFrame.isVisible()) {
            chatFrame.setVisible(true);
        }
    }

    /**
     * 删除了一个对话
     *
     * @param entity
     */
    public static void removeChat(IMEntity entity) {
        entityMap.remove(entity);
    }

    /**
     * 对话窗口已经关闭，进行清除处理
     */
    public static void clearChats() {
        entityMap.clear();
    }

    public static ChatFrame getChatFrame() {
        return chatFrame;
    }
}
