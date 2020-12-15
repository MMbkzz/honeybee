package com.stackstech.dcp.server.auth.model;

import lombok.Data;

import java.util.Date;

/**
 * 组织结构
 * 表名：AUTH_ORGANIZATION
 */
@Data
public class AuthOrganization {
    /**
     * 生成long类型ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String descr;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 创建时间
     */
    private Date createTime;


}
