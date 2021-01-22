package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QualityJobEntity extends DataEntity<QualityJobEntity> {
    private Long id;

    private String jobName;

    private String jobCode;

    private String jobExpression;

    private Integer jobOrder;

    private String desc;

}