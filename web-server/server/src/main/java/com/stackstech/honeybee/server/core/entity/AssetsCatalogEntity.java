package com.stackstech.honeybee.server.core.entity;

import java.util.Date;

public class AssetsCatalogEntity {
    private Long id;

    private Integer isroot;

    private String catalogName;

    private String catalogCode;

    private Long catalogParentId;

    private String catalogType;

    private Integer catalogOrder;

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

    public Integer getIsroot() {
        return isroot;
    }

    public void setIsroot(Integer isroot) {
        this.isroot = isroot;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getCatalogCode() {
        return catalogCode;
    }

    public void setCatalogCode(String catalogCode) {
        this.catalogCode = catalogCode;
    }

    public Long getCatalogParentId() {
        return catalogParentId;
    }

    public void setCatalogParentId(Long catalogParentId) {
        this.catalogParentId = catalogParentId;
    }

    public String getCatalogType() {
        return catalogType;
    }

    public void setCatalogType(String catalogType) {
        this.catalogType = catalogType;
    }

    public Integer getCatalogOrder() {
        return catalogOrder;
    }

    public void setCatalogOrder(Integer catalogOrder) {
        this.catalogOrder = catalogOrder;
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