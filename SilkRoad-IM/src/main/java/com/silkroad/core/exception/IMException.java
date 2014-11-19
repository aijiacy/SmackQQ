package com.silkroad.core.exception;

/**
 * Created by XJNT-CY on 2014/7/8.
 */
public class IMException extends Exception{
    private static final long serialVersionUID = 1L;
    private IMErrorCode errorCode;

    public IMException(IMErrorCode errorCode) {
        super(errorCode.toString());
        this.errorCode = errorCode;
    }

    public IMException(IMErrorCode errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    public IMException(IMErrorCode errorCode, Throwable e) {
        super(errorCode.toString(), e);
        this.errorCode = errorCode;
    }

    public IMErrorCode getError() {
        return errorCode;
    }

    public enum IMErrorCode {
        /**登录凭证实效*/
        INVALID_LOGIN_AUTH,
        /**无效的链接*/
        INVALID_CONNECTION,
        /**不支持的功能服务*/
        FEATURE_NOT_SUPPORTED,
        /**状态无效*/
        INVALID_STATE_CHANGE,
        /** 获取好友头像失败 */
        UNEXPECTED_RESPONSE,
        /** 无效的用户 */
        INVALID_USER,
        /** 密码错误 */
        WRONG_PASSWORD,
        /** 验证码错误 */
        WRONG_CAPTCHA,
        /** 需要验证 */
        NEED_CAPTCHA,
        /** 网络错误 */
        IO_ERROR,
        /** 网络超时*/
        IO_TIMEOUT,
        /**用户没有连接服务器*/
        NOT_CONNECTED,
        /**用户没有找到*/
        USER_NOT_FOUND,
        /**用户已登陆*/
        USER_IS_LOGGEDIN,
        /**用户没有登陆*/
        USER_NOT_LOGGEDIN,
        /**回答验证问题错误*/
        WRONG_ANSWER,
        /**用户拒绝添加好友*/
        USER_REFUSE_ADD,
        /** 无法解析的结果 */
        INVALID_RESPONSE,
        /**错误的状态码*/
        ERROR_SOCKET_STATUS,
        /** 初始化错误 */
        INIT_ERROR,
        /** 用户取消操作 */
        CANCELED,
        /**无法取消*/
        UNABLE_CANCEL,
        /** 未知的资源绑定 */
        UNKNOWN_RES_BINDING,
        /**未知的错误*/
        UNKNOWN_ERROR,
        /**等待事件被中断*/
        WAIT_INTERUPPTED,
        /**等待超时*/
        WAIT_TIMEOUT
    }
}
