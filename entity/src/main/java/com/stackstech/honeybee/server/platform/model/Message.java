package com.stackstech.honeybee.server.platform.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 消息中心表<公告 / 预警>
 * <p>
 * message
 */
public class Message implements Serializable {

    private static final long serialVersionUID = -8114572443121163089L;
    private Long id;                            //主键
    private String messageLevel;                //消息级别
    private String title;                       //标题
    private String context;                     //内容
    private Timestamp expireTime;               //过期时间
    private Timestamp createTime;               //创建时间
    private Long createBy;                      //创建人
    private Timestamp updateTime;               //更新时间
    private Long updateBy;                      //更新人

    private String statusCode;                  //状态<已读/未读>
    private Long objectId;                     //MessageObject ID

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageLevel() {
        return messageLevel;
    }

    public void setMessageLevel(String messageLevel) {
        this.messageLevel = messageLevel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Timestamp getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Timestamp expireTime) {
        this.expireTime = expireTime;
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

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }
}
