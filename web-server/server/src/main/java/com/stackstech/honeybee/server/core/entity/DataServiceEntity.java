package com.stackstech.honeybee.server.core.entity;

import java.util.Date;

public class DataServiceEntity {
    private Long id;

    private String dataServiceName;

    private String dataServiceCode;

    private Long assetsModelId;

    private String datasourceMeta;

    private String serviceMeta;

    private Integer cacheExpire;

    private String expression;

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

    public String getDataServiceName() {
        return dataServiceName;
    }

    public void setDataServiceName(String dataServiceName) {
        this.dataServiceName = dataServiceName;
    }

    public String getDataServiceCode() {
        return dataServiceCode;
    }

    public void setDataServiceCode(String dataServiceCode) {
        this.dataServiceCode = dataServiceCode;
    }

    public Long getAssetsModelId() {
        return assetsModelId;
    }

    public void setAssetsModelId(Long assetsModelId) {
        this.assetsModelId = assetsModelId;
    }

    public String getDatasourceMeta() {
        return datasourceMeta;
    }

    public void setDatasourceMeta(String datasourceMeta) {
        this.datasourceMeta = datasourceMeta;
    }

    public String getServiceMeta() {
        return serviceMeta;
    }

    public void setServiceMeta(String serviceMeta) {
        this.serviceMeta = serviceMeta;
    }

    public Integer getCacheExpire() {
        return cacheExpire;
    }

    public void setCacheExpire(Integer cacheExpire) {
        this.cacheExpire = cacheExpire;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
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