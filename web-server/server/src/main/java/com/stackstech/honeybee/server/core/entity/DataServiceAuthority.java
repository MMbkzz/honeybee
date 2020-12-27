package com.stackstech.honeybee.server.core.entity;

import java.util.Date;

public class DataServiceAuthority {
    private Long id;

    private Long tenantId;

    private Long dataServiceId;

    private String authorityToken;

    private Long authorityExpire;

    private String authorityData;

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

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getDataServiceId() {
        return dataServiceId;
    }

    public void setDataServiceId(Long dataServiceId) {
        this.dataServiceId = dataServiceId;
    }

    public String getAuthorityToken() {
        return authorityToken;
    }

    public void setAuthorityToken(String authorityToken) {
        this.authorityToken = authorityToken;
    }

    public Long getAuthorityExpire() {
        return authorityExpire;
    }

    public void setAuthorityExpire(Long authorityExpire) {
        this.authorityExpire = authorityExpire;
    }

    public String getAuthorityData() {
        return authorityData;
    }

    public void setAuthorityData(String authorityData) {
        this.authorityData = authorityData;
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