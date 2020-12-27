package com.stackstech.honeybee.server.core.entity;

import lombok.Data;

import java.util.Date;

@Data
public class QualityJobEntity {
    private Long id;

    private String jobName;

    private String jobCode;

    private String jobExpression;

    private Integer jobOrder;

    private Integer status;

    private Long owner;

    private Date updatetime;

    private Date createtime;

    private String desc;

}