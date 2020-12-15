package com.stackstech.dcp.server.auth.model;

import lombok.Data;

import java.util.Date;

@Data
public class AuthOperation {
    /**
     * 生成long类型ID
     */
    private Long id;

    /**
     * 操作编码
     */
    private String code;

    /**
     * 操作名称
     */
    private String name;

    /**
     * 操作描述
     */
    private String descr;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建时间
     */
    private Long createBy;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建时间
     */
    private Long updateBy;


}
