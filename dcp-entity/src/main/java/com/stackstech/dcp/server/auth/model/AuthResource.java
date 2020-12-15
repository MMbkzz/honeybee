package com.stackstech.dcp.server.auth.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 资源
 */
@Data
public class AuthResource {
    /**
     * 生成long类型ID
     */
    private Long id;

    /**
     * 资源编码
     */
    private String code;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源描述
     */
    private String descr;

    /**
     * 父级资源id
     */
    private Long parentId;

    /**
     * 是否启用
     */
    private String status;

    /**
     * 分类id
     */
    private Long categoryId;
    /**
     * 排序
     */
    private Integer sort;

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

    /**
     * 预留属性1
     */
    private String attr1;

    /**
     * 预留属性2
     */
    private String attr2;

    /**
     * 预留属性3
     */
    private String attr3;

    /**
     * 预留属性4
     */
    private String attr4;

    /**
     * 预留属性5
     */
    private String attr5;

    private List<AuthOperation> operation;

    private List<AuthPermission> permission;

    private AuthCategory category;


}