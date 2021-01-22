package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditLogEntity extends DataEntity {
    private Long id;

    private String logTitle;

    private String logAudit;

    private String logType;

    private String logContent;

    private String desc;

}