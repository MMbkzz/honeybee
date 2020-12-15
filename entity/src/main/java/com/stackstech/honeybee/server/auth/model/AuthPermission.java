package com.stackstech.honeybee.server.auth.model;

import lombok.Data;

import java.util.Date;

/**
 * 权限
 * 表名：dcp_AUTH_PERMISSION
 */
@Data
public class AuthPermission {
    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 资源id
     */
    private Long resourceId;

    /**
     * 操作id
     */
    private Long operationId;

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
