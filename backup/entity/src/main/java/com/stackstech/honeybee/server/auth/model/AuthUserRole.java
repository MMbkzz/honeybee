package com.stackstech.honeybee.server.auth.model;

import lombok.Data;

import java.util.Date;

/**
 * 用户角色关系表
 * 表名：AUTH_USER_ROLE
 */
@Data
public class AuthUserRole {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;


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
