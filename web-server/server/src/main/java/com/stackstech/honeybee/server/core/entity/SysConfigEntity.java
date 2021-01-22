package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysConfigEntity extends DataEntity<SysConfigEntity> {
    private Long id;

    private String configKey;

    private String configValue;

    private String desc;

}