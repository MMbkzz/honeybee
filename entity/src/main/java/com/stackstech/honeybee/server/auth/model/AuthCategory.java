package com.stackstech.honeybee.server.auth.model;

import lombok.Data;

import java.util.Date;

@Data
public class AuthCategory {
    /**
     * 生成long类型ID
     */
    private Long id;

    /**
     * 分类编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String descr;

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
