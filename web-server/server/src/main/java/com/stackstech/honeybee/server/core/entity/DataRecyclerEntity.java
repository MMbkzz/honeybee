package com.stackstech.honeybee.server.core.entity;

import java.util.Date;

public class DataRecyclerEntity {
    private Long id;

    private Long assetsModelId;

    private Long assetsDataSize;

    private Long assetsDataCount;

    private Integer status;

    private Long owner;

    private Date updatetime;

    private Date createtime;

    private String desc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAssetsModelId() {
        return assetsModelId;
    }

    public void setAssetsModelId(Long assetsModelId) {
        this.assetsModelId = assetsModelId;
    }

    public Long getAssetsDataSize() {
        return assetsDataSize;
    }

    public void setAssetsDataSize(Long assetsDataSize) {
        this.assetsDataSize = assetsDataSize;
    }

    public Long getAssetsDataCount() {
        return assetsDataCount;
    }

    public void setAssetsDataCount(Long assetsDataCount) {
        this.assetsDataCount = assetsDataCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getOwner() {
        return owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}