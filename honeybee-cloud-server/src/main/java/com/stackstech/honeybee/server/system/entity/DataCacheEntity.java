package com.stackstech.honeybee.server.system.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataCacheEntity {

    private String uuid;
    private Object data;
    private Integer expire;
    private Date updatetime;

}
