package com.stackstech.dcp.server.dataservice.model;

import lombok.Data;

import java.sql.Timestamp;

/**
 * api访问数据服务
 */
@Data
public class DataService {

    /**
     * 唯一，api编号,格式：API+6位数字序列号 如：API000001
     */
    private String id;

    /**
     * 关联dcp_service_model主键ID
     */
    private String serviceModelId;

    /**
     * 服务类型：data 数据服务 message 消息服务 api api服务
     */
    private String typeCode;

    /**
     * 状态: enabled 启用 disable 禁用deleted 删除
     */
    private String statusCode;

    /**
     * 数据服务名称
     */
    private String dataServiceName;

    /**
     * 数据服务描述
     */
    private String dataServiceDesc;

    /**
     * 操作类型<read/write></>
     */
    private String operateType;

    /**
     * 表达式<与Model同步></>
     */
    private String expression;

    /**
     * 请求方法：POST/GET
     */
    private String requestMethod;

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

    //url
    private String url;

    //body模板
    private String bodyPattern;

}
