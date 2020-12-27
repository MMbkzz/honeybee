package com.stackstech.honeybee.server.core.entity;

import lombok.Data;

import java.util.Date;

@Data
public class AuditLogEntity {
    private Long id;

    private String logTitle;

    private String logAudit;

    private String logType;

    private String logContent;

    private Integer status;

    private Long owner;

    private Date updatetime;

    private Date createtime;

    private String desc;

}