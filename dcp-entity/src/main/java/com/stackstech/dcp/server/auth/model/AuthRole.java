package com.stackstech.dcp.server.auth.model;

import lombok.Data;

import java.util.Date;

/**
 * 角色对象
 * 表名 dcp_auth_role
 * 映射 AuthRoleMapper.xml
 */
@Data
public class AuthRole {
    /**
     * 生成long类型编号ID
     */
    private Long id;

    /**
     * 角色编码
     */
    private String code;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色描述
     */
    private String descr;

    /**
     * 是否启用
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新人
     */
    private Long updateBy;


}
