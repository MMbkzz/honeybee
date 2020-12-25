package com.stackstech.honeybee.server.dataservice.model;

import java.sql.Timestamp;

/**
 * 服务APP授权表
 * <p>
 * app_ds
 */
public class AppDs {

    private Long id;

    /**
     * 关联api_user主键ID
     */
    private String appId;

    /**
     * 关联data_service 主键id
     */
    private String dataServiceId;

    /**
     * 数字序列
     */
    private Integer seqNum;

    /**
     * token值，系统生成
     */
    private String token;

    /**
     * token 失效时间，为4999/01/01表示永不失效
     */
    private Timestamp tokenExpiredTime;

    /**
     *
     */
    private String sample;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 修改时间
     */
    private Timestamp updateTime;

    /**
     * 修改人
     */
    private Long updateBy;


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDataServiceId() {
        return dataServiceId;
    }

    public void setDataServiceId(String dataServiceId) {
        this.dataServiceId = dataServiceId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    public Timestamp getTokenExpiredTime() {
        return tokenExpiredTime;
    }

    public void setTokenExpiredTime(Timestamp tokenExpiredTime) {
        this.tokenExpiredTime = tokenExpiredTime;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
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
