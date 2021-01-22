package com.stackstech.honeybee.server.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

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