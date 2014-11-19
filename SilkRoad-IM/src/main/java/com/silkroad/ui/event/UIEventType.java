package com.silkroad.ui.event;
 /*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE com.alee.extended.breadcrumb.file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this com.alee.extended.breadcrumb.file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this com.alee.extended.breadcrumb.file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Project  : iqq
 * Author   : solosky < solosky772@qq.com >
 * Created  : 5/3/14
 * License  : Apache License 2.0
 */
public enum UIEventType {
    LOGIN_READY_REQUEST,
    LOGIN_READY_SUCCESS,
    LOGIN_READY_ERROR,
    LOGIN_REQUEST,
    LOGIN_SUCCESS,
    LOGIN_PROGRESS,
    LOGIN_ERROR,
    SELF_FACE_UPDATE,
    SELF_STATUS_UPDATE,
    SELF_INFO_UPDATE,
    SELF_SIGN_UPDATE,
    BUDDY_LIST_REQUEST,
    BUDDY_LIST_UPDATE,
    BUDDY_ADD_REQUEST,
    BUDDY_ADD_SUCCESS,
    BUDDY_ADD_ERROR,
    REQUEST_ADD_BUDDY,
    REQUEST_ADDED_BUDDY,
    USER_FACE_UPDATE,
    /******/
    SEND_CHATMSG_REQUEST,
    SEND_CHATMSG_SUCCESS,
    SEND_CHATMSG_ERROR,
    /******/
    CHANGE_MSG_LANGUAGE,
    RECV_CHATMSG_SUCCESS,
    SYNC_CHATMSG_STATE,

    /***/
    USER_REG_REQUEST,
    USER_REG_SUCCESS,
    USER_REG_ERROR,
    USER_FIND_REQUEST,
    USER_FIND_SUCCESS,
    USER_FIND_ERROR

}
