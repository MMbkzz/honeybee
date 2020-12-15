package com.stackstech.dcp.server.dataservice.model;

import java.sql.Timestamp;

/**
 * api访问日志表
 * <p>
 * dcp_access_log
 */
public class AccessLog {

    /**
     * 主键，自增
     */
    private Integer id;

    /**
     * 关联dcp_app主键ID ，请求用户
     */
    private String appId;

    /**
     * 关联dcp_data_service 主键id
     */
    private String dataServiceId;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 访问开始时间
     */
    private Timestamp accessStartTime;

    /**
     * 访问结束时间
     */
    private Timestamp accessEndTime;

    /**
     * DB执行开始时间
     */
    private Timestamp dbStartTime;

    /**
     * DB执行结束时间
     */
    private Timestamp dbEndTime;

    /**
     * 实例IP
     */
    private String instanceHost;

    /**
     * 端口
     */
    private Integer instancePort;

    /**
     * 客户端IP
     */
    private String clientHost;

    /**
     * 返回状态码
     */
    private String returnCode;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 返回行数
     */
    private Integer returnRow;

    /**
     * 返回数据量大小，单位kb
     */
    private Integer returnSize;

    private Integer accessTimes;

    private Integer execTimes;

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

    //创建用户
    private String createUser;

    //更新用户
    private String updateUser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getInstanceHost() {
        return instanceHost;
    }

    public void setInstanceHost(String instanceHost) {
        this.instanceHost = instanceHost;
    }

    public Integer getInstancePort() {
        return instancePort;
    }

    public void setInstancePort(Integer instancePort) {
        this.instancePort = instancePort;
    }

    public String getClientHost() {
        return clientHost;
    }

    public void setClientHost(String clientHost) {
        this.clientHost = clientHost;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getReturnRow() {
        return returnRow;
    }

    public void setReturnRow(Integer returnRow) {
        this.returnRow = returnRow;
    }

    public Integer getReturnSize() {
        return returnSize;
    }

    public void setReturnSize(Integer returnSize) {
        this.returnSize = returnSize;
    }

    public Timestamp getAccessStartTime() {
        return accessStartTime;
    }

    public void setAccessStartTime(Timestamp accessStartTime) {
        this.accessStartTime = accessStartTime;
    }

    public Timestamp getAccessEndTime() {
        return accessEndTime;
    }

    public void setAccessEndTime(Timestamp accessEndTime) {
        this.accessEndTime = accessEndTime;
    }

    public Timestamp getDbStartTime() {
        return dbStartTime;
    }

    public void setDbStartTime(Timestamp dbStartTime) {
        this.dbStartTime = dbStartTime;
    }

    public Timestamp getDbEndTime() {
        return dbEndTime;
    }

    public void setDbEndTime(Timestamp dbEndTime) {
        this.dbEndTime = dbEndTime;
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

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Integer getAccessTimes() {
        return accessTimes;
    }

    public void setAccessTimes(Integer accessTimes) {
        this.accessTimes = accessTimes;
    }

    public Integer getExecTimes() {
        return execTimes;
    }

    public void setExecTimes(Integer execTimes) {
        this.execTimes = execTimes;
    }
}
