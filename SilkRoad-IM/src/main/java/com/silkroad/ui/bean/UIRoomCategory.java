package com.silkroad.ui.bean;

import java.util.List;

/**
 * Project  : iqq-projects
 * Author   : 承∮诺 < 6208317@qq.com >
 * Created  : 14-5-9
 * License  : Apache License 2.0
 */
public class UIRoomCategory extends UICategory {
    private List<UIRoom> roomList;

    public List<UIRoom> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<UIRoom> roomList) {
        this.roomList = roomList;
    }
}
