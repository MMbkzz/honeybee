package com.stackstech.honeybee.server.core.entity;

import java.util.Date;

public class AssetsModelEntity {
    private Long id;

    private String assetsModelName;

    private String assetsModelCode;

    private Long assetsCatalogDomain;

    private Long assetsCatalogTopic;

    private Long datasourceId;

    private String datasourceMeta;

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

    public String getAssetsModelName() {
        return assetsModelName;
    }

    public void setAssetsModelName(String assetsModelName) {
        this.assetsModelName = assetsModelName;
    }

    public String getAssetsModelCode() {
        return assetsModelCode;
    }

    public void setAssetsModelCode(String assetsModelCode) {
        this.assetsModelCode = assetsModelCode;
    }

    public Long getAssetsCatalogDomain() {
        return assetsCatalogDomain;
    }

    public void setAssetsCatalogDomain(Long assetsCatalogDomain) {
        this.assetsCatalogDomain = assetsCatalogDomain;
    }

    public Long getAssetsCatalogTopic() {
        return assetsCatalogTopic;
    }

    public void setAssetsCatalogTopic(Long assetsCatalogTopic) {
        this.assetsCatalogTopic = assetsCatalogTopic;
    }

    public Long getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(Long datasourceId) {
        this.datasourceId = datasourceId;
    }

    public String getDatasourceMeta() {
        return datasourceMeta;
    }

    public void setDatasourceMeta(String datasourceMeta) {
        this.datasourceMeta = datasourceMeta;
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