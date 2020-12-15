package com.stackstech.dcp.server.auth.model;

import lombok.Data;

import java.util.Date;

@Data
public class AuthOrgUser {
    /**
     * 组织id
     */
    private Long orgId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;


}
