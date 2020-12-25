package com.stackstech.honeybee.server.platform.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 消息对象表
 * 19:08 2018/8/22
 * <p>
 * 消息对象表
 */
public class MessageObject implements Serializable {
    private static final long serialVersionUID = -6609576711231991851L;

    private Long id;                            //主键
    private Long messageId;                     //消息ID
    private Long userId;                        //用户ID
    private String statusCode;                  //状态
    private Timestamp createTime;               //创建时间
    private Long createBy;                      //创建人
    private Timestamp updateTime;               //更新时间
    private Long updateBy;                      //更新人

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }
}
