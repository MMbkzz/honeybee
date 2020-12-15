package com.stackstech.honeybee.core.enums;

/**
 * 站内信模块 code
 */
public enum SitemsgCode {
    /**
     * sitemsg code.
     */
    SITEMSG_CODE("站内信", "sitemsg", 16),

    /**
     * sitemsg_sendmsg表插入错误.
     */
    SITEMSG_SENDMSG_ERROR("Receiverid is null", "接收人为空", 110),
    /**
     * sitemsg_sendmsg表msgType错误.
     */
    SITEMSG_SENDMSG_MSGTYPE_ERROR("Msgtype is error", "类型错误", 111),
    /**
     * sitemsg_sendmsg表id不存在.
     */
    SITEMSG_SENDMSG_ID_ERROR("MsgId not exist", "数据不存在", 112),
    /**
     * 批量删除列表为null.
     */
    SITEMSG_RECVMSG_DELETELIST_ERROR("Delete list is null", "删除数据不存在", 114),
    /**
     * 一对多发送站内信人数上限错误.
     */
    SITEMSG_SENDMSG_TOPLIMIT_ERROR("The recipient toplimit error", "发送人数超过上限", 113),

    /**
     * sitemsg_recvmsg表，msgId为null的时候错误.
     */
    SITEMSG_RECVMSG_QUERY_ERROR("MsgId is null", "数据不存在", 120),
    /**
     * sitemsg status_type表不为 1-3 错误.
     */
    SITEMSG_RECVMSG_STATUS_ERROR("Status is Error", "状态错误", 121),
    /**
     * MSGID为不存在的时候错误.
     */
    SITEMSG_RECVMSG_MSGID_ERROR("Msgid not exist", "数据不存在", 122),
    /**
     * receiverId不存在的时候错误.
     */
    SITEMSG_RECVMSG_RECEIVERID_ERROR("Receiverid not exist", "数据不存在", 123),
    /**
     * receiverId不存在的时候错误.
     */
    SITEMSG_RECVMSG_RESULT_ERROR("Result is null", "数据不存在", 124),

    /**
     * 语言类型错误.
     */
    NO_SITEMSG_LANGUAGE_ANALYSIS_ERRO("Language analysis error", "语言错误", 130),

    /**
     * 权限模块错误码.
     */
    SITEMSG_PERMISSION_ERROR("Authentication failed", "权限不足", 101),
    /**
     * 登录错误码.
     */
    SITEMSG_LOGIN_ERROR("Authentication failed", "未登录", 100),

    /**
     * sitemsg模块内部未知错误.
     */
    SITEMSG_INNER_ERROR("Inner Exception", "内部错误", 999);
    private String description; //代码描述
    private String message;
    private int code; //代码

    SitemsgCode(String description, String message, int code) {
        this.description = description;
        this.message = message;
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
