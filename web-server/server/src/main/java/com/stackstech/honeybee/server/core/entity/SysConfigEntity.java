package com.stackstech.honeybee.server.core.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SysConfigEntity {
    private Long id;

    private String configKey;

    private String configValue;

    private Long owner;

    private Date updatetime;

    private Date createtime;

    private String desc;

}