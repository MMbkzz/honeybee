package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMessageEntity extends DataEntity {
    private Long id;

    private String messageTitle;

    private String messageType;

    private String messageContent;

    private Long messageReceiver;

    private String desc;

}