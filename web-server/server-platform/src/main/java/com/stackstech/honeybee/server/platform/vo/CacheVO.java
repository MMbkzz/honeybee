package com.stackstech.honeybee.server.platform.vo;

/**
 * 缓存管理VO
 */
public class CacheVO {

    private int id;                                 //序号
    private String key;                             //key值
    private String value;                           //value值
    private Long expireTime;                        //过期时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }
}
