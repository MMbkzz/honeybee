package com.stackstech.honeybee.server.platform.vo;

/**
 * 消息查询VO
 */
public class MessageQueryVO {

    //查询条件
    private String queryString;
    //消息类型
    private String messageLevel;

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getMessageLevel() {
        return messageLevel;
    }

    public void setMessageLevel(String messageLevel) {
        this.messageLevel = messageLevel;
    }
}
