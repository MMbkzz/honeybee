package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysConfigEntity extends DataEntity {
    private Long id;

    private String configKey;

    private String configValue;

    private String desc;

}